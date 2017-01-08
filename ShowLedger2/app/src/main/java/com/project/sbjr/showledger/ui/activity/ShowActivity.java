package com.project.sbjr.showledger.ui.activity;

import android.accounts.Account;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
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
import com.project.sbjr.showledger.provider.ProviderContract;
import com.project.sbjr.showledger.ui.fragment.DetailsMovieFragment;
import com.project.sbjr.showledger.ui.fragment.DetailsTvShowFragment;
import com.project.sbjr.showledger.ui.fragment.MovieFragment;
import com.project.sbjr.showledger.ui.fragment.NavigationDrawerFragment;
import com.project.sbjr.showledger.ui.fragment.TvShowFragment;

public class ShowActivity extends AppCompatActivity implements NavigationDrawerFragment.OnNavigationDrawerFragmentListener,MovieFragment.OnMovieFragmentInteractionListener,TvShowFragment.OnTvShowFragmentInteractionListener {

    public final static String MOVIE_NAME="com.project.sbjr.MOVIE";
    public final static String TVSHOW_NAME="com.project.sbjr.TVSHOW";


    private final String TAG = ShowActivity.class.getName();


    public final static String DETAILS_FRAG_TAG = "details";
    public final static String PRESENT_FRAG_TAG = "present_frag";

    private final static String MISMOVIEKEY="com.project.sbjr.ShowActivity.mismovie";
    private static  final String mMovieFragmentKey="com.project.sbjr.showledger.ui.activity.ShowActivity.moviefragment";
    private static  final String mTvShowFragmentKey="com.project.sbjr.showledger.ui.activity.ShowActivity.tvshowfragment";


    private boolean mTwoPane=false;
    private boolean mIsMovie=true;

    private FrameLayout mDetailsFrameLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mContainerFrameLayout;

    private MovieFragment mMovieFragment;
    private TvShowFragment mTvShowFragment;


    private final static int INTERVAL_SYNC_ADAPTER=60;//seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);


        /**
         * sync adapter
         * */
        Account account = new Account("example",getString(R.string.auth_type));
        /*Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_INITIALIZE,true);*/
        ContentResolver.setIsSyncable(account,ProviderContract.AUTHORITY,1);
        ContentResolver.requestSync(account, ProviderContract.AUTHORITY,Bundle.EMPTY);
        //ContentResolver.addPeriodicSync(account, ProviderContract.AUTHORITY,Bundle.EMPTY,INTERVAL_SYNC_ADAPTER);


        AdView mAdView = (AdView) findViewById(R.id.ad_bottom);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("D26C335A1E231CBAA6BF6FCF0777F14B")
                .build();
        mAdView.loadAd(adRequest);

        mDetailsFrameLayout = (FrameLayout) findViewById(R.id.details_layout);
        if(mDetailsFrameLayout!=null){
            mTwoPane = true;
            Log.d(TAG,"2 panes exist");

            AdView mAdViewDetails = (AdView) findViewById(R.id.ad_details);
            AdRequest adRequestDetails = new AdRequest.Builder()
                    .addTestDevice("D26C335A1E231CBAA6BF6FCF0777F14B")
                    .build();
            mAdViewDetails.loadAd(adRequestDetails);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupToolbar();
        setupNavigationBar();
        mContainerFrameLayout = (FrameLayout) findViewById(R.id.fragment_content_holder);


        mMovieFragment = MovieFragment.newInstance(Util.getUserUidFromSharedPreference(this));
        mTvShowFragment = TvShowFragment.newInstance(Util.getUserUidFromSharedPreference(this));

        if(savedInstanceState!=null){
            if(savedInstanceState.getBoolean(MISMOVIEKEY,true)) {
                mIsMovie = true;
                changeShowFragment(mMovieFragment);
            }else{
                mIsMovie = false;
                changeShowFragment(mTvShowFragment);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = fragmentManager.findFragmentByTag(DETAILS_FRAG_TAG);
            if(fragment!=null) {
                fragmentTransaction.replace(R.id.details_layout, fragment, DETAILS_FRAG_TAG);
                fragmentTransaction.commit();
            }

        }
        else {
            mIsMovie = true;
            changeShowFragment(mMovieFragment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(MISMOVIEKEY,mIsMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_activity,menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        if(searchView!=null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.search){
            Intent intent = new Intent(this,SearchActivity.class);
            intent.putExtra(MOVIE_NAME, mIsMovie);
            startActivity(intent);
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
                fragmentTransaction.commit();
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onNavigationFragmentInteraction(int pos) {
        mDrawerLayout.closeDrawer(GravityCompat.START,true);
        switch (pos){
            case 0: changeShowFragment(mMovieFragment);
                mIsMovie = true;
                break;
            case 1: changeShowFragment(mTvShowFragment);
                mIsMovie = false;
                break;
            case 2:Util.clearCredentials(this);
                Intent intent = new Intent(this,SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onMovieSelectedInteraction(MovieModel movieModel) {
        if(mTwoPane){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailsMovieFragment fragment = DetailsMovieFragment.newInstance(Util.getUserUidFromSharedPreference(this),movieModel);
            fragmentTransaction.replace(R.id.details_layout,fragment,DETAILS_FRAG_TAG);
            fragmentTransaction.commit();
        }
        else{
            Intent intent = new Intent(this,MovieDetailsActivity.class);
            intent.putExtra(MOVIE_NAME,movieModel);
            startActivity(intent);
        }
    }

    @Override
    public void onTvShowSelectedInteraction(TvShowModel tvShowModel) {
        if(mTwoPane){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailsTvShowFragment fragment = DetailsTvShowFragment.newInstance(Util.getUserUidFromSharedPreference(this),tvShowModel);
            fragmentTransaction.replace(R.id.details_layout,fragment,DETAILS_FRAG_TAG);
            fragmentTransaction.commit();
        }
        else{
            Intent intent = new Intent(this,TvShowDetailActivity.class);
            intent.putExtra(TVSHOW_NAME,tvShowModel);
            startActivity(intent);
        }
    }
}
