package com.project.sbjr.showledger.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.sbjr.showinfodatabase.HighOnShow;
import com.project.sbjr.showinfodatabase.handler.MovieHandler;
import com.project.sbjr.showinfodatabase.model.Cast;
import com.project.sbjr.showinfodatabase.model.Crew;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.response.CreditResponse;
import com.project.sbjr.showledger.R;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private MovieModel mMovieModel;

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

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mMovieModel = getIntent().getParcelableExtra(ShowActivity.MOVIE_NAME);

        mDirectorTextView = (TextView) findViewById(R.id.director);
        mMusicTextView = (TextView) findViewById(R.id.music);
        mCastTextView = (TextView) findViewById(R.id.cast);
        mProducedTextView = (TextView) findViewById(R.id.produced);
        mReleaseTextView = (TextView) findViewById(R.id.release);
        mRatingTextView = (TextView) findViewById(R.id.rating);
        mOverviewTextView = (TextView) findViewById(R.id.overview);

        mDirectorContainer = (LinearLayout) findViewById(R.id.container_director);
        mMusicContainer = (LinearLayout) findViewById(R.id.container_music);
        mCastContainer = (LinearLayout) findViewById(R.id.container_cast);
        mProducedContainer = (LinearLayout) findViewById(R.id.container_produced);
        mReleaseContainer = (LinearLayout) findViewById(R.id.container_release);
        mRatingContainer = (LinearLayout) findViewById(R.id.container_rating);
        mOverviewContainer = (LinearLayout) findViewById(R.id.container_overview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mMovieModel.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mOverviewTextView.setText(mMovieModel.getOverview());
        mReleaseTextView.setText(mMovieModel.getRelease_date());
        mRatingTextView.setText(mMovieModel.getVote_average()+"");


        HighOnShow.Movie movie = new HighOnShow(getString(R.string.api_key)).initMovie();
        movie.getMovieCredits(mMovieModel.getId(), null, null, null, new MovieHandler<CreditResponse>() {
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

                //Todo:check if the string is empty or not and if empty then change visibility of the container
                //eg:- if music string is "" then change container layout of the music part

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
}
