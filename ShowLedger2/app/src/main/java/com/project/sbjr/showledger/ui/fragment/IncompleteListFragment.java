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

import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.project.sbjr.showledger.adapter.UserListTvShowAdapter;
import com.project.sbjr.showledger.provider.ProviderContract;

import java.util.ArrayList;


public class IncompleteListFragment extends Fragment implements UserListTvShowAdapter.UserListTvShowAdapterInteraction {
    private static final String USER_UID = "user_uid";
    private static final String SHOW_TYPE = "show_type";

    private String userUid;
    private String showType;

    private OnIncompleteFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private TextView mEmptyTextView;

    private Loader<Cursor> mTvLoader = null;

    public IncompleteListFragment() {
        // Required empty public constructor
    }

    public static IncompleteListFragment newInstance(String useruid, String showtype) {
        IncompleteListFragment fragment = new IncompleteListFragment();
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
        View view = inflater.inflate(R.layout.fragment_incomplete_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.contents);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text);
        mEmptyTextView = (TextView) view.findViewById(R.id.empty_text);

        if (!Util.isInternetConnected(getContext())) {
            toggleVisibility(mErrorTextView);
            return view;
        }

        toggleVisibility(mProgressBar);

        mTvLoader = getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader loader = new CursorLoader(getContext(),
                        Uri.parse(ProviderContract.CONTENT_AUTHORITY + ProviderContract.URI_MATCH_TV_INCOMPLETE),
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
                    } while (cursor.moveToNext());

                    if (tvshows.isEmpty()) {
                        toggleVisibility(mEmptyTextView);
                        return;
                    }

                    toggleVisibility(mRecyclerView);

                    UserListTvShowAdapter adapter = new UserListTvShowAdapter(getContext(), IncompleteListFragment.this, tvshows);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                } else {
                    toggleVisibility(mEmptyTextView);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });

        /*final ArrayList<Integer> tvshows = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.TVSHOW);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(Util.FireBaseConstants.INCOMPLETE)){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.TVSHOW).child(Util.FireBaseConstants.INCOMPLETE);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                int l = Integer.parseInt(snapshot.getKey());
                                tvshows.add(l);
                            }

                            if(tvshows.isEmpty()){
                                toggleVisibility(mEmptyTextView);
                                return;
                            }

                            toggleVisibility(mRecyclerView);

                            UserListTvShowAdapter adapter = new UserListTvShowAdapter(getContext(),IncompleteListFragment.this,tvshows);
                            mRecyclerView.setAdapter(adapter);
                            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    toggleVisibility(mEmptyTextView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                toggleVisibility(mErrorTextView);
            }
        });*/
        return view;
    }

    public void toggleVisibility(View v) {
        mRecyclerView.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mEmptyTextView.setVisibility(View.GONE);
        v.setVisibility(View.VISIBLE);
    }

    public void initListener(Fragment fragment) {
        if (fragment instanceof OnIncompleteFragmentInteractionListener) {
            mListener = (OnIncompleteFragmentInteractionListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement OnIncompleteFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnIncompleteFragmentInteractionListener {
        void onIncompleteFragmentItemClickListener(TvShowModel tvShowModel);
    }

    @Override
    public void userListTvShowAdapterItemOnClickListener(TvShowModel tvShowModel) {
        mListener.onIncompleteFragmentItemClickListener(tvShowModel);
    }
}
