package com.project.sbjr.showledger.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.project.sbjr.showledger.R;

public class RegisterActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /**
         * Todo: check internet and show snackbar if it is not present
         * */

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

        mUserNameTextInputLaout.setErrorEnabled(true);
        mEmailIdTextInputLaout.setErrorEnabled(true);
        mPasswordTextInputLaout.setErrorEnabled(true);
        mRetypePasswordTextInputLaout.setErrorEnabled(true);


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userid = mEmailIdEditText.getText().toString();
                String pass = mPasswordEditText.getText().toString();

                if(!checkCred()){
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(userid, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                /**
                                 * Todo: add the user details to firebase and change sharedpref data
                                 * */
                                mProgressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(RegisterActivity.this,ShowActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                /**
                                 * Todo:add snackbar that there was a problem
                                 * */
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    public boolean checkCred(){
        boolean success = true;

        String userid = mEmailIdEditText.getText().toString();
        String pass = mPasswordEditText.getText().toString();
        String repass = mRetypePasswordEditText.getText().toString();
        String user = mUserNameEditText.getText().toString();

        if(user.length()==0){
            mUserNameTextInputLaout.setError(getString(R.string.user_name_error_empty));
            success = false;
        }
        if(userid.length()==0){
            mEmailIdTextInputLaout.setError(getString(R.string.user_id_error_empty));
            success = false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userid).matches()){
            mEmailIdTextInputLaout.setError(getString(R.string.user_id_error_invalid));
            success = false;
        }
        if(pass.length()<6){
            mPasswordTextInputLaout.setError(getString(R.string.password_error_length));
            success = false;
        }
        if(!pass.equals(repass)){
            mRetypePasswordTextInputLaout.setError(getString(R.string.retype_password_error_not_match));
            success = false;
        }
        return success;
    }
}
