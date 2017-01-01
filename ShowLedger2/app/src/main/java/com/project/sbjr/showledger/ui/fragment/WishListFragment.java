package com.project.sbjr.showledger.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.adapter.UserListMovieAdapter;

import java.util.ArrayList;

public class WishListFragment extends Fragment implements UserListMovieAdapter.UserListMovieAdapterInteraction{
    private static final String USER_UID = "user_uid";
    private static final String SHOW_TYPE = "show_type";

    private String userUid;
    private String showType;

    private OnMovieWishListFragmentInteractionListener mMovieListener;
    private OnTvShowWishListFragmentInteractionListener mTvListener;


    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;

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
        View view= inflater.inflate(R.layout.fragment_wish_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.contents);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text);

        if(showType.equalsIgnoreCase(MovieFragment.MOVIE_TAG)){
            final ArrayList<Integer> movies = new ArrayList<>();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("user").child(userUid).child("movies").child("watchLater");
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        long l = (long) snapshot.getValue();
                        movies.add((int)l);
                    }
                    UserListMovieAdapter adapter = new UserListMovieAdapter(getContext(),WishListFragment.this,movies);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("FireBaseError",databaseError.getMessage()+"-----"+databaseError.toString());
                }
            });
        }
        else {
            FirebaseDatabase.getInstance().getReference().child("user").child(userUid).child("tvshows").child("watchLater").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("FireBaseError",databaseError.getMessage()+"-----"+databaseError.toString());
                }
            });
        }

        return view;
    }

    public void initListener(Fragment fragment){
        if(showType.equalsIgnoreCase(MovieFragment.MOVIE_TAG)) {
            if (fragment instanceof OnMovieWishListFragmentInteractionListener) {
                mMovieListener = (OnMovieWishListFragmentInteractionListener) fragment;
            } else {
                throw new RuntimeException(fragment.toString()
                        + " must implement OnMovieWishListFragmentInteractionListener");
            }
        }
        else {
            if (fragment instanceof OnTvShowWishListFragmentInteractionListener) {
                mTvListener = (OnTvShowWishListFragmentInteractionListener) fragment;
            } else {
                throw new RuntimeException(fragment.toString()
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

    @Override
    public void userListMovieAdapterItemOnClickLstener(MovieModel movieModel) {
        mMovieListener.onWishListFragmentMovieItemOnClickListener(movieModel);
    }
}
