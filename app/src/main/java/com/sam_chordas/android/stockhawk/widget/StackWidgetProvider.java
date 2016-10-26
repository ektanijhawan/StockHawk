package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.StockDetailActivity;

/**
 * Created by Ekta on 24-10-2016.
 */
public class StackWidgetProvider extends AppWidgetProvider {
        public static final String ACTION = "com.sam_chordas.android.stockhawk.widget.StockWidgetProvider.ACTION";
        public static final String EXTRA_ITEM = "com.sam_chordas.android.stockhawk.widget.StockWidgetProvider.EXTRA_ITEM";

        @Override
        public void onReceive(Context context, Intent intent) {
                AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                if(intent.getAction().equals(ACTION)){
                        String symbol = intent.getStringExtra(EXTRA_ITEM);
                        Intent detail_intent = new Intent(context, StockDetailActivity.class);
                        detail_intent.putExtra("symbol", symbol);
                        detail_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(detail_intent);
                }
                super.onReceive(context, intent);
        }

        @Override
        public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                             int[] appWidgetIds) {

                for (int i = 0; i< appWidgetIds.length ; i++){

                        Intent intent = new Intent(context, StackWidgetService.class);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
                        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                        rv.setRemoteAdapter(appWidgetIds[i], R.id.lv_stock_widget_layout, intent);
                        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

                        //Update the widget
                        views.setRemoteAdapter(R.id.lv_stock_widget_layout, new Intent(context, StackWidgetService.class));

                        Intent toastIntent = new Intent(context, StackWidgetProvider.class);
                        toastIntent.setAction(StackWidgetProvider.ACTION);
                        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
                        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setPendingIntentTemplate(R.id.lv_stock_widget_layout, toastPendingIntent);

                        appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
                }
                super.onUpdate(context, appWidgetManager, appWidgetIds);
        }
}
