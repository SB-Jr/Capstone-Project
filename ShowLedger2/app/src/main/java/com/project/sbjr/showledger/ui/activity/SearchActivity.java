package com.project.sbjr.showledger.ui.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private RecyclerView mSearchListRecyclerView;
    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private FrameLayout mDetailsFrameLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private TextView mEmptyTextView;

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

        mDetailsFrameLayout = (FrameLayout) findViewById(R.id.details_layout);
        if(mDetailsFrameLayout!=null){
            isTwoPane = true;
        }

        Intent intent = getIntent();
        isMovie = intent.getBooleanExtra(ShowActivity.MOVIE_NAME, false);

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
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void search(String query){
        mSearchListRecyclerView.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
        if(isMovie){
            new HighOnShow(getString(R.string.api_key)).initMovie().getMovieBySearch(query, mSearchListRecyclerView, mProgressBar, mErrorTextView, new ShowHandler<MovieResponse>() {
                @Override
                public void onResult(MovieResponse result) {
                    ArrayList<MovieModel> movieModels = result.getResults();
                    if(movieModels.isEmpty()){
                        showEmpty();
                        return;
                    }
                    ShowMovieItemAdapter adapter = new ShowMovieItemAdapter(movieModels, SearchActivity.this);
                    mSearchListRecyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
                    mSearchListRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure() {
                    Snackbar.make(mCoordinatorLayout,getString(R.string.snack_bar_error),Snackbar.LENGTH_SHORT).show();
                }
            });
        }
        else{
            new HighOnShow(getString(R.string.api_key)).initTvShow().getTvShowDetailsBySearch(query, mSearchListRecyclerView, mProgressBar, mErrorTextView, new ShowHandler<TvResponse>() {
                @Override
                public void onResult(TvResponse result) {
                    ArrayList<TvShowModel> tvShowModels = result.getResults();
                    ShowTvItemAdapter adapter = new ShowTvItemAdapter(tvShowModels,SearchActivity.this);
                    mSearchListRecyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this,2));
                    mSearchListRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure() {
                    Snackbar.make(mCoordinatorLayout,getString(R.string.snack_bar_error),Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void showEmpty(){
        mSearchListRecyclerView.setVisibility(View.GONE);
        mEmptyTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void ShowMovieItemClickListener(MovieModel movie) {
        if(isTwoPane){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailsMovieFragment fragment = DetailsMovieFragment.newInstance(Util.getUserUidFromSharedPreference(this),movie);
            fragmentTransaction.replace(R.id.details_layout,fragment,ShowActivity.DETAILS_FRAG_TAG);
            fragmentTransaction.commit();
        }
        else{
            Intent intent = new Intent(this,MovieDetailsActivity.class);
            intent.putExtra(ShowActivity.MOVIE_NAME,movie);
            startActivity(intent);
        }
    }

    @Override
    public void tvShowItemClickListener(TvShowModel tvShowModel) {
        if(isTwoPane){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailsTvShowFragment fragment = DetailsTvShowFragment.newInstance(Util.getUserUidFromSharedPreference(this),tvShowModel);
            fragmentTransaction.replace(R.id.details_layout,fragment,ShowActivity.DETAILS_FRAG_TAG);
            fragmentTransaction.commit();
        }
        else{
            Intent intent = new Intent(this,TvShowDetailActivity.class);
            intent.putExtra(ShowActivity.TVSHOW_NAME,tvShowModel);
            startActivity(intent);
        }
    }
}
