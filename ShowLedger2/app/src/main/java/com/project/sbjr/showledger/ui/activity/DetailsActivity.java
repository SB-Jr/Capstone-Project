package com.project.sbjr.showledger.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showledger.R;

public class DetailsActivity extends AppCompatActivity {

    private MovieModel mMovieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mMovieModel = getIntent().getParcelableExtra(ShowActivity.MOVIE_NAME);
    }
}
