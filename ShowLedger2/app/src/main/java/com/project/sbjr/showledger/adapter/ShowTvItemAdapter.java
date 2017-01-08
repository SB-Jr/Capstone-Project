package com.project.sbjr.showledger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sbjr on 28/12/16.
 */

public class ShowTvItemAdapter extends RecyclerView.Adapter<ShowTvItemAdapter.ShowItemViewHolder> {

    private ArrayList<TvShowModel> mTvShowList = new ArrayList<>();

    private ShowTvItemAdapterInteractionListener mListener;

    public ShowTvItemAdapter(ArrayList<TvShowModel> mTvShowList, ShowTvItemAdapterInteractionListener mListener) {
        this.mTvShowList = mTvShowList;
        this.mListener = mListener;
    }

    @Override
    public ShowItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.show_detail_list_item, parent, false);
        return new ShowItemViewHolder(context, view, mListener);
    }

    @Override
    public void onBindViewHolder(ShowItemViewHolder holder, int position) {
        holder.fillData(mTvShowList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTvShowList.size();
    }


    class ShowItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView, mRatingTextView;
        private ImageView mImageView;
        private ShowTvItemAdapterInteractionListener mListener;
        TvShowModel mTvShow;
        private Context mContext;

        public ShowItemViewHolder(Context context, View itemView, ShowTvItemAdapterInteractionListener listener) {
            super(itemView);
            mContext = context;
            mTitleTextView = (TextView) itemView.findViewById(R.id.title);
            mRatingTextView = (TextView) itemView.findViewById(R.id.rating);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        public void fillData(TvShowModel tvShowModel) {
            mTvShow = tvShowModel;
            mTitleTextView.setText(mTvShow.getName());
            mRatingTextView.setText(mTvShow.getVote_average() + "");
            Picasso.with(mContext)
                    .load("https://image.tmdb.org/t/p/w185" + mTvShow.getPoster_path())
                    .placeholder(Util.getRandomColor())
                    .fit()
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            mListener.tvShowItemClickListener(mTvShow);
        }
    }

    public interface ShowTvItemAdapterInteractionListener {
        void tvShowItemClickListener(TvShowModel tvShowModel);
    }

}
