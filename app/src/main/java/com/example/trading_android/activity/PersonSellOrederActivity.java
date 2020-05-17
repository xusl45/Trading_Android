package com.example.trading_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.trading_android.R;

public class PersonSellOrederActivity extends AppCompatActivity {

    private LinearLayout didNotSell,didSell;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_sellorder);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        initViewID();
        initPost();
        didNotSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonSellOrederActivity.this, CommoditySellOrderActivity.class);
                startActivity(intent);
            }
        });
        didSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonSellOrederActivity.this, PersonCommoditySellActivity.class);
                startActivity(intent);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initPost() {
    }

    private void initViewID() {
        didNotSell =  findViewById(R.id.did_not_sell);
        didSell = findViewById(R.id.did_sell);
        imageView = findViewById(R.id.detail_back);
    }
}
