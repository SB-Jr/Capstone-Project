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
import com.project.sbjr.showledger.adapter.ShowViewPagerAdapter;

public class MovieFragment extends Fragment implements ShowFragment.OnShowFragmentInteractionListener{
    public static final String MOVIE_TAG = "movie";

    private static final String USER_UID = "user_uid";

    private String userUid;

    private OnMovieFragmentInteractionListener mListener;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public MovieFragment() {
        // Required empty public constructor
    }

    public static MovieFragment newInstance(String useruid) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString(USER_UID, useruid);
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
        View view =  inflater.inflate(R.layout.fragment_movie, container, false);
        mTabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        initViewPager();
        mTabLayout.setupWithViewPager(mViewPager,false);
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

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof ShowFragment){
            ((ShowFragment) childFragment).initListener(this);
        }
    }

    public void initViewPager(){
        ShowViewPagerAdapter adapter = new ShowViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ShowFragment.newInstance(userUid,MOVIE_TAG),getString(R.string.show_movie));
        adapter.addFragment(WishListFragment.newInstance(userUid,MOVIE_TAG),getString(R.string.show_wish));
        adapter.addFragment(WatchedListFragment.newInstance(userUid,MOVIE_TAG),getString(R.string.show_watched));
        mViewPager.setAdapter(adapter);
    }

    public interface OnMovieFragmentInteractionListener {
        void onMovieSelectedInteraction(MovieModel movieModel);
    }

    @Override
    public void onMovieShowFragmentItemSelectListener(MovieModel movieModel) {
        mListener.onMovieSelectedInteraction(movieModel);
    }
}
