package com.example.trading_android.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.adapter.CommoditySellListAdapter;
import com.example.trading_android.adapter.CommoditySellOrderAdapter;
import com.example.trading_android.model.CommodityRecord;
import com.example.trading_android.model.CommodityStorage;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//商品的出售订单
public class CommoditySellOrderActivity extends AppCompatActivity {

    private static final String TAG = "COMMODITYBUYActivity" ;
    private  static final int UPDATE_SELL= 1;
    private  static final int UPDATE_NOTSELL= 2;
    private  static final int UPDATE_BUY= 3;
    private RefreshLayout refreshLayout;
    private LinearLayout addressMessage,personMessage;
    private ImageView imageView;
    private RelativeLayout relativeLayout;
    private ListView listView;
    private String uid;
    private List<CommodityStorage> commodityStorages ;
    private ServerResponse<List<CommodityStorage>> serverResponse;

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_NOTSELL:
                    relativeLayout.setVisibility(View.VISIBLE);
                    break;
                case UPDATE_SELL:
//                    CommoditySellListAdapter listAdapter = new  CommoditySellListAdapter(commodityRecords,PersonCommoditySellActivity.this);
//                    listView.setAdapter(listAdapter);
                    CommoditySellOrderAdapter lAdapter = new CommoditySellOrderAdapter(commodityStorages,CommoditySellOrderActivity.this);
                    listView.setAdapter(lAdapter);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_order);
        SharedPreferences sharedPreferences=getSharedPreferences("loginCentent", Context.MODE_PRIVATE);
        uid=sharedPreferences.getString("uid",null);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        initViewID();
        initPost();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                initPost();
            }
        });
    }

    private void initPost() {
        getALLCommodityStorage();
    }

    private void getALLCommodityStorage() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_CommoditySELL_Order_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("uid",uid)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<CommodityStorage>>>(){}.getType());
                    commodityStorages = (List<CommodityStorage>) serverResponse.getData();
                    if (commodityStorages.size() ==0) {
                        Message msg= Message.obtain();
                        msg.what=UPDATE_NOTSELL;
                        mHandler.sendMessage(msg);
                    }else{
                        Message msg= Message.obtain();
                        msg.what=UPDATE_SELL;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private void initViewID() {
        imageView = findViewById(R.id.detail_back);
        relativeLayout =findViewById(R.id.user_info_show_layout);
        listView =findViewById(R.id.commodity_sellorder_list);
        refreshLayout = findViewById(R.id.refresh_person_sellORDER);
    }
}
