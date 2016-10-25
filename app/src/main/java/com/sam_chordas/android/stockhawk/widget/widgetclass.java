package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.StockDetailActivity;

import java.net.URL;
import java.util.Random;

/**
 * Created by Ekta on 24-10-2016.
 */
public class widgetclass extends AppWidgetProvider {
        private static final String ACTION_CLICK = "ACTION_CLICK";

        @Override
        public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                             int[] appWidgetIds) {

                for (int i = 0; i< appWidgetIds.length ; i++){

                        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

                        //Update the widget
                        views.setRemoteAdapter(R.id.lv_stock_widget_layout, new Intent(context, WidgetService.class));

                        Intent intent = new Intent(context, StockDetailActivity.class);
                        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                        views.setPendingIntentTemplate(R.id.lv_stock_widget_layout, pendingIntent);

                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.lv_stock_widget_layout);
                        appWidgetManager.updateAppWidget(appWidgetIds[i], views);
                }
        }
}