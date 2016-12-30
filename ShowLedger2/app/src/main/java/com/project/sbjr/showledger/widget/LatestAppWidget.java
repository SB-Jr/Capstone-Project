package com.project.sbjr.showledger.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
 * Implementation of App Widget functionality.
 */
public class LatestAppWidget extends AppWidgetProvider {


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("data fetch","onReceive()");
        if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), LatestAppWidget.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.movie_list);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d("data fetch","onUpdate()");
        for (int appWidgetId : appWidgetIds) {

            Intent movieIntent = new Intent(context,ListWidgetService.class);
            movieIntent.putExtra("type",true);
            movieIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
            movieIntent.setData(Uri.parse(movieIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.latest_app_widget);
            rv.setRemoteAdapter(R.id.movie_list, movieIntent);
            rv.setEmptyView(R.id.movie_list,R.id.empty_view);

            /*Intent tvIntent = new Intent(context,ListWidgetService.class);
            movieIntent.putExtra("type",false);

            rv.setRemoteAdapter(R.id.tv_list, tvIntent);
            rv.setEmptyView(R.id.tv_list,R.id.empty_view);*/

            appWidgetManager.updateAppWidget(appWidgetId, rv);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

