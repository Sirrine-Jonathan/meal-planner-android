package com.sirrineprogramming.mealplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ItemViewActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.sirrineprogramming.mealplanner.newitem.id";
    //Get Database Reference
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get stuff from intent
        Intent intent = getIntent();
        Bundle itemData = intent.getExtras();
        String itemKey = null;
        String itemName = null;
        DatabaseReference itemRef = null;

        if(!itemData.isEmpty()) {

            if (itemData.containsKey("key")) {
                itemKey = itemData.getString("key");
            }
            if (itemData.containsKey("name")) {
                itemName = itemData.getString("name");
                itemRef = database.getReference("users/" + user.getUid() +
                        "/pantryItemList/" + itemKey);
            }
            Log.d("DATA", "key: " + itemKey);
            Log.d("DATA", "key: " + itemName);
            Log.d("DATA OBJ", itemRef.toString());
        }
        else{
            Log.d("NO DATA", "IN THE INTENT");
        }

    }

}
