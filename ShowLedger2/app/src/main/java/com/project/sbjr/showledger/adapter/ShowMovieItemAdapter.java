package com.project.sbjr.showledger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showledger.R;
import com.project.sbjr.showledger.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sbjr on 26/12/16.
 */

public class ShowMovieItemAdapter extends RecyclerView.Adapter<ShowMovieItemAdapter.ShowItemViewHolder>{

    private ArrayList<MovieModel> mMovieList = new ArrayList<>();

    private ShowMovieItemAdapterInteractionListener mListener;

    public ShowMovieItemAdapter(ArrayList<MovieModel> mMovieList, ShowMovieItemAdapterInteractionListener mListener) {
        this.mMovieList = mMovieList;
        this.mListener = mListener;
    }

    @Override
    public ShowItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.show_detail_list_item,parent,false);
        return new ShowItemViewHolder(context,view,mListener);
    }

    @Override
    public void onBindViewHolder(ShowItemViewHolder holder, int position) {
        holder.fillData(mMovieList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    class ShowItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView,mRatingTextView;
        private ImageView mImageView;
        private ShowMovieItemAdapterInteractionListener mListener;
        MovieModel mMovie;
        private Context mContext;

        public ShowItemViewHolder(Context context,View itemView,ShowMovieItemAdapterInteractionListener listener) {
            super(itemView);
            mContext = context;
            mTitleTextView = (TextView) itemView.findViewById(R.id.title);
            mRatingTextView = (TextView) itemView.findViewById(R.id.rating);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        public void fillData(MovieModel movieModel){
            mMovie = movieModel;
            mTitleTextView.setText(mMovie.getTitle());
            mRatingTextView.setText(mMovie.getVote_average()+"");
            Picasso.with(mContext)
                    .load("https://image.tmdb.org/t/p/w185"+movieModel.getPoster_path())
                    .placeholder(Util.getRandomColor())
                    .fit()
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            mListener.ShowMovieItemClickListener(mMovie);
        }
    }

    public interface ShowMovieItemAdapterInteractionListener {
        void ShowMovieItemClickListener(MovieModel movie);
    }
}
