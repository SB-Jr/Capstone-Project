package com.project.sbjr.showledger.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.sbjr.showinfodatabase.handler.MovieHandler;
import com.project.sbjr.showinfodatabase.response.MovieResponse;
import com.project.sbjr.showledger.R;

import com.project.sbjr.showinfodatabase.HighOnShow;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showledger.adapter.ShowMovieItemAdapter;

import java.util.ArrayList;

/**
 * created by sbjr
 *
 * fragment which shows the show grid based on which fragment called it
 * */
public class ShowFragment extends Fragment implements ShowMovieItemAdapter.ShowMovieItemAdapterInteractionListener {
    private static final String USER_UID = "user_uid";
    private static final String SHOW_TYPE = "show_type";

    private String userUid;
    private String showType;

    private OnShowFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;

    public ShowFragment() {
        // Required empty public constructor
    }

    public static ShowFragment newInstance(String useruid, String showtype) {
        ShowFragment fragment = new ShowFragment();
        Bundle args = new Bundle();
        args.putString(USER_UID, useruid);
        args.putString(SHOW_TYPE, showtype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
            showType = getArguments().getString(SHOW_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_show, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.contents);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text);


        new HighOnShow(getString(R.string.api_key)).initMovie().getUpcomingMovies(mRecyclerView, mProgressBar, mErrorTextView, new MovieHandler<MovieResponse>() {
            @Override
            public void onResult(MovieResponse result) {
                ArrayList<MovieModel> movieModels = result.getResults();
                ShowMovieItemAdapter adapter = new ShowMovieItemAdapter(movieModels,ShowFragment.this);
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                //Todo: add a snackbar for failure and try again later
            }
        });
        return view;
    }


    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShowFragmentInteractionListener) {
            mListener = (OnShowFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShowFragmentInteractionListener");
        }
    }*/

    public void initListener(Fragment fragment){
        if (fragment instanceof OnShowFragmentInteractionListener) {
            mListener = (OnShowFragmentInteractionListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement OnShowFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnShowFragmentInteractionListener {
        void onMovieShowFragmentItemSelectListener(MovieModel movieModel);
    }

    @Override
    public void ShowMovieItemClickListener(MovieModel movieModel) {
        mListener.onMovieShowFragmentItemSelectListener(movieModel);
    }
}
