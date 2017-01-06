package com.project.sbjr.showledger.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.project.sbjr.showledger.adapter.UserListMovieAdapter;
import com.project.sbjr.showledger.adapter.UserListTvShowAdapter;
import com.project.sbjr.showledger.ui.activity.ShowActivity;

import java.util.ArrayList;


public class WatchedListFragment extends Fragment implements UserListMovieAdapter.UserListMovieAdapterInteraction,UserListTvShowAdapter.UserListTvShowAdapterInteraction{
    private static final String USER_UID = "user_uid";
    private static final String SHOW_TYPE = "show_type";

    private String userUid;
    private String showType;

    private OnMovieWatchedFragmentInteractionListener mMovieListener;
    private OnTvShowWatchedFragmentInteractionListener mTvListener;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;

    public WatchedListFragment() {
        // Required empty public constructor
    }

    public static WatchedListFragment newInstance(String useruid, String showtype) {
        WatchedListFragment fragment = new WatchedListFragment();
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
        if(savedInstanceState!=null){
            userUid = savedInstanceState.getString(USER_UID);
            showType = savedInstanceState.getString(SHOW_TYPE);
        }
        else if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
            showType = getArguments().getString(SHOW_TYPE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(USER_UID,userUid);
        outState.putString(SHOW_TYPE,showType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watched_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.contents);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text);

        if(showType.equalsIgnoreCase(MovieFragment.MOVIE_TAG)){
            final ArrayList<Integer> movies = new ArrayList<>();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.MOVIE).child(Util.FireBaseConstants.WATCHED);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        long l = (long) snapshot.getValue();
                        movies.add((int)l);
                    }
                    UserListMovieAdapter adapter = new UserListMovieAdapter(getContext(),WatchedListFragment.this,movies);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            final ArrayList<Integer> tvshows = new ArrayList<>();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.TVSHOW).child(Util.FireBaseConstants.WATCHED);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        long l = (long) snapshot.getValue();
                        tvshows.add((int)l);
                    }
                    UserListTvShowAdapter adapter = new UserListTvShowAdapter(getContext(),WatchedListFragment.this,tvshows);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        return view;
    }

    public void initListener(Fragment fragment){
        if(showType.equalsIgnoreCase(MovieFragment.MOVIE_TAG)) {
            if (fragment instanceof OnMovieWatchedFragmentInteractionListener) {
                mMovieListener = (OnMovieWatchedFragmentInteractionListener) fragment;
            } else {
                throw new RuntimeException(fragment.toString()
                        + " must implement OnMovieWatchedFragmentInteractionListener");
            }
        }
        else {
            if (fragment instanceof OnTvShowWatchedFragmentInteractionListener) {
                mTvListener = (OnTvShowWatchedFragmentInteractionListener) fragment;
            } else {
                throw new RuntimeException(fragment.toString()
                        + " must implement OnTvShowWatchedFragmentInteractionListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMovieListener = null;
    }

    public interface OnMovieWatchedFragmentInteractionListener {
        void onWatchedFragmentMovieItemOnClickListener(MovieModel movieModel);
    }

    public interface OnTvShowWatchedFragmentInteractionListener {
        void onWatchedFragmentTvShowItemOnClickListener(TvShowModel tvShowModel);
    }

    @Override
    public void userListMovieAdapterItemOnClickListener(MovieModel movieModel) {
        mMovieListener.onWatchedFragmentMovieItemOnClickListener(movieModel);
    }

    @Override
    public void userListTvShowAdapterItemOnClickListener(TvShowModel tvShowModel) {
        mTvListener.onWatchedFragmentTvShowItemOnClickListener(tvShowModel);
    }
}
