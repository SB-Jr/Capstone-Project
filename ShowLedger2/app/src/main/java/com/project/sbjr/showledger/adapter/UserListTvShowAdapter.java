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
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sbjr on 1/1/17.
 */

public class UserListTvShowAdapter extends RecyclerView.Adapter<UserListTvShowAdapter.UserListItemHolder> {

    private ArrayList<TvShowModel> tvShowModels;
    private Context mContext;
    private UserListTvShowAdapterInteraction mListener;

    public UserListTvShowAdapter(Context mContext, UserListTvShowAdapterInteraction mListener, ArrayList<Integer> tvShows) {
        this.mContext = mContext;
        this.mListener = mListener;
        tvShowModels = new ArrayList<>();
        for (int t : tvShows) {
            TvShowModel tvShowModel = new TvShowModel();
            tvShowModel.setId(t);
            tvShowModels.add(tvShowModel);
        }
    }

    public class UserListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView, mRatingTextView;
        private ImageView mImageView;
        private TvShowModel tvShowModel;
        private UserListTvShowAdapterInteraction mListener;

        public UserListItemHolder(View itemView, UserListTvShowAdapterInteraction listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            mListener = listener;
            mNameTextView = (TextView) itemView.findViewById(R.id.title);
            mRatingTextView = (TextView) itemView.findViewById(R.id.rating);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
        }

        public void setData(TvShowModel tvShowModel) {
            this.tvShowModel = tvShowModel;
            mNameTextView.setText(tvShowModel.getName());
            mRatingTextView.setText(tvShowModel.getVote_average() + "");
            Picasso.with(mContext)
                    .load("https://image.tmdb.org/t/p/w185" + tvShowModel.getPoster_path())
                    .placeholder(Util.getRandomColor())
                    .fit()
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            mListener.userListTvShowAdapterItemOnClickListener(tvShowModel);
        }
    }


    @Override
    public UserListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_detail_list_item, parent, false);
        return new UserListTvShowAdapter.UserListItemHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final UserListItemHolder holder, int position) {
        new HighOnShow(mContext.getString(R.string.api_key)).initTvShow().getTvShowDetailsById(tvShowModels.get(position).getId(), null, null, null, new ShowHandler<TvShowModel>() {
            @Override
            public void onResult(TvShowModel result) {

                TvShowModel tvShowModel = tvShowModels.get(holder.getAdapterPosition());
                tvShowModel.setPoster_path(result.getPoster_path());
                tvShowModel.setOverview(result.getOverview());
                tvShowModel.setBackdrop_path(result.getBackdrop_path());
                tvShowModel.setCreated_by(result.getCreated_by());
                tvShowModel.setFirst_air_date(result.getFirst_air_date());
                tvShowModel.setGenres(result.getGenres());
                tvShowModel.setLast_air_date(result.getLast_air_date());
                tvShowModel.setName(result.getName());
                tvShowModel.setNumber_of_episodes(result.getNumber_of_episodes());
                tvShowModel.setNetworks(result.getNetworks());
                tvShowModel.setNumber_of_seasons(result.getNumber_of_seasons());
                tvShowModel.setVote_average(result.getVote_average());
                tvShowModels.set(holder.getAdapterPosition(), tvShowModel);
                holder.setData(tvShowModel);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return tvShowModels.size();
    }


    public interface UserListTvShowAdapterInteraction {
        void userListTvShowAdapterItemOnClickListener(TvShowModel tvShowModel);
    }

}
