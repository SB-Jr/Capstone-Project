package com.project.sbjr.showledger.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.sbjr.showledger.R;

public class TvShowFragment extends Fragment {
    private static final String USER_UID = "user_uid";

    private String userUid;

    private OnTvShowFragmentInteractionListener mListener;

    public TvShowFragment() {
        // Required empty public constructor
    }


    public static TvShowFragment newInstance(String param1) {
        TvShowFragment fragment = new TvShowFragment();
        Bundle args = new Bundle();
        args.putString(USER_UID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTvShowSelectedInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTvShowFragmentInteractionListener) {
            mListener = (OnTvShowFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNavigationDrawerFragmentListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTvShowFragmentInteractionListener {
        void onTvShowSelectedInteraction(Uri uri);
    }
}
