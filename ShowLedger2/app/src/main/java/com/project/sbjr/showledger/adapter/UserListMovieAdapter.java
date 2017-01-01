package com.project.sbjr.showledger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.sbjr.showinfodatabase.HighOnShow;
import com.project.sbjr.showinfodatabase.handler.ShowHandler;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showledger.R;

import java.util.ArrayList;

/**
 * Created by sbjr on 31/12/16.
 */

public class UserListMovieAdapter extends RecyclerView.Adapter<UserListMovieAdapter.UserListItemHolder>{

    private ArrayList<MovieModel> movieModels;
    private Context mContext;
    private UserListMovieAdapterInteraction mListener;

    public UserListMovieAdapter(Context context, UserListMovieAdapterInteraction listener, ArrayList<Integer> movies) {
        mContext = context;
        mListener = listener;
        movieModels = new ArrayList<>();
        for(int id:movies){
            MovieModel model = new MovieModel();
            model.setId(id);
            movieModels.add(model);
        }
    }

    public class UserListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView,mRatingTextView;
        private ImageView mImageView;
        private MovieModel movieModel;
        private UserListMovieAdapterInteraction mListener;

        public UserListItemHolder(View itemView,UserListMovieAdapterInteraction listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            mListener = listener;
            mTitleTextView = (TextView) itemView.findViewById(R.id.title);
            mRatingTextView = (TextView) itemView.findViewById(R.id.rating);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
        public void setData(MovieModel movieModel){
            this.movieModel = movieModel;
            mTitleTextView.setText(movieModel.getTitle());
            mRatingTextView.setText(movieModel.getRelease_date());
        }

        @Override
        public void onClick(View v) {
            mListener.userListMovieAdapterItemOnClickLstener(movieModel);
        }
    }

    @Override
    public UserListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_detail_list_item,parent,false);
        return new UserListItemHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(final UserListItemHolder holder, final int position) {
        new HighOnShow(mContext.getString(R.string.api_key)).initMovie().getMovieDetails(movieModels.get(position).getId(), null, null, null, new ShowHandler<MovieModel>() {
            @Override
            public void onResult(MovieModel result) {
                MovieModel model = movieModels.get(holder.getAdapterPosition());
                model.setAdult(result.getAdult());
                model.setBackdrop_path(result.getBackdrop_path());
                model.setGenres(result.getGenres());
                model.setOverview(result.getOverview());
                model.setPoster_path(result.getPoster_path());
                model.setRelease_date(result.getRelease_date());
                model.setTitle(result.getTitle());
                model.setVote_average(result.getVote_average());
                /*movieModels.remove(holder.getAdapterPosition());
                movieModels.add(holder.getAdapterPosition(),model);
                */
                movieModels.set(holder.getAdapterPosition(),model);
                holder.setData(model);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }

    public interface UserListMovieAdapterInteraction {
        void userListMovieAdapterItemOnClickLstener(MovieModel movieModel);
    }

}
