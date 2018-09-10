package com.sirrineprogramming.mealplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.List;
import java.util.Vector;

import static com.sirrineprogramming.mealplanner.R.id.shopping_list_linear_layout;

public class ShoppingList extends AppCompatActivity {

    //Get Database Reference
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference listRef = database.getReference("users/" + user.getUid() + "/savedShoppingLists");

    //Log stuff
    private static final String TAG = "ShoppingList";
    String noDataString = "Failed to retrieve data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        //prepare the data
        ValueEventListener savedListsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    if (child.hasChild("mobile")) {
                        String name = (String) child.child("name").getValue();
                        String key = (String) child.getKey();
                        List list = new List(name, key);
                        addButton(list);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        listRef.addListenerForSingleValueEvent(savedListsListener);
    }

    public void tryToast(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void addButton(List list){
        LinearLayout linearLayout = findViewById(shopping_list_linear_layout);

        //Linear Layout Params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Button btn = new Button(this);
        btn.setLayoutParams(params);
        btn.setPadding(20,20,20,20);
        btn.setText(list.getName());
        linearLayout.addView(btn);

        final String shoppingListName = list.getName();
        final String shoppingListKey = list.getKey();
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                Bundle bundle = new Bundle();
                bundle.putString("name", shoppingListName);
                bundle.putString("key", shoppingListKey);

                Intent i4 = new Intent (getApplicationContext(), SpecificList.class);
                i4.putExtras(bundle);
                startActivity(i4);
            }
        });


        this.setContentView(linearLayout, params);
    }


    //create top level object for shoppingList
    public static class List {

        //constructor
        public List(){}
        public List(String name, String key){
            setName(name);
            setKey(key);
        }

        //name
        public String name;
        public String getName(){ return name; }
        public void setName(String name) { this.name = name; }

        //id
        public String key;
        public String getKey() {return key; }
        public void setKey(String key) { this.key = key; }

    }

}

