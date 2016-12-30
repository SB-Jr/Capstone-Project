package com.project.sbjr.showledger.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * service providing factor object for widget list view
 * */
public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListWidgetViewFactory(this,intent);
    }
}
