package com.example.trading_android.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.adapter.UserAddressAdapter;
import com.example.trading_android.model.CommodityRecord;
import com.example.trading_android.model.UserAddress;
import com.example.trading_android.util.ServerResponse;
import com.example.trading_android.view.AddressDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonCommodityBuyedInfoActivity extends AppCompatActivity {
    private static final String TAG = "COMMODITYBUYActivity" ;
    private  static final int UPDATE_address= 1;
    private  static final int Save_ADDRESS= 2;
    private  static final int UPDATE_BUY= 3;
    private String id;
    private TextView commodityName,commoditySize,commodityPrice,finalPrice;
    private TextView addressName,addressPhone,addressPlace,addressImage;
    private TextView commodityBuy,commodityState,commodityCententPrice,tradingPrice;
    private TextView orderNumber,courierNumber,commodityCreatTime;
    private AlertDialog.Builder builder;
    private ImageView imageBACK,commodityIMG;
    private RefreshLayout refreshLayout;
    private LinearLayout linearLayout;
    private AddressDialog dialog;
    private CommodityRecord commodityRecord;
    private ServerResponse<CommodityRecord> serverResponseBuy;


    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_BUY:
                    Glide.with(PersonCommodityBuyedInfoActivity.this).load(commodityRecord.getCommodity().getMainImage()).into(commodityIMG);
                    commodityName.setText(commodityRecord.getCommodity().getName());
                    commodityPrice.setText(String.valueOf(commodityRecord.getPrice()));
                    commoditySize.setText(String.valueOf(commodityRecord.getSize()));
                    addressPlace.setText(commodityRecord.getAddress().getAddress());
                    addressPhone.setText(commodityRecord.getAddress().getPhone());
                    addressName.setText(commodityRecord.getAddress().getName());
                    char Title[] =commodityRecord.getAddress().getName().toCharArray();
                    addressImage.setText(String.valueOf(Title[0]));
                    if (commodityRecord.getState()==0){
                        commodityState.setText("未发货");
                        commodityBuy.setVisibility(View.GONE);
                    }else if(commodityRecord.getState()==1){
                        commodityState.setText("待收货"+"");
                        commodityBuy.setVisibility(View.VISIBLE);
                    }else{
                        commodityState.setText("交易完成");
                        commodityBuy.setVisibility(View.GONE);
                    }
                    tradingPrice.setText(String.valueOf("￥"+commodityRecord.getTradingPrice()));
                    commodityCententPrice.setText("+"+String.valueOf((commodityRecord.getTradingPrice()-commodityRecord.getPrice()-23)));
                    orderNumber.setText(String.valueOf(commodityRecord.getOrderNumber()));
                    courierNumber.setText(String.valueOf(commodityRecord.getCourierNumber()));
                    Date time =commodityRecord.getBuyTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeFormat = sdf.format(time);
                    commodityCreatTime.setText(timeFormat);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_buyed_info);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        Bundle mbundle = intent.getExtras();
        id = mbundle.getString("id");
        initViewID();
        initPost();
        imageBACK.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commodityBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentCommodityAFTER();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                initPost();
            }
        });

    }

    private void sentCommodityAFTER() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.POSR_CommodityReceiving_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("id",id)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    ServerResponse<Boolean> serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<Boolean>>(){}.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void initPost() {
        BuyCommodity();
    }

    private void BuyCommodity() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_Commodity_BUY_HISTORY_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("id",id)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    serverResponseBuy = gson.fromJson(responsedate,new TypeToken<ServerResponse<CommodityRecord>>(){}.getType());
                    commodityRecord = (CommodityRecord) serverResponseBuy.getData();
                    Log.d("xushulong0147789",commodityRecord.toString());
                    Message msg= Message.obtain();
                    msg.what=UPDATE_BUY;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initViewID() {
        imageBACK= findViewById(R.id.imageBack);
        commodityIMG =findViewById(R.id.commodity_buy_img);
        commodityName =findViewById(R.id.commodity_buy_name);
        commodityPrice =findViewById(R.id.commodity_buy_price);
        commoditySize =findViewById(R.id.commodity_buy_Size);
        addressName = findViewById(R.id.address_buy_name);
        addressPhone = findViewById(R.id.address_buy_Phone);
        addressPlace = findViewById(R.id.address_buy_place);
        addressImage = findViewById(R.id.address_buy_image);
        linearLayout = findViewById(R.id.change_address);
        finalPrice =findViewById(R.id.final_price);
        commodityBuy =findViewById(R.id.commodity_save);
        commodityState = findViewById(R.id.commdoity_state);
        commodityCententPrice = findViewById(R.id.tv_centent_price);
        tradingPrice = findViewById(R.id.tv_trading_price);
        commodityCreatTime =findViewById(R.id.commodity_time);
        courierNumber = findViewById(R.id.courierNumber);
        orderNumber =findViewById(R.id.orderNumber);
        refreshLayout =findViewById(R.id.refresh_commodityBuyed);
    }
}


