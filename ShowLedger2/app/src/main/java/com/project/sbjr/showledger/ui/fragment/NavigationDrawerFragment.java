package com.project.sbjr.showledger.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.project.sbjr.showledger.adapter.NavigationDrawerAdapter;

public class NavigationDrawerFragment extends Fragment implements NavigationDrawerAdapter.NavigationDrawerItemClickListener{
    private static final String CURRENT_SELECTION = "current_selection";

    private int mCurrentSelection;

    private OnNavigationDrawerFragmentListener mListener;

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private RecyclerView mRecyclerView;
    private TextView mUserGreetTextView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    public static NavigationDrawerFragment newInstance(int currentSelection) {
        NavigationDrawerFragment fragment = new NavigationDrawerFragment();
        Bundle args = new Bundle();
        fragment.mCurrentSelection = currentSelection;
        args.putInt(CURRENT_SELECTION, currentSelection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentSelection = getArguments().getInt(CURRENT_SELECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        /*NativeExpressAdView mAdView = (NativeExpressAdView) view.findViewById(R.id.ad_nav);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("D26C335A1E231CBAA6BF6FCF0777F14B")
                .build();
        mAdView.loadAd(adRequest);*/

        mRecyclerView = (RecyclerView) view.findViewById(R.id.contents);
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(getContext(),this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUserGreetTextView = (TextView) view.findViewById(R.id.user_greeting);
        mUserGreetTextView.setText(Util.getUsernameFromSharedPreference(getContext()));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



        if (context instanceof OnNavigationDrawerFragmentListener) {
            mListener = (OnNavigationDrawerFragmentListener) context;
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


    public interface OnNavigationDrawerFragmentListener {
        void onNavigationFragmentInteraction(int pos);
    }


    public void init(DrawerLayout drawerLayout, final Toolbar toolbar){
        mDrawerLayout = drawerLayout;
        mToolbar = toolbar;
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.description_navigation_drawer_open,R.string.description_navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

    }


    @Override
    public void OnNavigationDrawerItemClickListener(int pos) {
        mCurrentSelection = pos;
        mListener.onNavigationFragmentInteraction(pos);
    }
}
