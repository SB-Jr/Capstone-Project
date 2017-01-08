package com.project.sbjr.showledger;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SyncService extends IntentService {

    private static final String SHOW_TYPE = "com.project.sbjr.showledger.syncservice.SHOW_TYPE";
    private static final String SHOW_LIST_TYPE = "com.project.sbjr.showledger.syncservice.SHOW_LIST_TYPE";
    private static final String USER_UID = "com.project.sbjr.showledger.syncservice.USER_UID";
    private static final String SHOW_IDS = "com.project.sbjr.showledger.syncservice.SHOW_IDS";

    private static Context mContext;

    public SyncService() {
        super("SyncService");
    }

    public static void startSync(Context context, String showType, String showListType, ArrayList<Integer> showIds) {
        if(mContext==null){
            mContext=context;
        }
        Intent intent = new Intent(context, SyncService.class);
        intent.putExtra(SHOW_TYPE, showType);
        intent.putExtra(SHOW_LIST_TYPE, showListType);
        intent.putExtra(USER_UID,Util.getUserUidFromSharedPreference(context));
        intent.putExtra(SHOW_IDS,showIds);
        mContext.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String userUid = intent.getStringExtra(USER_UID);
            final String showType = intent.getStringExtra(SHOW_TYPE);
            final String showTypeList = intent.getStringExtra(SHOW_LIST_TYPE);
            final ArrayList<Integer> showIds = intent.getIntegerArrayListExtra(SHOW_IDS);
            handleActionFoo(userUid, showType,showTypeList,showIds);
        }
    }

    private void handleActionFoo(String useruid, String showType,String showListType,ArrayList<Integer> showIds) {
        for(int i:showIds){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Util.FireBaseConstants.USER).child(useruid).child(showType).child(showListType);
            reference.child(i + "").setValue(i);
        }
    }
}
