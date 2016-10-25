package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by Ekta on 25-10-2016.
 */
public class WidgetService   extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockRVFactory(this.getApplicationContext(), intent);
    }


    /**
     * Equivalent to a CursorAdapter/ArrayAdapter with ListView.
     */
    public class StockRVFactory implements RemoteViewsFactory {

        private final String TAG = StockRVFactory.class.getSimpleName();
        private Context context;
        private Cursor cursor;
        private int appWidgetId;

        public StockRVFactory(Context context, Intent intent) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }







        @Override
        public void onCreate() {

            cursor = getContentResolver().query(
                    QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{QuoteColumns._ID, //0
                            QuoteColumns.SYMBOL, //1
                            QuoteColumns.BIDPRICE, //2
                            QuoteColumns.CHANGE, //3
                            QuoteColumns.ISUP}, //4
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null
            );
        }

        /**
         *
         * Called when notifyDataSetChanged() is called.
         * Hence we can update the widget with new data!
         */
        @Override
        public void onDataSetChanged() {
            cursor = getContentResolver().query(
                    QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{QuoteColumns._ID, //0
                            QuoteColumns.SYMBOL, //1
                            QuoteColumns.BIDPRICE, //2
                            QuoteColumns.CHANGE, //3
                            QuoteColumns.ISUP}, //4
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null
            );
        }

        @Override
        public void onDestroy() {
            //close the cursor.
            if (this.cursor != null)
                this.cursor.close();
        }

        @Override
        public int getCount() {
            //Meta-function for the AppWidgetManager
            return (this.cursor != null) ? this.cursor.getCount() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(this.context.getPackageName(), R.layout.list_item_quote);

            if (this.cursor.moveToPosition(position)) {
                String symbol = cursor.getString(1);
                remoteViews.setTextViewText(R.id.stock_symbol, symbol);
                remoteViews.setTextViewText(R.id.bid_price, cursor.getString(2));
                remoteViews.setTextViewText(R.id.change, cursor.getString(3));

                if (cursor.getInt(4) == 1) {
                    remoteViews.setInt(R.id.change, "setBackgroundResource",
                            R.drawable.percent_change_pill_green);
                } else {
                    remoteViews.setInt(R.id.change, "setBackgroundResource",
                            R.drawable.percent_change_pill_red);
                }

                Bundle extras = new Bundle();
                extras.putString("symbol", symbol);

                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                remoteViews.setOnClickFillInIntent(R.id.ll, fillInIntent);

            }


            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {

            return 1;
        }

        @Override
        public long getItemId(int position) {

            return this.cursor.getInt(0);
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }
    }

}