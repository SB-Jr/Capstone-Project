package com.project.sbjr.showledger.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.project.sbjr.showledger.ui.fragment.MovieFragment;
import com.project.sbjr.showledger.ui.fragment.NavigationDrawerFragment;
import com.project.sbjr.showledger.ui.fragment.TvShowFragment;

public class ShowActivity extends AppCompatActivity implements NavigationDrawerFragment.OnNavigationDrawerFragmentListener,MovieFragment.OnMovieFragmentInteractionListener,TvShowFragment.OnTvShowFragmentInteractionListener {

    public final static String MOVIE_NAME="com.project.sbjr.MOVIE";
    public final static String TVSHOW_NAME="com.project.sbjr.TVSHOW";


    private final String TAG = ShowActivity.class.getName();


    private final String DETAILS_FRAG_TAG = "details";
    private final String PRESENT_FRAG_TAG = "details";

    private boolean mTwoPane=false;

    private FrameLayout mDetailsFrameLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mContainerFrameLayout;

    private MovieFragment mMovieFragment;
    private TvShowFragment mTvShowFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);


        AdView mAdView = (AdView) findViewById(R.id.ad_bottom);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getString(R.string.ad_bottom))
                .build();
        mAdView.loadAd(adRequest);

        mDetailsFrameLayout = (FrameLayout) findViewById(R.id.details_layout);
        if(mDetailsFrameLayout!=null){
            mTwoPane = true;
            Log.d(TAG,"2 panes exist");
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupToolbar();
        setupNavigationBar();
        mContainerFrameLayout = (FrameLayout) findViewById(R.id.fragment_content_holder);
        mMovieFragment = MovieFragment.newInstance(Util.getUserUidFromSharedPreference(this));
        mTvShowFragment = TvShowFragment.newInstance(Util.getUserUidFromSharedPreference(this));
        changeShowFragment(mMovieFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.search){
            startActivity(new Intent(this,SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupNavigationBar(){
        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_fragment);
        mNavigationDrawerFragment.init(mDrawerLayout,mToolbar);
    }

    private void changeShowFragment(Fragment fragment){
        /**
         * using the tag we see if we are replacing any fragment with the same fragment
         * */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment presFrag = fragmentManager.findFragmentByTag(DETAILS_FRAG_TAG);
        if(presFrag!=null&&presFrag.equals(fragment)){
            fragmentTransaction.commit();
            return;
        }
        fragmentTransaction.replace(R.id.fragment_content_holder,fragment,PRESENT_FRAG_TAG);
        fragmentTransaction.commit();
        invalidateDetailsContentHolder();
    }

    private void invalidateDetailsContentHolder(){
        if(mTwoPane){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = fragmentManager.findFragmentByTag(DETAILS_FRAG_TAG);
            if(fragment!=null){
                fragmentTransaction.remove(fragment);
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onNavigationFragmentInteraction(int pos) {
        mDrawerLayout.closeDrawer(GravityCompat.START,true);
        switch (pos){
            case 0: changeShowFragment(mMovieFragment);
                break;
            case 1: changeShowFragment(mTvShowFragment);
                break;
            case 2://todo: complete this 2 cases
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onMovieSelectedInteraction(MovieModel movieModel) {
        if(mTwoPane){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //todo: add details fragment
            fragmentTransaction.replace(R.id.details_layout,null,DETAILS_FRAG_TAG);
            fragmentTransaction.commit();
        }
        else{
            Intent intent = new Intent(this,MovieDetailsActivity.class);
            //todo: add the data to the intent
            intent.putExtra(MOVIE_NAME,movieModel);
            startActivity(intent);
        }
    }

    @Override
    public void onTvShowSelectedInteraction(TvShowModel tvShowModel) {
        if(mTwoPane){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //todo: add details fragment
            fragmentTransaction.replace(R.id.details_layout,null,DETAILS_FRAG_TAG);
            fragmentTransaction.commit();
        }
        else{
            Intent intent = new Intent(this,TvShowDetailActivity.class);
            //todo: add the data to the intent
            intent.putExtra(TVSHOW_NAME,tvShowModel);
            startActivity(intent);
        }
    }
}
