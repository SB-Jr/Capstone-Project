package com.project.sbjr.showledger.ui.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.service.SyncService;
import com.project.sbjr.showledger.Util;
import com.project.sbjr.showledger.adapter.UserListMovieAdapter;
import com.project.sbjr.showledger.adapter.UserListTvShowAdapter;
import com.project.sbjr.showledger.provider.ProviderContract;

import java.util.ArrayList;

public class WishListFragment extends Fragment implements UserListMovieAdapter.UserListMovieAdapterInteraction, UserListTvShowAdapter.UserListTvShowAdapterInteraction {
    private static final String USER_UID = "user_uid";
    private static final String SHOW_TYPE = "show_type";

    private String userUid;
    private String showType;

    private OnMovieWishListFragmentInteractionListener mMovieListener;
    private OnTvShowWishListFragmentInteractionListener mTvListener;


    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private TextView mEmptyTextView;

    private Loader<Cursor> mMovieLoader =null;
    private Loader<Cursor> mTvLoader =null;

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
        if (savedInstanceState != null) {
            userUid = savedInstanceState.getString(USER_UID);
            showType = savedInstanceState.getString(SHOW_TYPE);
        } else if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
            showType = getArguments().getString(SHOW_TYPE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(USER_UID, userUid);
        outState.putString(SHOW_TYPE, showType);
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.contents);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text);
        mEmptyTextView = (TextView) view.findViewById(R.id.empty_text);

        if(!Util.isInternetConnected(getContext())){
            toggleVisibility(mErrorTextView);
            return view;
        }

        if (showType.equalsIgnoreCase(MovieFragment.MOVIE_TAG)) {

            toggleVisibility(mProgressBar);

            mMovieLoader = getLoaderManager().initLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    CursorLoader loader = new CursorLoader(getContext(),
                            Uri.parse(ProviderContract.CONTENT_AUTHORITY +ProviderContract.URI_MATCH_MOVIE_WISH),
                            null,
                            null,
                            null,
                            null);

                    return loader;
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    if (cursor != null && cursor.getCount() > 0) {
                        ArrayList<Integer> movies = new ArrayList<>();

                        cursor.moveToFirst();
                        do {
                            movies.add(cursor.getInt(0));
                        }while (cursor.moveToNext());

                        if(movies.isEmpty()){
                            toggleVisibility(mEmptyTextView);
                            return;
                        }

                        SyncService.startSync(getContext(),Util.FireBaseConstants.MOVIE,Util.FireBaseConstants.WISHLIST,movies);

                        toggleVisibility(mRecyclerView);

                        UserListMovieAdapter adapter = new UserListMovieAdapter(getContext(),WishListFragment.this,movies);
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                    }else{
                        toggleVisibility(mEmptyTextView);
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {

                }
            });

            /*DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.MOVIE);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(Util.FireBaseConstants.WISHLIST)) {
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.MOVIE).child(Util.FireBaseConstants.WISHLIST);
                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    long l = (long) snapshot.getValue();
                                    movies.add((int) l);
                                }
                                if (movies.isEmpty()) {
                                    toggleVisibility(mEmptyTextView);
                                    return;
                                }

                                toggleVisibility(mRecyclerView);

                                UserListMovieAdapter adapter = new UserListMovieAdapter(getContext(), WishListFragment.this, movies);
                                mRecyclerView.setAdapter(adapter);
                                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("FireBaseError", databaseError.getMessage() + "-----" + databaseError.toString());
                            }
                        });
                    } else {
                        toggleVisibility(mEmptyTextView);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    toggleVisibility(mErrorTextView);
                }
            });*/
        } else {

            toggleVisibility(mProgressBar);

            mTvLoader = getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    CursorLoader loader = new CursorLoader(getContext(),
                            Uri.parse(ProviderContract.CONTENT_AUTHORITY +ProviderContract.URI_MATCH_TV_WISH),
                            null,
                            null,
                            null,
                            null);

                    return loader;
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    if (cursor != null && cursor.getCount() > 0) {
                        ArrayList<Integer> tvshows = new ArrayList<>();

                        cursor.moveToFirst();
                        do {
                            tvshows.add(cursor.getInt(0));
                        }while (cursor.moveToNext());

                        if(tvshows.isEmpty()){
                            toggleVisibility(mEmptyTextView);
                            return;
                        }

                        SyncService.startSync(getContext(),Util.FireBaseConstants.TVSHOW,Util.FireBaseConstants.WISHLIST,tvshows);

                        toggleVisibility(mRecyclerView);

                        UserListTvShowAdapter adapter = new UserListTvShowAdapter(getContext(),WishListFragment.this,tvshows);
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                    }else{
                        toggleVisibility(mEmptyTextView);
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {

                }
            });

            /*final ArrayList<Integer> tvshows = new ArrayList<>();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.TVSHOW);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(Util.FireBaseConstants.WISHLIST)) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.TVSHOW).child(Util.FireBaseConstants.WISHLIST);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    long l = (long) snapshot.getValue();
                                    tvshows.add((int) l);
                                }
                                if (tvshows.isEmpty()) {
                                    toggleVisibility(mEmptyTextView);
                                    return;
                                }

                                toggleVisibility(mRecyclerView);

                                UserListTvShowAdapter adapter = new UserListTvShowAdapter(getContext(), WishListFragment.this, tvshows);
                                mRecyclerView.setAdapter(adapter);
                                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("FireBaseError", databaseError.getMessage() + "-----" + databaseError.toString());
                            }
                        });

                    } else {
                        toggleVisibility(mEmptyTextView);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    toggleVisibility(mErrorTextView);
                }
            });*/
        }

        return view;
    }

    public void initListener(Fragment fragment) {
        if (showType.equalsIgnoreCase(MovieFragment.MOVIE_TAG)) {
            if (fragment instanceof OnMovieWishListFragmentInteractionListener) {
                mMovieListener = (OnMovieWishListFragmentInteractionListener) fragment;
            } else {
                throw new RuntimeException(fragment.toString()
                        + " must implement OnMovieWishListFragmentInteractionListener");
            }
        } else {
            if (fragment instanceof OnTvShowWishListFragmentInteractionListener) {
                mTvListener = (OnTvShowWishListFragmentInteractionListener) fragment;
            } else {
                throw new RuntimeException(fragment.toString()
                        + " must implement OnTvShowWishListFragmentInteractionListener");
            }
        }
    }

    public void toggleVisibility(View v){
        mRecyclerView.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mEmptyTextView.setVisibility(View.GONE);
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMovieListener = null;
        mTvListener = null;
    }

    public interface OnMovieWishListFragmentInteractionListener {
        void onWishListFragmentMovieItemOnClickListener(MovieModel movieModel);
    }

    public interface OnTvShowWishListFragmentInteractionListener {
        void onWishListFragmentTvShowItemOnClickListener(TvShowModel tvShowModel);
    }

    @Override
    public void userListMovieAdapterItemOnClickListener(MovieModel movieModel) {
        mMovieListener.onWishListFragmentMovieItemOnClickListener(movieModel);
    }

    @Override
    public void userListTvShowAdapterItemOnClickListener(TvShowModel tvShowModel) {
        mTvListener.onWishListFragmentTvShowItemOnClickListener(tvShowModel);
    }
}
