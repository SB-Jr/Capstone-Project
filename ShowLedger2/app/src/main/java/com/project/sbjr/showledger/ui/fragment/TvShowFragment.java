package com.project.sbjr.showledger.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.adapter.ShowViewPagerAdapter;

public class TvShowFragment extends Fragment implements ShowFragment.onTvShowFragmentInteractionListener,WatchedListFragment.OnTvShowWatchedFragmentInteractionListener,WishListFragment.OnTvShowWishListFragmentInteractionListener,IncompleteListFragment.OnIncompleteFragmentInteractionListener{

    public static final String TVSHOW_TAG = "tv_show";

    private static final String USER_UID = "user_uid";

    private String userUid;

    private OnTvShowFragmentInteractionListener mListener;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

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
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    @Override
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
    }

    public void initViewPager(){
        ShowViewPagerAdapter adapter = new ShowViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ShowFragment.newInstance(userUid, TVSHOW_TAG),getString(R.string.show_tvshow));
        adapter.addFragment(WishListFragment.newInstance(userUid, TVSHOW_TAG),getString(R.string.show_wish));
        adapter.addFragment(WatchedListFragment.newInstance(userUid, TVSHOW_TAG),getString(R.string.show_watched));
        adapter.addFragment(IncompleteListFragment.newInstance(userUid,TVSHOW_TAG),getString(R.string.show_incomplete));
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
