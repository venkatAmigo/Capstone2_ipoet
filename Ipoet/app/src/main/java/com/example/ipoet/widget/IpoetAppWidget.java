package com.example.ipoet.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.ipoet.HomeActivity;
import com.example.ipoet.R;

/**
 * Implementation of App Widget functionality.
 */
public class IpoetAppWidget extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId, String poem) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ipoet_app_widget);
        Intent intent=new Intent(context, HomeActivity.class);
        PendingIntent pi= PendingIntent.getActivity(context,0,intent,0);
        views.setTextViewText(R.id.appwidget_text, poem);
        views.setOnClickPendingIntent(R.id.appwidget_text,pi);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PoemWidgetService.seeviceCall(context,"Tap for poems");

    }
    static void onUpdatePoems(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,String poem){
        for(int appWidgetId:appWidgetIds ){
            updateAppWidget(context,appWidgetManager,appWidgetId,poem);
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

