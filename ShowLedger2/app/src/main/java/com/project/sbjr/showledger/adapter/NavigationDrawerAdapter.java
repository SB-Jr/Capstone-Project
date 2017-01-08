package com.project.sbjr.showledger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.sbjr.showledger.R;

/**
 * Created by sbjr on 25/12/16.
 */

public class NavigationDrawerAdapter  extends RecyclerView.Adapter<NavigationDrawerAdapter.NavigationDrawerViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private NavigationDrawerItemClickListener listener;

    public NavigationDrawerAdapter(Context context,NavigationDrawerItemClickListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener  = listener;
    }

    public interface NavigationDrawerItemClickListener{
        void OnNavigationDrawerItemClickListener(int pos);
    }

    public class NavigationDrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private ImageView mTitleImageView;
        private int position;
        private NavigationDrawerItemClickListener listener;


        public NavigationDrawerViewHolder(View itemView,NavigationDrawerItemClickListener listener) {
            super(itemView);

            mTitleImageView = (ImageView) itemView.findViewById(R.id.title_image);
            mTitleTextView = (TextView) itemView.findViewById(R.id.title);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            this.listener = listener;
        }

        public void onBind(int pos){
            position = pos;
            switch (pos){
                case 0: mTitleImageView.setImageResource(R.drawable.ic_local_movies_grey_400_24dp);
                        mTitleTextView.setText(context.getString(R.string.navigation_movies));
                    break;
                case 1: mTitleImageView.setImageResource(R.drawable.ic_tv_show);
                        mTitleTextView.setText(context.getString(R.string.navigation_tvshows));
                    break;
                case 2: mTitleImageView.setImageResource(R.drawable.ic_sign_out);
                    mTitleTextView.setText(context.getString(R.string.navigation_signout));
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            listener.OnNavigationDrawerItemClickListener(position);
        }
    }


    @Override
    public NavigationDrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.navigation_drawer_content_holder,parent,false);
        NavigationDrawerViewHolder holder = new NavigationDrawerViewHolder(view,listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(NavigationDrawerViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
