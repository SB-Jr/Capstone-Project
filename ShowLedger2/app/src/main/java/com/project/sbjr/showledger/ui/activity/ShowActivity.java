package com.project.sbjr.showledger.ui.activity;

import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.ui.fragment.NavigationDrawerFragment;

public class ShowActivity extends AppCompatActivity implements NavigationDrawerFragment.OnFragmentInteractionListener {

    private final String TAG = ShowActivity.class.getName();

    private boolean mTwoPane=false;

    private FrameLayout mDetailsFrameLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mDetailsFrameLayout = (FrameLayout) findViewById(R.id.details_layout);
        if(mDetailsFrameLayout!=null){
            mTwoPane = true;
            Log.d(TAG,"2 panes exist");
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupToolbar();
        setupNavigationBar();
    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupNavigationBar(){
        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_fragment);
        mNavigationDrawerFragment.init(mDrawerLayout,mToolbar);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
