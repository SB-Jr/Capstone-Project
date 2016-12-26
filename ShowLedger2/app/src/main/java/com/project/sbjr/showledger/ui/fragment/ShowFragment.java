package com.project.sbjr.showledger.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.sbjr.showinfodatabase.HighOnShow;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.adapter.ShowItemAdapter;

import java.util.ArrayList;

public class ShowFragment extends Fragment implements ShowItemAdapter.ShowItemInteractionListener{
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_show, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.contents);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text);

        HighOnShow.Movie movie = new HighOnShow().initMovie();
        ArrayList<MovieModel> movieModels = movie.getUpcomingMovies(mRecyclerView,mProgressBar,mErrorTextView);

        ShowItemAdapter adapter = new ShowItemAdapter(movieModels,this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShowFragmentInteractionListener) {
            mListener = (OnShowFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNavigationDrawerFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnShowFragmentInteractionListener {
        void onShowFragmentItemSelectListener(MovieModel movieModel);
    }

    @Override
    public void ShowItemClickListener(MovieModel movieModel) {
        mListener.onShowFragmentItemSelectListener(movieModel);
    }
}
