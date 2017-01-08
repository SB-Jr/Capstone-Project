package com.project.sbjr.showledger.adapter.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.net.Authenticator;

public class AuthenticatorService extends Service {

    private SyncAuthenticator mAuthenticator;


    public AuthenticatorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator = new SyncAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
