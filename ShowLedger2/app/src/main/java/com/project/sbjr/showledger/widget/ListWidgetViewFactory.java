package com.project.sbjr.showledger.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.project.sbjr.showinfodatabase.HighOnShow;
import com.project.sbjr.showinfodatabase.handler.ShowHandler;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showinfodatabase.response.MovieResponse;
import com.project.sbjr.showinfodatabase.response.TvOnAirResponse;
import com.project.sbjr.showledger.R;

import java.util.ArrayList;

/**
 * factory class providing data to populate list
 * */
public class ListWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private ArrayList<String> listItems = new ArrayList<>();
    private boolean isMovie=true;
    private int mAppWidgetId;
    boolean isFetched=false;

    private void populateMovies(){
        isFetched = true;
        Log.d("data fetch","populateMovies()");
        new HighOnShow(context.getString(R.string.api_key)).initMovie().getUpcomingMovies(null, null, null, new ShowHandler<MovieResponse>() {

            @Override
            public void onResult(MovieResponse result) {
                for(MovieModel model: result.getResults()){
                    listItems.add(model.getTitle());
                }Log.d("data fetch",listItems.size()+" by populateMovieShow()");
                AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(mAppWidgetId,R.id.movie_list);

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void populateTvShow(){
        Log.d("data fetch","populateTvShow()");
        new HighOnShow(context.getString(R.string.api_key)).initTvShow().getTvShowOnAir(null, null, null, new ShowHandler<TvOnAirResponse>() {
            @Override
            public void onResult(TvOnAirResponse result) {
                for(TvShowModel model:result.getResults()){
                    listItems.add(model.getName());
                }
                Log.d("data fetch",listItems.size()+" by populateTvShow()");
                AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(mAppWidgetId,R.id.tv_list);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public ListWidgetViewFactory(Context context, Intent intent) {
        this.context = context;
        isMovie = intent.getBooleanExtra("type",true);
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.d("data fetch","onCreate()");
        if(isMovie) {
            populateMovies();
        }
        else{
            populateTvShow();
        }
    }

    @Override
    public void onDataSetChanged() {
        Log.d("data fetch","onDataSetChanged()");
        if(isFetched){
           return;
        }
        if(isMovie) {
            populateMovies();
        }
        else{
            populateTvShow();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d("data fetch","getViewAt() for "+position);
        RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.widget_list_item);
        rv.setTextViewText(R.id.content, listItems.get(position));

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_loading_layout);
        return remoteViews;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
