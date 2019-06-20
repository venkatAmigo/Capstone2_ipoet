package com.example.ipoet.widget;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.ipoet.R;

public class PoemWidgetService extends IntentService {

    public PoemWidgetService() {
        super("asdf");
    }
    public static void seeviceCall(Context context,String poem){
        Intent intent=new Intent(context, PoemWidgetService.class);
        intent.putExtra("Poem",poem);
        intent.setAction(context.getString(R.string.ACTION));
        context.startService(intent);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent.getAction().equals(getString(R.string.ACTION))){
            String poems=intent.getStringExtra("Poem");
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(this);
            int[] appWidgetids=appWidgetManager.getAppWidgetIds(new ComponentName(this, IpoetAppWidget.class));
            IpoetAppWidget.onUpdatePoems(this,appWidgetManager,appWidgetids,poems);

        }

    }
}
