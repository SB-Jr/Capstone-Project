package com.project.sbjr.showledger.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.project.sbjr.showledger.adapter.ShowMovieItemAdapter;
import com.project.sbjr.showledger.adapter.ShowTvItemAdapter;

import java.util.ArrayList;

/**
 * created by sbjr
 * <p>
 * fragment which shows the show grid based on which fragment called it
 */
public class ShowFragment extends Fragment implements ShowMovieItemAdapter.ShowMovieItemAdapterInteractionListener, ShowTvItemAdapter.ShowTvItemAdapterInteractionListener {
    private static final String USER_UID = "user_uid";
    private static final String SHOW_TYPE = "show_type";

    private String userUid;
    private String showType;

    private OnMovieShowFragmentInteractionListener mMovieListener;
    private onTvShowFragmentInteractionListener mTvListener;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private FrameLayout mFrameLayout;

    public ShowFragment() {
        // Required empty public constructor
    }

    public static ShowFragment newInstance(String useruid, String showtype) {
        ShowFragment fragment = new ShowFragment();
        Bundle args = new Bundle();
        fragment.userUid = useruid;
        fragment.showType = showtype;
        args.putString(USER_UID, useruid);
        args.putString(SHOW_TYPE, showtype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            userUid = savedInstanceState.getString(USER_UID);
            showType = savedInstanceState.getString(SHOW_TYPE);
        }
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
            showType = getArguments().getString(SHOW_TYPE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(USER_UID, userUid);
        outState.putString(SHOW_TYPE, showType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.contents);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.sudo_coordinator_layout);

        if (savedInstanceState != null) {
            userUid = savedInstanceState.getString(USER_UID);
            showType = savedInstanceState.getString(SHOW_TYPE);
        }
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
            showType = getArguments().getString(SHOW_TYPE);
        }


        if (showType.equalsIgnoreCase(MovieFragment.MOVIE_TAG)) {
            new HighOnShow(getString(R.string.api_key)).initMovie().getUpcomingMovies(mRecyclerView, mProgressBar, mErrorTextView, new ShowHandler<MovieResponse>() {
                @Override
                public void onResult(MovieResponse result) {
                    ArrayList<MovieModel> movieModels = result.getResults();
                    ShowMovieItemAdapter adapter = new ShowMovieItemAdapter(movieModels, ShowFragment.this);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure() {
                    //Snackbar.make(mFrameLayout,getString(R.string.snack_bar_error),Snackbar.LENGTH_SHORT).show();
                }
            });
        } else {
            new HighOnShow(getString(R.string.api_key)).initTvShow().getTvShowOnAir(mRecyclerView, mProgressBar, mErrorTextView, new ShowHandler<TvResponse>() {
                @Override
                public void onResult(TvResponse result) {
                    ArrayList<TvShowModel> tvShowModels = result.getResults();
                    ShowTvItemAdapter adapter = new ShowTvItemAdapter(tvShowModels, ShowFragment.this);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure() {
                    //Snackbar.make(mFrameLayout,getString(R.string.snack_bar_error),Snackbar.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }

    public void initListener(Fragment fragment) {

        if (showType.equalsIgnoreCase(MovieFragment.MOVIE_TAG)) {
            if (fragment instanceof OnMovieShowFragmentInteractionListener) {
                mMovieListener = (OnMovieShowFragmentInteractionListener) fragment;
            } else {
                throw new RuntimeException(fragment.toString()
                        + " must implement OnMovieShowFragmentInteractionListener");
            }
        } else {
            if (fragment instanceof onTvShowFragmentInteractionListener) {
                mTvListener = (onTvShowFragmentInteractionListener) fragment;
            } else {
                throw new RuntimeException(fragment.toString()
                        + " must implement onTvShowFragmentInteractionListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMovieListener = null;
    }

    public interface OnMovieShowFragmentInteractionListener {
        void onMovieShowFragmentItemSelectListener(MovieModel movieModel);
    }

    public interface onTvShowFragmentInteractionListener {
        void onTvShowFragmentItemSelectListener(TvShowModel tvShowModel);
    }

    @Override
    public void ShowMovieItemClickListener(MovieModel movieModel) {
        mMovieListener.onMovieShowFragmentItemSelectListener(movieModel);
    }

    @Override
    public void tvShowItemClickListener(TvShowModel tvShowModel) {
        mTvListener.onTvShowFragmentItemSelectListener(tvShowModel);
    }
}
