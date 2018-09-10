/*
    Copyright 2018 ZXing authors, Journey Mobile, Jonathan Sirrine

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.sirrineprogramming.mealplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    public static final String DISPLAY_NAME = "com.sirrineprogramming.mealplanner.extra.DISPLAY_NAME";
    private static final int RC_SIGN_IN = 123;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
    );

    //For demo purpose, I have provided two sample URLs. One for Privacy Policy and another for Terms of Service
    private static final String PP_URL = "";
    private static final String TOS_URL = "http://www.sirrineprogramming.com/privacypolicies/quickMessage.htm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiateUser();

        if (!isUserSignedIn())
            signInUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateUI(user);
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
                Intent i3 = new Intent ( this, SignIn.class);
                startActivity(i3);
            }
        }
    }

    private void instantiateUser(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    private void signInUser(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTosUrl(TOS_URL)
                        .setPrivacyPolicyUrl(PP_URL)
                        .setAllowNewEmailAccounts(true)
                        .setIsSmartLockEnabled(true)
                        .build(), RC_SIGN_IN);
    }

    public void signOutUser(View view){
        //Sign out
        mFirebaseAuth.signOut();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (!isUserSignedIn()) {
            //user is signed out
            Intent i3 = new Intent ( this, SignIn.class);
            startActivity(i3);
        }else{
            //check if internet connectivity is there
        }
    }

    public void startLoggedInActivityMain(){
       // Intent intent = new Intent(this, DisplayMessageActivity.class);
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        Log.d("updateUI", currentUser.getDisplayName());
    }

    private boolean isUserSignedIn(){
        if (mFirebaseUser == null){
            return false;
        }else{
            Log.d("isUserSignedIn", "user signed in");
            System.out.print(mFirebaseUser.getDisplayName());
            return true;
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null)
            updateUI(currentUser);
        else
            Log.d("onStart", "no user signed in");
    }

    public void updateUI(FirebaseUser currentUser) {
        //update user interface
        TextView textView = (TextView) findViewById(R.id.textView2);
        String displayName = currentUser.getDisplayName().toString();
        textView.setText(displayName);
        Log.d("updateUI", currentUser.getDisplayName());
    }

    public void startShoppingList(View view) {
        Intent i1 = new Intent( this, ShoppingList.class);
        startActivity(i1);
    }

    public void startPantry(View view) {
        Intent i2 = new Intent(this, Pantry.class);
        startActivity(i2);
    }

}
