package com.project.sbjr.showledger.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.Size;
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
    private int mAppWidgetId;

    public ListWidgetViewFactory(Context context, Intent intent) {
        this.context = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        listItems = intent.getStringArrayListExtra(LatestAppWidget.CONTENT_DATA);
    }

    @Override
    public void onCreate() {
        Log.d("data fetch","onCreate()");
    }

    @Override
    public void onDataSetChanged() {
        Log.d("data fetch","onDataSetChanged()");
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
        Log.d("data fetch","getViewAt() for "+position+" == "+listItems.get(position));
        RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.widget_list_item);
        rv.setTextViewText(R.id.content,listItems.get(position));
        if(listItems.get(position).equalsIgnoreCase(context.getString(R.string.navigation_movies))||listItems.get(position).equalsIgnoreCase(context.getString(R.string.navigation_tvshows))){
            SpannableString s = new SpannableString(listItems.get(position));
            s.setSpan(new StyleSpan(Typeface.BOLD),0,listItems.get(position).length(),0);
            s.setSpan(new RelativeSizeSpan(1.5f),0,listItems.get(position).length(),0);
            s.setSpan(new ForegroundColorSpan(Color.WHITE),0,listItems.get(position).length(),0);
            rv.setTextViewText(R.id.content, s);
            rv.setInt(R.id.content, "setBackgroundColor", context.getResources().getColor(R.color.colorPrimary));
        }
        else{
            rv.setInt(R.id.content, "setBackgroundColor", Color.WHITE);
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(),R.layout.widget_loading_layout);
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
