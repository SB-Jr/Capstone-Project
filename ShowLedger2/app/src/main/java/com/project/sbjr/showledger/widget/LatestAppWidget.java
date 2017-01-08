package com.project.sbjr.showledger.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.project.sbjr.showinfodatabase.HighOnShow;
import com.project.sbjr.showinfodatabase.handler.ShowHandler;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showinfodatabase.response.MovieResponse;
import com.project.sbjr.showinfodatabase.response.TvResponse;
import com.project.sbjr.showledger.R;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class LatestAppWidget extends AppWidgetProvider {


    public static String CONTENT_DATA = "content_data";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("data fetch", "onReceive()");
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), LatestAppWidget.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list);
        }
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d("data fetch", "onUpdate()");
        for (final int appWidgetId : appWidgetIds) {

            final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.latest_app_widget);

            new HighOnShow(context.getString(R.string.api_key)).initMovie().getUpcomingMovies(null, null, null, new ShowHandler<MovieResponse>() {
                @Override
                public void onResult(MovieResponse result) {
                    ArrayList<String> listItems = new ArrayList<>();

                    Intent mIntent = new Intent(context, ListWidgetService.class);
                    mIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
                    mIntent.setData(Uri.parse(mIntent.toUri(Intent.URI_INTENT_SCHEME)));

                    for (MovieModel model : result.getResults()) {
                        listItems.add(model.getTitle());
                    }

                    listItems.add(0, context.getString(R.string.navigation_movies));
                    final ArrayList<String> movieListItems = new ArrayList<>(listItems);

                    //mIntent.putExtra(CONTENT_DATA, movieListItems);


                    new HighOnShow(context.getString(R.string.api_key)).initTvShow().getTvShowOnAir(null, null, null, new ShowHandler<TvResponse>() {
                        @Override
                        public void onResult(TvResponse result) {

                            ArrayList<String> listItems = new ArrayList<>();

                            Intent finalIntent = new Intent(context, ListWidgetService.class);
                            finalIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
                            finalIntent.setData(Uri.parse(finalIntent.toUri(Intent.URI_INTENT_SCHEME)));

                            for (TvShowModel model : result.getResults()) {
                                listItems.add(model.getName());
                            }
                            listItems.add(0, context.getString(R.string.navigation_tvshows));

                            listItems.addAll(0, movieListItems);

                            finalIntent.putExtra(CONTENT_DATA, listItems);

                            Log.d("data fetch", listItems.size() + " by populateTvShow()");

                            AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(appWidgetId, R.id.list);

                            rv.setEmptyView(R.id.list, R.id.empty_view);
                            rv.setRemoteAdapter(R.id.list, finalIntent);

                            appWidgetManager.updateAppWidget(appWidgetId, rv);
                        }

                        @Override
                        public void onFailure() {

                        }
                    });

                }

                @Override
                public void onFailure() {

                }
            });

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

