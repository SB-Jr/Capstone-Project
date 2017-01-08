package com.project.sbjr.showledger.adapter.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ContentSyncService extends Service {

    private static ContentSyncAdapter sSyncAdapter = null;

    private static final Object sSyncAdapterLock = new Object();

    public ContentSyncService() {

        synchronized (sSyncAdapterLock){
            if(sSyncAdapter==null){
                sSyncAdapter = new ContentSyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
