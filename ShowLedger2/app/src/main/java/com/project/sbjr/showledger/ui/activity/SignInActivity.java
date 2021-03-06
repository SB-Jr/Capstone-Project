package com.project.sbjr.showledger.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sbjr.showledger.R;

import static com.project.sbjr.showledger.Util.checkInternet;
import static com.project.sbjr.showledger.Util.getUserIdFromSharedPreference;
import static com.project.sbjr.showledger.Util.getUserPassFromSharedPreference;
import static com.project.sbjr.showledger.Util.isUserDataPresent;
import static com.project.sbjr.showledger.Util.setUserIdInSharedPreference;
import static com.project.sbjr.showledger.Util.setUserNameInSharedPreference;
import static com.project.sbjr.showledger.Util.setUserPassInSharedPreference;
import static com.project.sbjr.showledger.Util.setUserUIDInSharedPreference;

public class SignInActivity extends AppCompatActivity {

    private final String TAG = SignInActivity.class.getName();

    private final static String mUseridEditTextKey = "com.project.sbjr.showledger.ui.activity.SignInActivity.userid";
    private final static String mPasswordEditTextKey = "com.project.sbjr.showledger.ui.activity.SignInActivity.password";

    private EditText mUseridEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private TextInputLayout mUserIdTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        /**
         * Check if user is already used the app before and using it again
         * or whether user is new or logged out
         * */
        if (isUserDataPresent(this)) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(getUserIdFromSharedPreference(this), getUserPassFromSharedPreference(this));
            Intent intent = new Intent(SignInActivity.this, ShowActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            MobileAds.initialize(getApplicationContext(), getString(R.string.ad_bottom));
            MobileAds.initialize(getApplicationContext(), getString(R.string.ad_search));
            MobileAds.initialize(getApplicationContext(), getString(R.string.ad_details));
            MobileAds.initialize(getApplicationContext(), getString(R.string.ad_nav));

            startActivity(intent);
        }

        mUseridEditText = (EditText) findViewById(R.id.user_id);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.login);
        mRegisterButton = (Button) findViewById(R.id.sign_up);
        mUserIdTextInputLayout = (TextInputLayout) findViewById(R.id.input_layout_user_id);
        mPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.input_layout_password);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        checkInternet(this, mCoordinatorLayout);

        if (savedInstanceState != null) {
            mUseridEditText.setText(savedInstanceState.getString(mUseridEditTextKey));
            mPasswordEditText.setText(savedInstanceState.getString(mPasswordEditTextKey));
        }


        mUserIdTextInputLayout.setErrorEnabled(true);
        mPasswordTextInputLayout.setErrorEnabled(true);


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInternet(SignInActivity.this, mCoordinatorLayout);

                final String userid = mUseridEditText.getText().toString();
                final String pass = mPasswordEditText.getText().toString();
                if (!checkEntry()) {
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(userid, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference().child("user").child(userUid).child("username");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        mProgressBar.setVisibility(View.GONE);
                                        String username = dataSnapshot.getValue(String.class);
                                        setUserNameInSharedPreference(SignInActivity.this, username);
                                        setUserIdInSharedPreference(SignInActivity.this, userid);
                                        setUserUIDInSharedPreference(SignInActivity.this, userUid);
                                        setUserPassInSharedPreference(SignInActivity.this, pass);
                                        Intent intent = new Intent(SignInActivity.this, ShowActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                        MobileAds.initialize(getApplicationContext(), getString(R.string.ad_bottom));
                                        MobileAds.initialize(getApplicationContext(), getString(R.string.ad_search));
                                        MobileAds.initialize(getApplicationContext(), getString(R.string.ad_details));
                                        MobileAds.initialize(getApplicationContext(), getString(R.string.ad_nav));

                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        mProgressBar.setVisibility(View.GONE);
                                        Log.d(TAG, databaseError.getMessage() + "->" + databaseError.getDetails());
                                        Snackbar.make(mCoordinatorLayout, R.string.signin_error, Snackbar.LENGTH_LONG).show();
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgressBar.setVisibility(View.GONE);
                                Log.d(TAG, e.getMessage());
                                mUserIdTextInputLayout.setError(getString(R.string.invalid_credentials));
                            }
                        });
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(mPasswordEditTextKey, mPasswordEditText.getText().toString());
        outState.putString(mUseridEditTextKey, mUseridEditText.getText().toString());

        super.onSaveInstanceState(outState);
    }

    private boolean checkEntry() {
        String userid = mUseridEditText.getText().toString();
        String pass = mPasswordEditText.getText().toString();

        mUserIdTextInputLayout.setErrorEnabled(false);
        mPasswordTextInputLayout.setErrorEnabled(false);

        boolean result = true;
        if (userid.length() == 0) {
            mUserIdTextInputLayout.setError(getString(R.string.user_id_error_empty));
            result = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userid).matches()) {
            mUserIdTextInputLayout.setError(getString(R.string.user_id_error_empty));
            result = false;
        }
        if (pass.length() < 6) {
            mPasswordTextInputLayout.setError(getString(R.string.user_id_error_invalid));
            result = false;
        }
        return result;
    }
}
