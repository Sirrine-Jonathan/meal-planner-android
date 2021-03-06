package com.sirrineprogramming.mealplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sirrineprogramming.mealplanner.PantryItem;

/*
    Two ways to enter this activity
    -Choose item from pantry list that has barcode
    -scan new or recognized item
 */
public class ItemViewActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.sirrineprogramming.mealplanner.newitem.id";

    //Get Database Reference
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private EditText mNameEdit;
    private EditText mQuantityEdit;
    private EditText mPriceEdit;
    private Button mSaveButton;
    private String itemKey;
    private PantryItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set up text inputs
        mNameEdit = findViewById(R.id.edit_name);
        mQuantityEdit = findViewById(R.id.edit_quantity);
        mPriceEdit = findViewById(R.id.edit_price);


        //get stuff from intent
        Intent intent = getIntent();
        Bundle itemData = intent.getExtras();
        DatabaseReference itemRef = database.getReference("users/" + user.getUid() +
                "/pantryItemList/barcode");

        if(!itemData.isEmpty()) {
            if (itemData.containsKey("key")) {
                this.itemKey = itemData.getString("key");
                final String key = this.itemKey;
                itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        /*
                            Takes this path if reference item key is recognized
                            in pantry list lookup table
                         */
                        if (dataSnapshot.hasChild(key)){
                            DatabaseReference actualItemRef =  database.getReference("users/" + user.getUid() +
                                    "/pantryItemList/barcode/" + key);
                            actualItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String name = "unknown item";
                                    String amount = "1";
                                    String price = "0.00";

                                    PantryItem item = PantryItemConverter.getNewItem(dataSnapshot);

                                    populateFieldsForKnown(item, dataSnapshot.getKey());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } else{
            Log.d("NO DATA", "IN THE INTENT");
        }


        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                save();
            }
        });


    }

    public void populateFieldsForKnown(PantryItem item, String key){
        Log.d("ITEM VIEW", "known item with key: " + this.itemKey);
        mNameEdit.setText(item.getName());
        mQuantityEdit.setText(item.getAmountInPantry());
        mPriceEdit.setText(item.getPrice().toString());
        this.currentItem = item;
    }

    public void save()
    {
        if(!TextUtils.isEmpty(mNameEdit.getText()))
        {
            String quantity = mQuantityEdit.getText().toString();
            PantryItem pantryItem = this.currentItem;
            pantryItem.setName(mNameEdit.getText().toString());
            pantryItem.setAmountInPantry(quantity);
            DatabaseReference itemRef = database.getReference("users/" + user.getUid() +
                    "/pantryItemList/barcode");
            itemRef.child(this.itemKey).setValue(pantryItem);
            finish();
        }
    }
}
