package com.project.sbjr.showledger.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by sbjr on 21/12/16.
 */

public class Authentication {

    public static boolean isAuthentic(String userid,String pass){

        final boolean[] success = new boolean[1];
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(userid,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        success[0]=true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        success[0]=false;
                    }
                });

        return success[0];
    }

    public static boolean createUser(String userid,String pass){
        final boolean success[] = new boolean[1];

        FirebaseAuth auth = FirebaseAuth.getInstance();
        synchronized((Object)success[0]) {
            auth.createUserWithEmailAndPassword(userid, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            success[0] = true;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            success[0] = false;
                        }
                    });
        }
        return success[0];
    }

}
