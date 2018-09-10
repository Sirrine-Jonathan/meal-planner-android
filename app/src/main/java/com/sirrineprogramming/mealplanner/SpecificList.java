package com.sirrineprogramming.mealplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.sirrineprogramming.mealplanner.R.id.specific_list_linear_layout;

public class SpecificList extends AppCompatActivity {


    //Get Database Reference
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_list);

        //get stuff from intent
        Intent intent = getIntent();
        Bundle listData = intent.getExtras();

        //stuff from last activity
        String listName = null;
        String listKey = null;
        DatabaseReference listRef = null;

        if(!listData.isEmpty()) {

            if (listData.containsKey("name")) {
                listName = listData.getString("name");
                updateHeader(listName);
            }
            if (listData.containsKey("key")) {
                listKey = listData.getString("key");
                listRef = database.getReference("users/" + user.getUid() +
                        "/savedShoppingLists/" + listKey + "/mobile");
            }
        }
        else{
            Log.d("NO DATA", "IN THE INTENT");
        }

        //prepare the data
        ValueEventListener savedListsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    com.google.firebase.database.DataSnapshot name = child.child("name");
                    GroceryItem list = child.getValue(GroceryItem.class);
                    addCheckbox(list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        if (listKey != null)
            listRef.addListenerForSingleValueEvent(savedListsListener);
    }

    public void updateHeader(String listName) {
        TextView textView = (TextView) findViewById(R.id.specificListName);
        textView.setText(listName);
    }

    public static class GroceryItem {

        public GroceryItem(){}
        public GroceryItem(String name, double amount, String unit){
            setName(name);
            setAmount(amount);
            setUnit(unit);
        }

        //name
        public String name;
        public String getName(){ return name; }
        public void setName(String name) { this.name = name; }

        //amount
        public double amount;
        public double getAmount(){ return amount; }
        public void setAmount(double amount){ this.amount = amount; }

        //unit
        public String unit;
        public String getUnit(){ return unit; }
        public void setUnit(String unit){ this.unit = unit; }

        public String display(){
            if (unit != "")
                setUnit(getUnit() + "(s) of");
            if (getAmount() % 1 == 0)
                return (int) getAmount() + " " + getUnit() + " " + getName();
            else
                return getAmount() + " " + getUnit() + " " + getName();
        }
    }

    public void addCheckbox(GroceryItem list){
        LinearLayout linearLayout = findViewById(specific_list_linear_layout);

        //Linear Layout Params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        CheckBox ch = new CheckBox(this);
        ch.setLayoutParams(params);
        ch.setPadding(20,20,20,20);
        ch.setText(list.display());
        linearLayout.addView(ch);

        //this.setContentView(linearLayout, params);
    }
}
