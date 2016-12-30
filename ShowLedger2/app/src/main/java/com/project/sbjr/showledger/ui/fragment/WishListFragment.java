package com.project.sbjr.showledger.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showledger.R;

public class WishListFragment extends Fragment {
    private static final String USER_UID = "user_uid";
    private static final String SHOW_TYPE = "show_type";

    private String userUid;
    private String showType;

    private OnMovieWishListFragmentInteractionListener mMovieListener;
    private OnTvShowWishListFragmentInteractionListener mTvListener;

    public WishListFragment() {
        // Required empty public constructor
    }


    public static WishListFragment newInstance(String useruid, String showtype) {
        WishListFragment fragment = new WishListFragment();
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
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
            showType = getArguments().getString(SHOW_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(showType.equalsIgnoreCase(MovieFragment.MOVIE_TAG)) {
            if (context instanceof OnMovieWishListFragmentInteractionListener) {
                mMovieListener = (OnMovieWishListFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnMovieWishListFragmentInteractionListener");
            }
        }
        else {
            if (context instanceof OnTvShowWishListFragmentInteractionListener) {
                mTvListener = (OnTvShowWishListFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnTvShowWishListFragmentInteractionListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMovieListener = null;
    }

    public interface OnMovieWishListFragmentInteractionListener {
        void onWishListFragmentMovieItemOnClickListener(MovieModel movieModel);
    }

    public interface OnTvShowWishListFragmentInteractionListener {
        void onWishListFragmentTvShowItemOnClickListener(TvShowModel tvShowModel);
    }
}
