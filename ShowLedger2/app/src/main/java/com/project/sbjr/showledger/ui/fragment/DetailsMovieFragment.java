package com.project.sbjr.showledger.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.sbjr.showinfodatabase.HighOnShow;
import com.project.sbjr.showinfodatabase.handler.ShowHandler;
import com.project.sbjr.showinfodatabase.model.Cast;
import com.project.sbjr.showinfodatabase.model.Crew;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.response.CreditResponse;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;


public class DetailsMovieFragment extends Fragment {


    private static final String USER_UID = "user_uid";
    private static final String MOVIE_MODEL = "movie_model";

    private MovieModel mMovieModel;
    private String userUid;

    private TextView mDirectorTextView,
            mMusicTextView,
            mCastTextView,
            mProducedTextView,
            mReleaseTextView,
            mRatingTextView,
            mOverviewTextView;

    private LinearLayout mDirectorContainer,
            mMusicContainer,
            mProducedContainer,
            mCastContainer,
            mRatingContainer,
            mReleaseContainer,
            mOverviewContainer;

    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private LinearLayout mContainerLinearLayout;

    public DetailsMovieFragment() {
        // Required empty public constructor
    }

    public static DetailsMovieFragment newInstance(String useruid,MovieModel movieModel) {
        DetailsMovieFragment fragment = new DetailsMovieFragment();
        Bundle args = new Bundle();
        fragment.userUid = useruid;
        fragment.mMovieModel = movieModel;
        args.putString(USER_UID, useruid);
        args.putParcelable(MOVIE_MODEL,movieModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            mMovieModel = savedInstanceState.getParcelable(MOVIE_MODEL);
            userUid = savedInstanceState.getString(USER_UID);
        }
        else if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
            mMovieModel = getArguments().getParcelable(MOVIE_MODEL);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_detail,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done:
                addMovieToWatchedList();
                break;
            case R.id.watch_later: addMovieToWatchLaterList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        mDirectorTextView = (TextView) view.findViewById(R.id.director);
        mMusicTextView = (TextView) view.findViewById(R.id.music);
        mCastTextView = (TextView) view.findViewById(R.id.cast);
        mProducedTextView = (TextView) view.findViewById(R.id.produced);
        mReleaseTextView = (TextView) view.findViewById(R.id.release);
        mRatingTextView = (TextView) view.findViewById(R.id.rating);
        mOverviewTextView = (TextView) view.findViewById(R.id.overview);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text);
        mContainerLinearLayout = (LinearLayout) view.findViewById(R.id.container);

        mDirectorContainer = (LinearLayout) view.findViewById(R.id.container_director);
        mMusicContainer = (LinearLayout) view.findViewById(R.id.container_music);
        mCastContainer = (LinearLayout) view.findViewById(R.id.container_cast);
        mProducedContainer = (LinearLayout) view.findViewById(R.id.container_produced);
        mReleaseContainer = (LinearLayout) view.findViewById(R.id.container_release);
        mRatingContainer = (LinearLayout) view.findViewById(R.id.container_rating);
        mOverviewContainer = (LinearLayout) view.findViewById(R.id.container_overview);

        mOverviewTextView.setText(mMovieModel.getOverview());
        mReleaseTextView.setText(mMovieModel.getRelease_date());
        mRatingTextView.setText(mMovieModel.getVote_average()+"");

        HighOnShow.Movie movie = new HighOnShow(getString(R.string.api_key)).initMovie();
        movie.getMovieCredits(mMovieModel.getId(), mContainerLinearLayout, mProgressBar, mErrorTextView, new ShowHandler<CreditResponse>() {
            @Override
            public void onResult(CreditResponse result) {
                String producers="";
                String actors="";
                String music="";
                String directors="";
                for(Cast cast : result.getCast()){
                    if(cast.getOrder()<=3){
                        actors+=","+cast.getName();
                    }
                }

                for(Crew crew: result.getCrew()){
                    if(crew.getJob().equalsIgnoreCase("Director")){
                        directors+=","+crew.getName();
                    }
                    else if(crew.getJob().equalsIgnoreCase("Music")){
                        music+=","+crew.getName();
                    }
                    else if(crew.getJob().equalsIgnoreCase("Producer")){
                        producers+=","+crew.getName();
                    }
                }

                if(actors.length()==0){
                    mCastContainer.setVisibility(View.GONE);
                }
                else {
                    mCastTextView.setText(actors.substring(1));
                }
                if(directors.length()==0){
                    mDirectorContainer.setVisibility(View.GONE);
                }
                else {
                    mDirectorTextView.setText(directors.substring(1));
                }
                if(producers.length()==0){
                    mProducedContainer.setVisibility(View.GONE);
                }
                else {
                    mProducedTextView.setText(producers.substring(1));
                }
                if(music.length()==0){
                    mMusicContainer.setVisibility(View.GONE);
                }
                else {
                    mMusicTextView.setText(music.substring(1));
                }
            }

            @Override
            public void onFailure() {

            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_MODEL,mMovieModel);
        outState.putString(USER_UID,userUid);
        super.onSaveInstanceState(outState);
    }

    private void addMovieToWatchedList() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.MOVIE).child(Util.FireBaseConstants.WATCHED);
        reference.child(mMovieModel.getId() + "").setValue(mMovieModel.getId(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //todo: add sncakbar for completion
            }
        });
    }

    private void addMovieToWatchLaterList(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Util.FireBaseConstants.USER).child(userUid).child(Util.FireBaseConstants.MOVIE).child(Util.FireBaseConstants.WISHLIST);
        reference.child(mMovieModel.getId() + "").setValue(mMovieModel.getId(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //todo: add sncakbar for completion
            }
        });
    }
}
