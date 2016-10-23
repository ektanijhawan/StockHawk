package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;

import java.net.URL;

/**
 * Created by Ekta on 24-10-2016.
 */
public class widgetclass extends AppWidgetProvider {
public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int i=0; i<appWidgetIds.length; i++){
        int currentWidgetId = appWidgetIds[i];

            String url="";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));

        PendingIntent pending = PendingIntent.getActivity(context, 0,intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_my_stocks);

        views.setOnClickPendingIntent(R.id.ll, pending);
        appWidgetManager.updateAppWidget(currentWidgetId,views);
        Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();
        }
        }
        }