package com.project.sbjr.showledger.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.project.sbjr.showinfodatabase.HighOnShow;
import com.project.sbjr.showinfodatabase.handler.ShowHandler;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showinfodatabase.response.MovieResponse;
import com.project.sbjr.showinfodatabase.response.TvResponse;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.project.sbjr.showledger.adapter.ShowMovieItemAdapter;
import com.project.sbjr.showledger.adapter.ShowTvItemAdapter;
import com.project.sbjr.showledger.ui.fragment.DetailsMovieFragment;
import com.project.sbjr.showledger.ui.fragment.DetailsTvShowFragment;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements ShowMovieItemAdapter.ShowMovieItemAdapterInteractionListener, ShowTvItemAdapter.ShowTvItemAdapterInteractionListener {


    public final static String DETAILS_FRAG_TAG = "details";
    public final static String IS_MOVIE_KEY = "is_movie";
    public final static String SEARCH_LIST_KEY = "search_list_key";

    private RecyclerView mSearchListRecyclerView;
    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private FrameLayout mDetailsFrameLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private TextView mEmptyTextView;

    private ArrayList<TvShowModel> SearchListTvShows = null;
    private ArrayList<MovieModel> SearchListMovies = null;

    private boolean isTwoPane = false;
    private boolean isMovie = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchListRecyclerView = (RecyclerView) findViewById(R.id.search_list);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mErrorTextView = (TextView) findViewById(R.id.error_text);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mEmptyTextView = (TextView) findViewById(R.id.empty_text);

        AdView mAdView = (AdView) findViewById(R.id.ad_bottom);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("D26C335A1E231CBAA6BF6FCF0777F14B")
                .build();
        mAdView.loadAd(adRequest);

        mDetailsFrameLayout = (FrameLayout) findViewById(R.id.details_layout);
        if (mDetailsFrameLayout != null) {
            isTwoPane = true;
            AdView mAdViewDetails = (AdView) findViewById(R.id.ad_details);
            AdRequest adRequestDetails = new AdRequest.Builder()
                    .addTestDevice("D26C335A1E231CBAA6BF6FCF0777F14B")
                    .build();
            mAdViewDetails.loadAd(adRequestDetails);
        }


        /**
         * retaining saved instance so that searched list is retained
         * */
        if (savedInstanceState != null) {
            isMovie = savedInstanceState.getBoolean(IS_MOVIE_KEY, false);

            if (isMovie) {
                SearchListMovies = savedInstanceState.getParcelableArrayList(SEARCH_LIST_KEY);
                if (SearchListMovies != null) {
                    ShowMovieItemAdapter adapter = new ShowMovieItemAdapter(SearchListMovies, SearchActivity.this);
                    mSearchListRecyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
                    mSearchListRecyclerView.setAdapter(adapter);
                }
            } else {
                SearchListTvShows = savedInstanceState.getParcelableArrayList(SEARCH_LIST_KEY);
                if (SearchListTvShows != null) {
                    ShowTvItemAdapter adapter = new ShowTvItemAdapter(SearchListTvShows, SearchActivity.this);
                    mSearchListRecyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
                    mSearchListRecyclerView.setAdapter(adapter);
                }
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = fragmentManager.findFragmentByTag(DETAILS_FRAG_TAG);
            if (fragment != null) {
                fragmentTransaction.replace(R.id.details_layout, fragment, DETAILS_FRAG_TAG);
                fragmentTransaction.commit();
            }
        } else {
            Intent intent = getIntent();
            isMovie = intent.getBooleanExtra(ShowActivity.MOVIE_NAME, false);
        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(IS_MOVIE_KEY, isMovie);
        if (isMovie) {
            outState.putParcelableArrayList(SEARCH_LIST_KEY, SearchListMovies);
        } else {
            outState.putParcelableArrayList(SEARCH_LIST_KEY, SearchListTvShows);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void search(String query) {
        mSearchListRecyclerView.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
        if (isMovie) {
            new HighOnShow(getString(R.string.api_key)).initMovie().getMovieBySearch(query, mSearchListRecyclerView, mProgressBar, mErrorTextView, new ShowHandler<MovieResponse>() {
                @Override
                public void onResult(MovieResponse result) {
                    ArrayList<MovieModel> movieModels = result.getResults();
                    SearchListMovies = movieModels;
                    if (movieModels.isEmpty()) {
                        showEmpty();
                        return;
                    }
                    ShowMovieItemAdapter adapter = new ShowMovieItemAdapter(movieModels, SearchActivity.this);
                    mSearchListRecyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
                    mSearchListRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure() {
                    Snackbar.make(mCoordinatorLayout, getString(R.string.snack_bar_error), Snackbar.LENGTH_SHORT).show();
                }
            });
        } else {
            new HighOnShow(getString(R.string.api_key)).initTvShow().getTvShowDetailsBySearch(query, mSearchListRecyclerView, mProgressBar, mErrorTextView, new ShowHandler<TvResponse>() {
                @Override
                public void onResult(TvResponse result) {
                    ArrayList<TvShowModel> tvShowModels = result.getResults();
                    SearchListTvShows = tvShowModels;
                    if (tvShowModels.isEmpty()) {
                        showEmpty();
                        return;
                    }
                    ShowTvItemAdapter adapter = new ShowTvItemAdapter(tvShowModels, SearchActivity.this);
                    mSearchListRecyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
                    mSearchListRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure() {
                    Snackbar.make(mCoordinatorLayout, getString(R.string.snack_bar_error), Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void showEmpty() {
        mSearchListRecyclerView.setVisibility(View.GONE);
        mEmptyTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void ShowMovieItemClickListener(MovieModel movie) {
        if (isTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailsMovieFragment fragment = DetailsMovieFragment.newInstance(Util.getUserUidFromSharedPreference(this), movie);
            fragmentTransaction.replace(R.id.details_layout, fragment, DETAILS_FRAG_TAG);
            fragmentTransaction.commit();
        } else {
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra(ShowActivity.MOVIE_NAME, movie);
            startActivity(intent);
        }
    }

    @Override
    public void tvShowItemClickListener(TvShowModel tvShowModel) {
        if (isTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailsTvShowFragment fragment = DetailsTvShowFragment.newInstance(Util.getUserUidFromSharedPreference(this), tvShowModel);
            fragmentTransaction.replace(R.id.details_layout, fragment, DETAILS_FRAG_TAG);
            fragmentTransaction.commit();
        } else {
            Intent intent = new Intent(this, TvShowDetailActivity.class);
            intent.putExtra(ShowActivity.TVSHOW_NAME, tvShowModel);
            startActivity(intent);
        }
    }
}
