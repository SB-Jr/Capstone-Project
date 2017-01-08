package com.project.sbjr.showledger.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.project.sbjr.showledger.adapter.ShowViewPagerAdapter;

public class MovieFragment extends Fragment implements ShowFragment.OnMovieShowFragmentInteractionListener, WatchedListFragment.OnMovieWatchedFragmentInteractionListener, WishListFragment.OnMovieWishListFragmentInteractionListener {
    public static final String MOVIE_TAG = "movie";

    private static final String USER_UID = "user_uid";


    private final static String mShowFragmentKey = "com.project.sbjr.showledger.ui.fragment.MovieFragment.showfrag";
    private final static String mWatchedListFragmentKey = "com.project.sbjr.showledger.ui.fragment.MovieFragment.watchlistfrag";
    private final static String mWishListFragmentKey = "com.project.sbjr.showledger.ui.fragment.MovieFragment.wishlistfrag";

    private String userUid;

    private OnMovieFragmentInteractionListener mListener;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private ShowFragment mShowFragment;
    private WatchedListFragment mWatchedListFragment;
    private WishListFragment mWishListFragment;

    public MovieFragment() {
        // Required empty public constructor
    }

    public static MovieFragment newInstance(String useruid) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        fragment.userUid = useruid;
        args.putString(USER_UID, useruid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            userUid = savedInstanceState.getString(USER_UID);
            /*mShowFragment = (ShowFragment) getChildFragmentManager().getFragment(savedInstanceState,mShowFragmentKey);
            mWishListFragment = (WishListFragment) getChildFragmentManager().getFragment(savedInstanceState,mWishListFragmentKey);
            mWatchedListFragment = (WatchedListFragment) getChildFragmentManager().getFragment(savedInstanceState,mWatchedListFragmentKey);*/
        }
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
        }
        if (userUid == null) {
            userUid = Util.getUserUidFromSharedPreference(getContext());
        }
        mShowFragment = ShowFragment.newInstance(userUid, MOVIE_TAG);
        mWishListFragment = WishListFragment.newInstance(userUid, MOVIE_TAG);
        mWatchedListFragment = WatchedListFragment.newInstance(userUid, MOVIE_TAG);
        mShowFragment.initListener(this);
        mWatchedListFragment.initListener(this);
        mWishListFragment.initListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(USER_UID, userUid);
        /*getChildFragmentManager().putFragment(outState,mShowFragmentKey,mShowFragment);
        getChildFragmentManager().putFragment(outState,mWishListFragmentKey,mWishListFragment);
        getChildFragmentManager().putFragment(outState,mWatchedListFragmentKey,mWatchedListFragment);*/
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        initViewPager();
        mTabLayout.setupWithViewPager(mViewPager, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMovieFragmentInteractionListener) {
            mListener = (OnMovieFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMovieFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*@Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof ShowFragment){
            ((ShowFragment) childFragment).initListener(this);
        }
        else if(childFragment instanceof WishListFragment){
            ((WishListFragment) childFragment).initListener(this);
        }
        else if(childFragment instanceof WatchedListFragment){
            ((WatchedListFragment) childFragment).initListener(this);
        }
        else if(childFragment instanceof IncompleteListFragment){
            ((IncompleteListFragment) childFragment).initListener(this);
        }
    }*/

    public void initViewPager() {
        ShowViewPagerAdapter adapter = new ShowViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(mShowFragment, getString(R.string.show_movie));
        adapter.addFragment(mWishListFragment, getString(R.string.show_wish));
        adapter.addFragment(mWatchedListFragment, getString(R.string.show_watched));
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
    }

    public interface OnMovieFragmentInteractionListener {
        void onMovieSelectedInteraction(MovieModel movieModel);
    }

    @Override
    public void onMovieShowFragmentItemSelectListener(MovieModel movieModel) {
        mListener.onMovieSelectedInteraction(movieModel);
    }

    @Override
    public void onWatchedFragmentMovieItemOnClickListener(MovieModel movieModel) {
        mListener.onMovieSelectedInteraction(movieModel);
    }

    @Override
    public void onWishListFragmentMovieItemOnClickListener(MovieModel movieModel) {
        mListener.onMovieSelectedInteraction(movieModel);
    }
}
