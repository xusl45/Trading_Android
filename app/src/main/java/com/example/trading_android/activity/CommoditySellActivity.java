package com.example.trading_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.fragment.MainActivity;
import com.example.trading_android.model.Commodity;
import com.example.trading_android.model.User;
import com.example.trading_android.util.AddressPickTask;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;
import cn.addapp.pickers.picker.NumberPicker;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CommoditySellActivity extends AppCompatActivity {

    private static final String TAG = "CommoditySELLActivity";
    private  static final int UPDATE_Commodity= 1;
    private  static final int UPDATE_Size= 2;
    private  static final int ALERT_Price= 3;
    private  static final int UPDATE_True= 4;
    private  static final int UPDATE_False= 5;
    private ImageView commoditySellImg;
    private TextView commoditysellName,commoditySellPrice,choseText;
    private EditText inputPrice;
    private LinearLayout choseSIZE,commoditySellSend;
    private String id ,Uid,SIZE,price;
    private Commodity commodities;
    private ServerResponse<Commodity> serverResponseCommodity;
    private ServerResponse<String> serverResponse;

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_Commodity:
                    commoditysellName.setText(commodities.getName());
                    commoditySellPrice.setText("最低售价："+commodities.getMinPrice());
                    Glide.with(CommoditySellActivity.this).load(commodities.getMainImage()).into(commoditySellImg);
                    break;
                case UPDATE_Size:
                    choseText.setText("已选：" +SIZE);
                    break;
                case ALERT_Price:
                    Toast.makeText(CommoditySellActivity.this,"请先输入价格",Toast.LENGTH_LONG).show();
                    break;
                case UPDATE_True:
                    Toast.makeText(CommoditySellActivity.this,"添加预售商品成功",Toast.LENGTH_LONG).show();
                    break;
                case UPDATE_False:
                    Toast.makeText(CommoditySellActivity.this,"添加预售商品失败",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_sell);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        Bundle mbundle = intent.getExtras();
        id = mbundle.getString("id");
        SharedPreferences sharedPreferences=getSharedPreferences("loginCentent", Context.MODE_PRIVATE);
        Uid=sharedPreferences.getString("uid",null);
        initViewID();
        initPost();
        choseSIZE.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                onNumberPicker();
            }
        });
        commoditySellSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                price = inputPrice.getText().toString();
                if (price == null) {
                    Message msg=Message.obtain();
                    msg.what=ALERT_Price;
                    mHandler.sendMessage(msg);
                }else{
                    setCommodityStorage();
                    Log.d(TAG,id +Uid + SIZE+price+"xushulong");
                }
            }
        });
    }

    private void setCommodityStorage() {
        final String urlLogin = URLpath.BASE_URL+URLpath.POST_ADD_TO_COMMODITYSTORAGE_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("commodityID",id)
                            .add("uid",Uid)
                            .add("size",SIZE)
                            .add("price",price)
                            .build();
                    Request request =new Request.Builder()
                            .url(urlLogin)
                            .post(requestBody)
                            .build();
                    Response response =client.newCall(request).execute();
                    String  responsedate = response.body().string();
                    Log.d(TAG,responsedate);
                    parseJSNOWITHgson(responsedate);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseJSNOWITHgson(String responsedate) {
        Gson gson = new  Gson();
        serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<Boolean>>(){}.getType());
        Message message = new Message();
        if(serverResponse.getStatu() ==200){
            message.what = UPDATE_True;
            mHandler.sendMessage(message);
            finish();
        }else{
            message.what = UPDATE_False;
            mHandler.sendMessage(message);
        }
    }

    private void initViewID() {
        commoditySellImg = findViewById(R.id.commoditySell_img);
        commoditysellName = findViewById(R.id.commoditySell_name);
        commoditySellPrice = findViewById(R.id.commoditySell_price);
        choseSIZE = findViewById(R.id.choseSIZE);
        choseText = findViewById(R.id.choseText);
        commoditySellSend = findViewById(R.id.commoditySell_send);
        inputPrice = findViewById(R.id.input_price);
    }
    private void initPost() {
        getCommodity();
    }

    //商品
    private void getCommodity() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_COMMODITY_BY_ID_URL;
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
                    Gson gson = new  Gson();
                    serverResponseCommodity = gson.fromJson(responsedate,new TypeToken<ServerResponse<Commodity>>(){}.getType());
                    commodities = (Commodity) serverResponseCommodity.getData();
//                        Log.d(TAG,commodities.toString());
                    Message msg=Message.obtain();
                    msg.what=UPDATE_Commodity;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //尺码选择器
    public void onNumberPicker() {
        NumberPicker picker = new NumberPicker(this);
        picker.setWidth(picker.getScreenWidthPixels() / 2);
        picker.setCanLoop(false);
        picker.setLineVisible(false);
        picker.setOffset(2);//偏移量
        picker.setRange(36, 47, 1);//数字范围
        picker.setSelectedItem(42);
        picker.setLabel("厘米");
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                SIZE = String.valueOf(item.intValue());
                Toast.makeText(CommoditySellActivity.this,"index=" + index + ", item=" + item.intValue(),Toast.LENGTH_LONG).show();
                Message msg=Message.obtain();
                msg.what=UPDATE_Size;
                mHandler.sendMessage(msg);
            }
        });
        picker.show();
    }

}
