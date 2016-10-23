package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.example.sam_chordas.stockhawk.*;
import com.sam_chordas.android.stockhawk.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
public class StockDetailActivity extends Activity {


@InjectView(R.id.stock_name) TextView stock_name;
    @InjectView(R.id.stock_symbol) TextView stock_symbol;
    @InjectView(R.id.bid_price) TextView bid_prie;
    String symbol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.inject(this);
         symbol = getIntent().getExtras().getString("symbol");
    }

}
