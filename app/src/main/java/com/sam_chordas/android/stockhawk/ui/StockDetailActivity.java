package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;


import com.sam_chordas.android.stockhawk.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StockDetailActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private OkHttpClient client = new OkHttpClient();
    String companyName;
    private ArrayList<String> labels;
    private ArrayList<Float> values;
    @InjectView(R.id.stock_name)
    TextView company_name;
    @InjectView(R.id.stock_symbol)
    TextView stock_symbol;
    @InjectView(R.id.linechartz)
    ValueLineChart valueLineChart;
    Boolean isConnected;
    String symbol;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_stock_detail);
        ButterKnife.inject(this);
        isConnected();
        symbol = getIntent().getExtras().getString("symbol");
        downloadStockDetails();

    }

    public void isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected)
            Toast.makeText(mContext, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();

    }

    String url;

    private void downloadStockDetails() {
        OkHttpClient client = new OkHttpClient();

            url = "http://chartapi.finance.yahoo.com/instrument/1.0/" + symbol + "/chartdata;type=quote;range=1m/json";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() == 200) { //on Success
                    try {

                        String result = response.body().string();
                        if (result.startsWith("finance_charts_json_callback( ")) {
                            result = result.substring(29, result.length() - 2);
                        }


                        JSONObject object = new JSONObject(result);
                        companyName = object.getJSONObject("meta").getString("Company-Name");
                        labels = new ArrayList<>();
                        values = new ArrayList<>();
                        JSONArray series = object.getJSONArray("series");
                        for (int i = 0; i < series.length(); i++) {
                            JSONObject seriesItem = series.getJSONObject(i);
                            SimpleDateFormat srcFormat = new SimpleDateFormat("yyyyMMdd");
                            String date = android.text.format.DateFormat.
                                    getMediumDateFormat(getApplicationContext()).
                                    format(srcFormat.parse(seriesItem.getString("Date")));
                            labels.add(date);
                            values.add(Float.parseFloat(seriesItem.getString("close")));
                            Log.v("close", seriesItem.getString("close"));
                        }

                        onDownloadCompleted();
                    } catch (Exception e) {
                        onDownloadFailed();
                        e.printStackTrace();
                    }
                } else {
                    onDownloadFailed();
                }
            }


            @Override
            public void onFailure(Request request, IOException e) {
                onDownloadFailed();
            }
        });
    }

    private void onDownloadCompleted() {
        StockDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                company_name.setText(companyName);
                stock_symbol.setText(symbol);
                ValueLineSeries series = new ValueLineSeries();
                series.setColor(0xFF56B7F1);
                for (int i = 0; i < labels.size(); i++) {
                    series.addPoint(new ValueLinePoint(labels.get(i), values.get(i)));
                }

                valueLineChart.addSeries(series);
                valueLineChart.setVisibility(View.VISIBLE);

            }

        });

    }

    private void onDownloadFailed() {
        StockDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        downloadStockDetails();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}