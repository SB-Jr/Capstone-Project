package com.project.sbjr.showledger.adapter.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sbjr.showledger.Util;
import com.project.sbjr.showledger.database.DatabaseContract;
import com.project.sbjr.showledger.provider.ProviderContract;
import com.project.sbjr.showledger.provider.ShowLedgerProvider;

/**
 * Created by sbjr on 8/1/17.
 */

public class ContentSyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;
    Context mContext;

    public ContentSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public ContentSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(Util.getUserIdFromSharedPreference(getContext()),Util.getUserPassFromSharedPreference(getContext()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        syncMovieWishList();
                    }
                });
    }

    public void syncMovieWishList(){
        Log.d("sync","--------------------syncing------------------------");
    }
}
