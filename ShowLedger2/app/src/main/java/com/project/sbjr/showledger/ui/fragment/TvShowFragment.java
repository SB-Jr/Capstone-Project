package com.project.sbjr.showledger.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.adapter.ShowViewPagerAdapter;

public class TvShowFragment extends Fragment implements ShowFragment.onTvShowFragmentInteractionListener,WatchedListFragment.OnTvShowWatchedFragmentInteractionListener,WishListFragment.OnTvShowWishListFragmentInteractionListener,IncompleteListFragment.OnIncompleteFragmentInteractionListener{

    public static final String TVSHOW_TAG = "tv_show";

    private static final String USER_UID = "user_uid";

    private final static String mShowFragmentKey="com.project.sbjr.showledger.ui.fragment.MovieFragment.showfrag";
    private final static String mWatchedListFragmentKey="com.project.sbjr.showledger.ui.fragment.MovieFragment.watchlistfrag";
    private final static String mWishListFragmentKey="com.project.sbjr.showledger.ui.fragment.MovieFragment.wishlistfrag";
    private final static String mIncompleteListFragmentKey="com.project.sbjr.showledger.ui.fragment.MovieFragment.incompletelistfrag";

    private String userUid;

    private OnTvShowFragmentInteractionListener mListener;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private ShowFragment mShowFragment;
    private WatchedListFragment mWatchedListFragment;
    private WishListFragment mWishListFragment;
    private IncompleteListFragment mIncompleteListFragment;

    public TvShowFragment() {
        // Required empty public constructor
    }


    public static TvShowFragment newInstance(String param1) {
        TvShowFragment fragment = new TvShowFragment();
        Bundle args = new Bundle();
        fragment.userUid = param1;
        args.putString(USER_UID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            userUid = savedInstanceState.getString(USER_UID);
        }
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
        }
        mShowFragment = ShowFragment.newInstance(userUid,TVSHOW_TAG);
        mWishListFragment = WishListFragment.newInstance(userUid,TVSHOW_TAG);
        mWatchedListFragment = WatchedListFragment.newInstance(userUid,TVSHOW_TAG);
        mIncompleteListFragment = IncompleteListFragment.newInstance(userUid,TVSHOW_TAG);
        mShowFragment.initListener(this);
        mWatchedListFragment.initListener(this);
        mWishListFragment.initListener(this);
        mIncompleteListFragment.initListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(USER_UID,userUid);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tv_show, container, false);
        mTabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        initViewPager();
        mTabLayout.setupWithViewPager(mViewPager,false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTvShowFragmentInteractionListener) {
            mListener = (OnTvShowFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTvShowFragmentInteractionListener");
        }
    }


    public void initViewPager(){
        ShowViewPagerAdapter adapter = new ShowViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(mShowFragment,getString(R.string.show_tvshow));
        adapter.addFragment(mWishListFragment,getString(R.string.show_wish));
        adapter.addFragment(mWatchedListFragment,getString(R.string.show_watched));
        adapter.addFragment(mIncompleteListFragment,getString(R.string.show_incomplete));
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTvShowFragmentInteractionListener {
        void onTvShowSelectedInteraction(TvShowModel tvShowModel);
    }

    @Override
    public void onTvShowFragmentItemSelectListener(TvShowModel tvShowModel) {
        mListener.onTvShowSelectedInteraction(tvShowModel);
    }

    @Override
    public void onIncompleteFragmentItemClickListener(TvShowModel tvShowModel) {
        mListener.onTvShowSelectedInteraction(tvShowModel);
    }

    @Override
    public void onWatchedFragmentTvShowItemOnClickListener(TvShowModel tvShowModel) {
        mListener.onTvShowSelectedInteraction(tvShowModel);
    }

    @Override
    public void onWishListFragmentTvShowItemOnClickListener(TvShowModel tvShowModel) {
        mListener.onTvShowSelectedInteraction(tvShowModel);
    }
}
