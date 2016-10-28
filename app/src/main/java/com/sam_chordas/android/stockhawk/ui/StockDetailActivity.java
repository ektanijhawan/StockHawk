package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.VolleySingleton;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
public class StockDetailActivity extends Activity  implements  AdapterView.OnItemSelectedListener{

    private OkHttpClient client = new OkHttpClient();
    String companyName;
    String previous_close_price;
    String comapany_symbol;
    private String sortOrder;
    private ArrayList<String> labels;//=new ArrayList<>();
    private ArrayList<Float> values;//= new ArrayList<>();
    private int item = 0;
    @InjectView(R.id.stock_name)
    TextView company_name;
    @InjectView(R.id.stock_symbol)
    TextView stock_symbol;
    @InjectView(R.id.linechartz)
    ValueLineChart valueLineChart;

    String symbol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        ButterKnife.inject(this);
        symbol = getIntent().getExtras().getString("symbol");
        downloadStockDetails();

    }

String url;
    private void downloadStockDetails() {
        OkHttpClient client = new OkHttpClient();
        if (item == 0) {
            url = "http://chartapi.finance.yahoo.com/instrument/1.0/" + symbol + "/chartdata;type=quote;range=1m/json";
        }else if (item == 1) {
           url=  "http://chartapi.finance.yahoo.com/instrument/1.0/" + symbol + "/chartdata;type=quote;range=1y/json";
        }
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
                            Log.v("close",seriesItem.getString("close"));
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


                item = position;
               downloadStockDetails();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}