package com.project.sbjr.showledger.ui.activity;

import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.project.sbjr.showinfodatabase.model.Cast;
import com.project.sbjr.showinfodatabase.model.Crew;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.response.CreditResponse;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String MOVIE_MODEL_KEY="com.project.sbjr.showledger.ui.activity.MovieDetailsActivity.mMovieModel";

    private MovieModel mMovieModel;
    private String userUid;

    private TextView mDirectorTextView,
                     mMusicTextView,
                     mCastTextView,
                     mProducedTextView,
                     mReleaseTextView,
                     mRatingTextView,
                     mOverviewTextView;

    private LinearLayout mDirectorContainer,
                         mMusicContainer,
                         mProducedContainer,
                         mCastContainer,
                         mRatingContainer,
                         mReleaseContainer,
                         mOverviewContainer;
    private ImageView mImageView;

    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private LinearLayout mContainerLinearLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        AdView mAdView = (AdView) findViewById(R.id.ad_bottom);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("D26C335A1E231CBAA6BF6FCF0777F14B")
                .build();
        mAdView.loadAd(adRequest);


        if(savedInstanceState!=null){
            mMovieModel = savedInstanceState.getParcelable(MOVIE_MODEL_KEY);
        }
        else {
            mMovieModel = getIntent().getParcelableExtra(ShowActivity.MOVIE_NAME);
        }
        userUid = Util.getUserUidFromSharedPreference(this);

        mDirectorTextView = (TextView) findViewById(R.id.director);
        mMusicTextView = (TextView) findViewById(R.id.music);
        mCastTextView = (TextView) findViewById(R.id.cast);
        mProducedTextView = (TextView) findViewById(R.id.produced);
        mReleaseTextView = (TextView) findViewById(R.id.release);
        mRatingTextView = (TextView) findViewById(R.id.rating);
        mOverviewTextView = (TextView) findViewById(R.id.overview);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mErrorTextView = (TextView) findViewById(R.id.error_text);
        mContainerLinearLayout = (LinearLayout) findViewById(R.id.container);
        mImageView = (ImageView) findViewById(R.id.movie_image);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        mDirectorContainer = (LinearLayout) findViewById(R.id.container_director);
        mMusicContainer = (LinearLayout) findViewById(R.id.container_music);
        mCastContainer = (LinearLayout) findViewById(R.id.container_cast);
        mProducedContainer = (LinearLayout) findViewById(R.id.container_produced);
        mReleaseContainer = (LinearLayout) findViewById(R.id.container_release);
        mRatingContainer = (LinearLayout) findViewById(R.id.container_rating);
        mOverviewContainer = (LinearLayout) findViewById(R.id.container_overview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(mMovieModel.getTitle());
        mCollapsingToolbarLayout.setTitleEnabled(true);
        mCollapsingToolbarLayout.setTitle(mMovieModel.getTitle());
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);


        mOverviewTextView.setText(mMovieModel.getOverview());
        mReleaseTextView.setText(mMovieModel.getRelease_date());
        mRatingTextView.setText(mMovieModel.getVote_average()+"");


        Picasso.with(MovieDetailsActivity.this)
                .load("https://image.tmdb.org/t/p/w300"+mMovieModel.getBackdrop_path())
                .placeholder(Util.getRandomColor())
                .fit()
                .placeholder(Util.getRandomColor())
                .into(mImageView);

        HighOnShow.Movie movie = new HighOnShow(getString(R.string.api_key)).initMovie();
        movie.getMovieCredits(mMovieModel.getId(), mContainerLinearLayout, mProgressBar, mErrorTextView, new ShowHandler<CreditResponse>() {
            @Override
            public void onResult(CreditResponse result) {
                String producers="";
                String actors="";
                String music="";
                String directors="";
                for(Cast cast : result.getCast()){
                    if(cast.getOrder()<=3){
                        actors+=","+cast.getName();
                    }
                }

                for(Crew crew: result.getCrew()){
                    if(crew.getJob().equalsIgnoreCase("Director")){
                        directors+=","+crew.getName();
                    }
                    else if(crew.getJob().equalsIgnoreCase("Music")){
                        music+=","+crew.getName();
                    }
                    else if(crew.getJob().equalsIgnoreCase("Producer")){
                        producers+=","+crew.getName();
                    }
                }

                if(actors.length()==0){
                    mCastContainer.setVisibility(View.GONE);
                }
                else {
                    mCastTextView.setText(actors.substring(1));
                }
                if(directors.length()==0){
                    mDirectorContainer.setVisibility(View.GONE);
                }
                else {
                    mDirectorTextView.setText(directors.substring(1));
                }
                if(producers.length()==0){
                    mProducedContainer.setVisibility(View.GONE);
                }
                else {
                    mProducedTextView.setText(producers.substring(1));
                }
                if(music.length()==0){
                    mMusicContainer.setVisibility(View.GONE);
                }
                else {
                    mMusicTextView.setText(music.substring(1));
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_MODEL_KEY,mMovieModel);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.done:
                addMovieToWatchedList();
                break;
            case R.id.watch_later: addMovieToWatchLaterList();
                break;
            case R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addMovieToWatchedList() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.MOVIE).child(Util.FireBaseConstants.WATCHED);
        reference.child(mMovieModel.getId() + "").setValue(mMovieModel.getId(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //todo: add sncakbar for completion
            }
        });
    }

    private void addMovieToWatchLaterList(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.MOVIE).child(Util.FireBaseConstants.WISHLIST);
        reference.child(mMovieModel.getId() + "").setValue(mMovieModel.getId(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //todo: add sncakbar for completion
            }
        });
    }

}
