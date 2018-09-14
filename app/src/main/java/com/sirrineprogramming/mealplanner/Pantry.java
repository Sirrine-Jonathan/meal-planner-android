package com.sirrineprogramming.mealplanner;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.provider.CalendarContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.sirrineprogramming.mealplanner.ItemViewActivity.EXTRA_ID;
import static com.sirrineprogramming.mealplanner.R.id.pantry_view;
import static com.sirrineprogramming.mealplanner.R.id.shopping_list_linear_layout;
import com.sirrineprogramming.mealplanner.PantryItemConverter;

public class Pantry extends AppCompatActivity {
    //Get Database Reference
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference listRef = database.getReference("users/" + user.getUid() + "/pantryItemList");
    DatabaseReference noBarcodeRef = listRef.child("noBarcode");
    DatabaseReference barcodeRef = listRef.child("barcode");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<PantryItem> items = new ArrayList<PantryItem>();

        setContentView(R.layout.activity_pantry);

        listRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    if (child.hasChild("name")) {
                        try {
                            PantryItem nextItem = PantryItemConverter.getNewItem(child);
                            Log.d("ITEM OBJ", nextItem.toString());
                            addPantryItem(nextItem, child.getKey());
                        } catch (DatabaseException de){
                            de.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        barcodeRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    if (child.hasChild("name")) {
                        try {
                            PantryItem nextItem = PantryItemConverter.getNewItem(child);
                            Log.d("ITEM OBJ", nextItem.toString());
                            addPantryItem(nextItem, child.getKey());
                        } catch (DatabaseException de){
                            de.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        noBarcodeRef.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    if (child.hasChild("name")) {
                        try {
                            PantryItem nextItem = PantryItemConverter.getNewItem(child);
                            Log.d("ITEM OBJ", nextItem.toString());
                            addPantryItemWithoutBarcode(nextItem, child.getKey());
                        } catch (DatabaseException de){
                            de.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startScanner();
            }
        });
    }


    public void addPantryItem(PantryItem item, String key)
    {
        LinearLayout linearLayout = findViewById(pantry_view);


        //Linear Layout Params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);


        int buttonStyle = R.style.itemButtonHasBarcode;
        Button btn = new Button(new ContextThemeWrapper(this, buttonStyle), null, buttonStyle);
        btn.setLayoutParams(params);
        btn.setPadding(20,20,20,20);
        btn.setText(item.getName());

        final String pantryName = item.getName();
        final String pantryKey = key;

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                Bundle bundle = new Bundle();
                bundle.putString("name", pantryName);
                bundle.putString("key", pantryKey);

                Intent i4 = new Intent (getApplicationContext(), ItemViewActivity.class);
                i4.putExtras(bundle);
                startActivity(i4);
            }
        });

        Log.d("PANTRY_ITEM", "Name: " + item.getName() + " Key: " + key);
        linearLayout.addView(btn);

        //ViewParent parent = LinearLayout.getParent();
        //this.setContentView(linearLayout, params);
    }


    public void addPantryItemWithoutBarcode(PantryItem item, String key)
    {
        LinearLayout linearLayout = findViewById(pantry_view);


        //Linear Layout Params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);

        int buttonStyle = R.style.itemButtonNoBarcode;
        Button btn = new Button(new ContextThemeWrapper(this, buttonStyle), null, buttonStyle);
        btn.setPadding(20,20,20,20);
        btn.setText(item.getName());


        final String pantryName = item.getName();
        final String pantryKey = key;

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                Bundle bundle = new Bundle();
                bundle.putString("name", pantryName);
                bundle.putString("key", pantryKey);

                Intent i4 = new Intent (getApplicationContext(), ItemWithoutBarcodeView.class);
                i4.putExtras(bundle);
                startActivity(i4);
            }
        });

        Log.d("PANTRY_ITEM", "Name: " + item.getName() + " Key: " + key);
        linearLayout.addView(btn);

        //ViewParent parent = LinearLayout.getParent();
        //this.setContentView(linearLayout, params);
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
                Bundle bundle = new Bundle();
                String code = result.getContents();
                bundle.putString("key", code);
                Intent intent = new Intent(Pantry.this, ItemViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
