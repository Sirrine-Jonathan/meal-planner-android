package com.sirrineprogramming.mealplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sirrineprogramming.mealplanner.PantryItem;

public class ItemWithoutBarcodeView extends AppCompatActivity {

    //Get Database Reference
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private EditText mNameEdit;
    private EditText mBarcodeEdit;
    private Button mSaveButton;
    private String itemKey;
    PantryItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_without_barcode_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameEdit = findViewById(R.id.edit_noBarcode_name);
        mBarcodeEdit = findViewById(R.id.edit_barcode);

        //get stuff from intent
        Intent intent = getIntent();
        Bundle itemData = intent.getExtras();
        DatabaseReference itemRef = database.getReference("users/" + user.getUid() +
                "/pantryItemList/noBarcode");

        if (!itemData.isEmpty()){
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
                                    "/pantryItemList/noBarcode/" + key);
                            actualItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    PantryItem item = PantryItemConverter.getNewItem(dataSnapshot);
                                    String key = dataSnapshot.getKey().toString();
                                    populateFields(item, key);
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
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startScanner();
            }
        });

        mSaveButton = findViewById(R.id.button_no_barcode_save);
        mSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                save();
            }
        });
    }

    private void populateFields(PantryItem item, String key){
        mNameEdit.setText(item.getName());
        mBarcodeEdit.setText(key);
        this.currentItem = item;
    }

    private void save()
    {
        if(!TextUtils.isEmpty(mNameEdit.getText()))
        {

            // get item ready
            PantryItem pantryItem = this.currentItem;
            pantryItem.setName(mNameEdit.getText().toString());

            // add new item to barcode item list
            String newKey = mBarcodeEdit.getText().toString();
            DatabaseReference itemRef = database.getReference("users/" + user.getUid() +
                    "/pantryItemList/barcode");
            itemRef.child(newKey).setValue(pantryItem);

            // remove item from noBarcode item list
            DatabaseReference oldRef = database.getReference("users/" + user.getUid() +
                    "/pantryItemList/noBarcode");
            oldRef.child(this.itemKey).removeValue();
            finish();
        }
    }

    public void startScanner()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    // Get the results:
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
        {
            if(result.getContents() == null)
            {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
            else
            {
                String code = result.getContents();
                mBarcodeEdit.setText(code);
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

}
