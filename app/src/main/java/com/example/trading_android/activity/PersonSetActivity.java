package com.example.trading_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.trading_android.R;

public class PersonSetActivity extends AppCompatActivity {

    private LinearLayout addressMessage,personMessage;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_set);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        initViewID();
        initPost();
        addressMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("loginCentent", MODE_PRIVATE); sharedPreferences.edit().clear().commit();
                Intent intent = new Intent(PersonSetActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        personMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        addressMessage =  findViewById(R.id.adressMessage);
        personMessage = findViewById(R.id.personMessage);
        imageView = findViewById(R.id.detail_back);
    }
}
