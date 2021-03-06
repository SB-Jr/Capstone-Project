package com.project.sbjr.showledger.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.sbjr.showledger.R;

import static com.project.sbjr.showledger.Util.checkInternet;
import static com.project.sbjr.showledger.Util.setUserNameInSharedPreference;

public class RegisterActivity extends AppCompatActivity {


    private final static String mUserNameEditTextKey = "com.project.sbjr.showledger.ui.activity.RegisterActivity.username";
    private final static String mEmailIdEditTextKey = "com.project.sbjr.showledger.ui.activity.RegisterActivity.emailid";
    private final static String mPasswordEditTextKey = "com.project.sbjr.showledger.ui.activity.RegisterActivity.password";
    private final static String mRetypePasswordEditTextKey = "com.project.sbjr.showledger.ui.activity.RegisterActivity.retypepassword";

    private EditText mUserNameEditText;
    private EditText mEmailIdEditText;
    private EditText mPasswordEditText;
    private EditText mRetypePasswordEditText;
    private TextInputLayout mUserNameTextInputLaout;
    private TextInputLayout mEmailIdTextInputLaout;
    private TextInputLayout mPasswordTextInputLaout;
    private TextInputLayout mRetypePasswordTextInputLaout;
    private Button mRegisterButton;
    private ProgressBar mProgressBar;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mUserNameEditText = (EditText) findViewById(R.id.user_name);
        mEmailIdEditText = (EditText) findViewById(R.id.user_id);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        mRetypePasswordEditText = (EditText) findViewById(R.id.retype_password);
        mUserNameTextInputLaout = (TextInputLayout) findViewById(R.id.user_name_text_input_layout);
        mEmailIdTextInputLaout = (TextInputLayout) findViewById(R.id.user_id_text_input_layout);
        mPasswordTextInputLaout = (TextInputLayout) findViewById(R.id.password_text_input_layout);
        mRetypePasswordTextInputLaout = (TextInputLayout) findViewById(R.id.retype_password_text_input_layout);
        mRegisterButton = (Button) findViewById(R.id.register);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        if (savedInstanceState != null) {
            mEmailIdEditText.setText(savedInstanceState.getString(mEmailIdEditTextKey));
            mUserNameEditText.setText(savedInstanceState.getString(mUserNameEditTextKey));
            mPasswordEditText.setText(savedInstanceState.getString(mPasswordEditTextKey));
            mRetypePasswordEditText.setText(savedInstanceState.getString(mRetypePasswordEditTextKey));
        }


        mUserNameTextInputLaout.setErrorEnabled(true);
        mEmailIdTextInputLaout.setErrorEnabled(true);
        mPasswordTextInputLaout.setErrorEnabled(true);
        mRetypePasswordTextInputLaout.setErrorEnabled(true);


        checkInternet(this, mCoordinatorLayout);


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userid = mEmailIdEditText.getText().toString();
                String pass = mPasswordEditText.getText().toString();

                if (!checkCred()) {
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                final FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(userid, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String userUid = auth.getCurrentUser().getUid();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                final DatabaseReference reference = database.getReference().child("user").child(userUid);
                                reference.child("userid").setValue(userid, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        reference.child("username").setValue(mUserNameEditText.getText().toString(), new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                mProgressBar.setVisibility(View.GONE);
                                                setUserNameInSharedPreference(RegisterActivity.this, mUserNameEditText.getText().toString());
                                                Intent intent = new Intent(RegisterActivity.this, ShowActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(mCoordinatorLayout, getString(R.string.register_error), Snackbar.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(mPasswordEditTextKey, mPasswordEditText.getText().toString());
        outState.putString(mEmailIdEditTextKey, mEmailIdEditText.getText().toString());
        outState.putString(mRetypePasswordEditTextKey, mRetypePasswordEditText.getText().toString());
        outState.putString(mUserNameEditTextKey, mUserNameEditText.getText().toString());

        super.onSaveInstanceState(outState);
    }

    public boolean checkCred() {
        boolean success = true;

        mUserNameTextInputLaout.setErrorEnabled(false);
        mEmailIdTextInputLaout.setErrorEnabled(false);
        mPasswordTextInputLaout.setErrorEnabled(false);
        mRetypePasswordTextInputLaout.setErrorEnabled(false);

        String userid = mEmailIdEditText.getText().toString();
        String pass = mPasswordEditText.getText().toString();
        String repass = mRetypePasswordEditText.getText().toString();
        String user = mUserNameEditText.getText().toString();

        if (user.length() == 0) {
            mUserNameTextInputLaout.setError(getString(R.string.user_name_error_empty));
            success = false;
        }
        if (userid.length() == 0) {
            mEmailIdTextInputLaout.setError(getString(R.string.user_id_error_empty));
            success = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userid).matches()) {
            mEmailIdTextInputLaout.setError(getString(R.string.user_id_error_invalid));
            success = false;
        }
        if (pass.length() < 6) {
            mPasswordTextInputLaout.setError(getString(R.string.password_error_length));
            success = false;
        }
        if (!pass.equals(repass)) {
            mRetypePasswordTextInputLaout.setError(getString(R.string.retype_password_error_not_match));
            success = false;
        }
        return success;
    }
}
