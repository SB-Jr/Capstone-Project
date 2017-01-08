package com.project.sbjr.showledger.ui.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.sbjr.showinfodatabase.HighOnShow;
import com.project.sbjr.showinfodatabase.handler.ShowHandler;
import com.project.sbjr.showinfodatabase.model.Genre;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showinfodatabase.model.TvShowNetwork;
import com.project.sbjr.showinfodatabase.model.TvShowSeason;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.project.sbjr.showledger.database.DatabaseContract;
import com.project.sbjr.showledger.provider.ProviderContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class TvShowDetailActivity extends AppCompatActivity {

    private final static String TV_SHOW_MODEL_KEY="com.project.sbjr.showledger.ui.activity.TvShowDetailActivity.tvshowmodel";

    private String userUid;
    private TvShowModel mTvShowModel;

    private TextView mCreatedByTextView,
            mGenreTextView,
            mNetworksTextView,
            /*mProducedTextView,*/
            mFirstAirDateTextView,
            mRatingTextView,
            mNumberOfSeasonsTextView,
            mOverviewTextView;

    private LinearLayout mCreatedByContainer,
            mGenreContainer,
            /*mProducedContainer,*/
            mNetworksContainer,
            mRatingContainer,
            mFirstAirDateContainer,
            mNumberOfSeaonsContainer,
            mOverviewContainer;

    private ImageView mImageView;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private LinearLayout mContainerLinearLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_detail);


        if(savedInstanceState!=null){
            mTvShowModel = savedInstanceState.getParcelable(TV_SHOW_MODEL_KEY);
        }
        else {
            mTvShowModel = getIntent().getParcelableExtra(ShowActivity.TVSHOW_NAME);
        }
        userUid = Util.getUserUidFromSharedPreference(this);
        AdView mAdView = (AdView) findViewById(R.id.ad_bottom);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("D26C335A1E231CBAA6BF6FCF0777F14B")
                .build();
        mAdView.loadAd(adRequest);



        mCreatedByTextView = (TextView) findViewById(R.id.created_by);
        mGenreTextView = (TextView) findViewById(R.id.genre);
        mNetworksTextView = (TextView) findViewById(R.id.network);
        /*mProducedTextView = (TextView) findViewById(R.id.produced);*/
        mFirstAirDateTextView = (TextView) findViewById(R.id.air_date);
        mRatingTextView = (TextView) findViewById(R.id.rating);
        mNumberOfSeasonsTextView = (TextView) findViewById(R.id.number_of_season);
        mOverviewTextView = (TextView) findViewById(R.id.overview);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mErrorTextView = (TextView) findViewById(R.id.error_text);
        mContainerLinearLayout = (LinearLayout) findViewById(R.id.container);
        mImageView = (ImageView) findViewById(R.id.tvshow_image);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        mCreatedByContainer = (LinearLayout) findViewById(R.id.container_created_by);
        mGenreContainer = (LinearLayout) findViewById(R.id.container_genre);
        mNetworksContainer = (LinearLayout) findViewById(R.id.container_network);
        /*mProducedContainer = (LinearLayout) findViewById(R.id.container_produced);*/
        mFirstAirDateContainer = (LinearLayout) findViewById(R.id.container_air_date);
        mRatingContainer = (LinearLayout) findViewById(R.id.container_rating);
        mNumberOfSeaonsContainer = (LinearLayout) findViewById(R.id.container_number_of_season);
        mOverviewContainer = (LinearLayout) findViewById(R.id.container_overview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mTvShowModel.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mCollapsingToolbarLayout.setTitleEnabled(true);
        mCollapsingToolbarLayout.setTitle(mTvShowModel.getName());
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

        mOverviewTextView.setText(mTvShowModel.getOverview());
        mFirstAirDateTextView.setText(mTvShowModel.getFirst_air_date());
        mRatingTextView.setText(mTvShowModel.getVote_average()+"");

        Picasso.with(TvShowDetailActivity.this)
                .load("https://image.tmdb.org/t/p/w300"+mTvShowModel.getBackdrop_path())
                .fit()
                .into(mImageView);

        new HighOnShow(getString(R.string.api_key)).initTvShow().getTvShowDetailsById(mTvShowModel.getId(), mContainerLinearLayout, mProgressBar, mErrorTextView, new ShowHandler<TvShowModel>() {
            @Override
            public void onResult(TvShowModel result) {

                mTvShowModel = result;

                String genre="";
                String network="";
                String createdBy="";
                for(Genre genre1 : result.getGenres()){
                    genre+=","+genre1.getName();
                }

                for(TvShowNetwork network1 : result.getNetworks()){
                    network+=","+network1.getName();
                }

                if(genre.length()==0){
                    mGenreContainer.setVisibility(View.GONE);
                }
                else {
                    mGenreTextView.setText(genre.substring(1));
                }
                if(createdBy.length()==0){
                    mCreatedByContainer.setVisibility(View.GONE);
                }
                else {
                    mCreatedByTextView.setText(createdBy.substring(1));
                }
                /*if(producers.length()==0){
                    mProducedContainer.setVisibility(View.GONE);
                }
                else {
                    mProducedTextView.setText(producers.substring(1));
                }*/
                if(network.length()==0){
                    mNetworksContainer.setVisibility(View.GONE);
                }
                else {
                    mNetworksTextView.setText(network.substring(1));
                }

                mNumberOfSeasonsTextView.setText(result.getNumber_of_seasons()+"");
            }

            @Override
            public void onFailure() {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tv_show_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done: addShowToWatchedList();
                break;
            case R.id.watch_later: addShowToWatchLaterList();
                break;
            case R.id.incomplete: addShowToIncompleteList();
                break;
            case R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(TV_SHOW_MODEL_KEY,mTvShowModel);

        super.onSaveInstanceState(outState);
    }

    private void addShowToWatchedList(){
       /* DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.TVSHOW).child(Util.FireBaseConstants.WATCHED);
        reference.child(mTvShowModel.getId() + "").setValue(mTvShowModel.getId(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Snackbar.make(mCoordinatorLayout,getString(R.string.snack_bar_watch_list),Snackbar.LENGTH_SHORT).show();
            }
        });*/
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TABLE_SHOW_ID,mTvShowModel.getId());
        getContentResolver().insert(Uri.parse(ProviderContract.CONTENT_AUTHORITY+ProviderContract.URI_MATCH_TV_WATCHED),values);
        Snackbar.make(mCoordinatorLayout,getString(R.string.snack_bar_watch_list),Snackbar.LENGTH_SHORT).show();
    }

    private void addShowToWatchLaterList(){
        /*DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.TVSHOW).child(Util.FireBaseConstants.WISHLIST);
        reference.child(mTvShowModel.getId() + "").setValue(mTvShowModel.getId(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Snackbar.make(mCoordinatorLayout,getString(R.string.snack_bar_wish_list),Snackbar.LENGTH_SHORT).show();
            }
        });*/
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TABLE_SHOW_ID,mTvShowModel.getId());
        getContentResolver().insert(Uri.parse(ProviderContract.CONTENT_AUTHORITY+ProviderContract.URI_MATCH_TV_WISH),values);
        Snackbar.make(mCoordinatorLayout,getString(R.string.snack_bar_wish_list),Snackbar.LENGTH_SHORT).show();
    }

    private void addShowToIncompleteList(){
        buildDialog();
    }


    public void buildDialog(){
        if(mTvShowModel.getSeasons()==null){
            Snackbar.make(mCoordinatorLayout,getString(R.string.snack_bar_wait),Snackbar.LENGTH_LONG).show();
            return;
        }
        Dialog dialog;
        ArrayList<TvShowSeason> seasons = mTvShowModel.getSeasons();
        final String[] items = new String[seasons.size()-1];
        for(int i=1;i<seasons.size();i++){
            items[i-1] = getString(R.string.season)+" "+i;
        }
        final ArrayList itemsSelected = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.detail_select_watched_seasons));
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            itemsSelected.add(selectedItemId);
                        } else if (itemsSelected.contains(selectedItemId)) {
                            itemsSelected.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.TABLE_SHOW_ID,mTvShowModel.getId());
                        getContentResolver().insert(Uri.parse(ProviderContract.CONTENT_AUTHORITY+ProviderContract.URI_MATCH_TV_INCOMPLETE),values);

                        HashMap<String, String> seasons = new HashMap<String, String>();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.TVSHOW).child(Util.FireBaseConstants.INCOMPLETE);
                        reference.child(mTvShowModel.getId() + "").setValue(itemsSelected, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Snackbar.make(mCoordinatorLayout,getString(R.string.snack_bar_incomplete_list),Snackbar.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        dialog = builder.create();
        dialog.show();
    }
}
