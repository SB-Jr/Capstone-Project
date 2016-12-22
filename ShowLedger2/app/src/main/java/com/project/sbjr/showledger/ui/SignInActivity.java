package com.project.sbjr.showledger.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.sbjr.showledger.R;

public class SignInActivity extends AppCompatActivity {


    private EditText mUseridEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private TextInputLayout mUserIdTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        /**
         * Check if user is already used the app before and using it again
         * or whether user is new or logged out
         * */
        SharedPreferences sharedPreferences = getSharedPreferences("ShowLedger",MODE_PRIVATE);
        if(sharedPreferences.contains("user")&&sharedPreferences.getString("user","").length()>0){
            Intent intent = new Intent(SignInActivity.this,ShowActivity.class);
            startActivity(intent);
        }

        /**
         * Todo: check if internet is present and display a snackbar if not
         * */

        mUseridEditText = (EditText) findViewById(R.id.user_id);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.login);
        mRegisterButton = (Button) findViewById(R.id.sign_up);
        mUserIdTextInputLayout = (TextInputLayout) findViewById(R.id.input_layout_user_id);
        mPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.input_layout_password);
        mUserIdTextInputLayout.setErrorEnabled(true);
        mPasswordTextInputLayout.setErrorEnabled(true);


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = mUseridEditText.getText().toString();
                String pass = mPasswordEditText.getText().toString();
                if(!checkEntry()){
                    return;
                }
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(userid,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                /**
                                 * todo: check the first name of the user in the firebase database
                                 * */

                                Intent intent = new Intent(SignInActivity.this,ShowActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mUserIdTextInputLayout.setError(getString(R.string.invalid_credentials));
                            }
                        });
            }
        });
    }


    private boolean checkEntry(){
        String userid = mUseridEditText.getText().toString();
        String pass = mPasswordEditText.getText().toString();
        boolean result=true;
        if(userid.length()==0){
            mUserIdTextInputLayout.setError(getString(R.string.user_id_error_empty));
            result= false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userid).matches()){
            mUserIdTextInputLayout.setError(getString(R.string.user_id_error_empty));
            result= false;
        }
        if(pass.length()<6){
            mPasswordTextInputLayout.setError(getString(R.string.user_id_error_invalid));
            result= false;
        }
        return result;
    }
}
