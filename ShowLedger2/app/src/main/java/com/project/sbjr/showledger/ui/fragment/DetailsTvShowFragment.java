package com.project.sbjr.showledger.ui.fragment;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.sbjr.showinfodatabase.HighOnShow;
import com.project.sbjr.showinfodatabase.handler.ShowHandler;
import com.project.sbjr.showinfodatabase.model.Genre;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showinfodatabase.model.TvShowNetwork;
import com.project.sbjr.showinfodatabase.model.TvShowSeason;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.project.sbjr.showledger.database.DatabaseContract;
import com.project.sbjr.showledger.provider.ProviderContract;
import com.project.sbjr.showledger.ui.activity.MovieDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailsTvShowFragment extends Fragment {

    private static final String USER_UID = "user_uid";
    private static final String TV_SHOW_MODEL = "tv_show";

    private TvShowModel mTvShowModel;
    private String userUid;

    private TextView mCreatedByTextView,
            mGenreTextView,
            mNetworksTextView,
    /*mProducedTextView,*/
    mFirstAirDateTextView,
            mRatingTextView,
            mNumberOfSeasonsTextView,
            mOverviewTextView;

    private LinearLayout mCreatedByContainer,
            mGenreContainer,
    /*mProducedContainer,*/
    mNetworksContainer,
            mRatingContainer,
            mFirstAirDateContainer,
            mNumberOfSeaonsContainer,
            mOverviewContainer;

    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private LinearLayout mContainerLinearLayout;
    private FrameLayout mFrameLayout;
    private ImageView mImageView;

    public DetailsTvShowFragment() {
        // Required empty public constructor
    }

    public static DetailsTvShowFragment newInstance(String useruid, TvShowModel tvShowModel) {
        DetailsTvShowFragment fragment = new DetailsTvShowFragment();
        Bundle args = new Bundle();
        fragment.userUid = useruid;
        fragment.mTvShowModel = tvShowModel;
        args.putString(USER_UID, useruid);
        args.putParcelable(TV_SHOW_MODEL,tvShowModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
            mTvShowModel = getArguments().getParcelable(TV_SHOW_MODEL);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_details_tv_show, container, false);
        setHasOptionsMenu(true);


        mCreatedByTextView = (TextView) view.findViewById(R.id.created_by);
        mGenreTextView = (TextView) view.findViewById(R.id.genre);
        mNetworksTextView = (TextView) view.findViewById(R.id.network);
        /*mProducedTextView = (TextView) view.findViewById(R.id.produced);*/
        mFirstAirDateTextView = (TextView) view.findViewById(R.id.air_date);
        mRatingTextView = (TextView) view.findViewById(R.id.rating);
        mNumberOfSeasonsTextView = (TextView) view.findViewById(R.id.number_of_season);
        mOverviewTextView = (TextView) view.findViewById(R.id.overview);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text);
        mContainerLinearLayout = (LinearLayout) view.findViewById(R.id.container);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.sudo_coordinator_layout);
        mImageView = (ImageView) view.findViewById(R.id.tvshow_image);

        mCreatedByContainer = (LinearLayout) view.findViewById(R.id.container_created_by);
        mGenreContainer = (LinearLayout) view.findViewById(R.id.container_genre);
        mNetworksContainer = (LinearLayout) view.findViewById(R.id.container_network);
        /*mProducedContainer = (LinearLayout) view.findViewById(R.id.container_produced);*/
        mFirstAirDateContainer = (LinearLayout) view.findViewById(R.id.container_air_date);
        mRatingContainer = (LinearLayout) view.findViewById(R.id.container_rating);
        mNumberOfSeaonsContainer = (LinearLayout) view.findViewById(R.id.container_number_of_season);
        mOverviewContainer = (LinearLayout) view.findViewById(R.id.container_overview);


        mOverviewTextView.setText(mTvShowModel.getOverview());
        mFirstAirDateTextView.setText(mTvShowModel.getFirst_air_date());
        mRatingTextView.setText(mTvShowModel.getVote_average()+"");


        Picasso.with(getContext())
                .load("https://image.tmdb.org/t/p/w300"+mTvShowModel.getBackdrop_path())
                .fit()
                .into(mImageView);

        new HighOnShow(getString(R.string.api_key)).initTvShow().getTvShowDetailsById(mTvShowModel.getId(), mContainerLinearLayout, mProgressBar, mErrorTextView, new ShowHandler<TvShowModel>() {
            @Override
            public void onResult(TvShowModel result) {

                mTvShowModel = result;

                String genre="";
                String network="";
                String createdBy="";
                for(Genre genre1 : result.getGenres()){
                    genre+=","+genre1.getName();
                }

                for(TvShowNetwork network1 : result.getNetworks()){
                    network+=","+network1.getName();
                }

                if(genre.length()==0){
                    mGenreContainer.setVisibility(View.GONE);
                }
                else {
                    mGenreTextView.setText(genre.substring(1));
                }
                if(createdBy.length()==0){
                    mCreatedByContainer.setVisibility(View.GONE);
                }
                else {
                    mCreatedByTextView.setText(createdBy.substring(1));
                }
                /*if(producers.length()==0){
                    mProducedContainer.setVisibility(View.GONE);
                }
                else {
                    mProducedTextView.setText(producers.substring(1));
                }*/
                if(network.length()==0){
                    mNetworksContainer.setVisibility(View.GONE);
                }
                else {
                    mNetworksTextView.setText(network.substring(1));
                }

                mNumberOfSeasonsTextView.setText(result.getNumber_of_seasons()+"");
            }

            @Override
            public void onFailure() {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tv_show_detail,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done: addShowToWatchedList();
                break;
            case R.id.watch_later: addShowToWatchLaterList();
                break;
            case R.id.incomplete: addShowToIncompleteList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addShowToWatchedList(){
        /*DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.TVSHOW).child(Util.FireBaseConstants.WATCHED);
        reference.child(mTvShowModel.getId() + "").setValue(mTvShowModel.getId(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Snackbar.make(mFrameLayout,getString(R.string.snack_bar_watch_list),Snackbar.LENGTH_SHORT).show();
            }
        });*/
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TABLE_SHOW_ID,mTvShowModel.getId());
        getContext().getContentResolver().insert(Uri.parse(ProviderContract.CONTENT_AUTHORITY+ProviderContract.URI_MATCH_TV_WATCHED),values);
        Snackbar.make(mFrameLayout,getString(R.string.snack_bar_watch_list),Snackbar.LENGTH_SHORT).show();
    }

    private void addShowToWatchLaterList(){
        /*DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.TVSHOW).child(Util.FireBaseConstants.WISHLIST);
        reference.child(mTvShowModel.getId() + "").setValue(mTvShowModel.getId(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Snackbar.make(mFrameLayout,getString(R.string.snack_bar_wish_list),Snackbar.LENGTH_SHORT).show();
            }
        });*/
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TABLE_SHOW_ID,mTvShowModel.getId());
        getContext().getContentResolver().insert(Uri.parse(ProviderContract.CONTENT_AUTHORITY+ProviderContract.URI_MATCH_TV_WISH),values);
        Snackbar.make(mFrameLayout,getString(R.string.snack_bar_wish_list),Snackbar.LENGTH_SHORT).show();
    }

    private void addShowToIncompleteList(){

        buildDialog();
    }


    public void buildDialog(){
        if(mTvShowModel.getSeasons()==null){
            Snackbar.make(mFrameLayout,getString(R.string.snack_bar_wait),Snackbar.LENGTH_LONG).show();
            return;
        }
        Dialog dialog;
        ArrayList<TvShowSeason> seasons = mTvShowModel.getSeasons();
        final String[] items = new String[seasons.size()-1];
        for(int i=1;i<seasons.size();i++){
            items[i-1] =getString(R.string.season)+" "+i;
        }
        final ArrayList itemsSelected = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.detail_select_watched_seasons));
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            itemsSelected.add(selectedItemId);
                        } else if (itemsSelected.contains(selectedItemId)) {
                            itemsSelected.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.TABLE_SHOW_ID,mTvShowModel.getId());
                        getContext().getContentResolver().insert(Uri.parse(ProviderContract.CONTENT_AUTHORITY+ProviderContract.URI_MATCH_TV_INCOMPLETE),values);

                        HashMap<String, String> seasons = new HashMap<String, String>();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.TVSHOW).child(Util.FireBaseConstants.INCOMPLETE);
                        reference.child(mTvShowModel.getId() + "").setValue(itemsSelected, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Snackbar.make(mFrameLayout,getString(R.string.snack_bar_incomplete_list),Snackbar.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        dialog = builder.create();
        dialog.show();
    }
}
