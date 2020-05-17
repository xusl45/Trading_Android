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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.adapter.UserAddressAdapter;
import com.example.trading_android.fragment.MainActivity;
import com.example.trading_android.model.UserAddress;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonAddressActivity extends AppCompatActivity {

    private static final String TAG = "PersonAddressActivity" ;
    private  static final int UPDATE_address= 1;
    private  String Uid;
    private ListView listView;
    private TextView addressAdd;
    private ImageView imageView;
    private RefreshLayout refreshLayout;
    private List<UserAddress> userAddresses ;
    private ServerResponse<List<UserAddress>> serverResponseaddress;

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_address:
                    UserAddressAdapter Ladapter =  new  UserAddressAdapter(userAddresses,PersonAddressActivity.this);
                    listView.setAdapter(Ladapter);
                    break;
                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_address);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        SharedPreferences sharedPreferences=getSharedPreferences("loginCentent", Context.MODE_PRIVATE);
        Uid=sharedPreferences.getString("uid",null);
        initViewID();
        initPost();
        addressAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mBundle = new Bundle();
                mBundle.putString("uid",Uid);
                Intent intent = new Intent(PersonAddressActivity.this, PersonAddressChangeActivity.class);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                getUserAddress();
            }
        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
//                getUserAddress();
//            }
//        });
    }

    private void initPost() {
        getUserAddress();
    }

    private void getUserAddress() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_USER_ADDRESS_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("uid",Uid)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new  Gson();
                    serverResponseaddress = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<UserAddress>>>(){}.getType());
                    userAddresses = (List<UserAddress>) serverResponseaddress.getData();
                    for (UserAddress userAddress :userAddresses)
                    {
                        Log.d(TAG,userAddress.toString());
                    }
                    Message msg= Message.obtain();
                    msg.what=UPDATE_address;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initViewID() {
        listView = findViewById(R.id.address_list);
        addressAdd = findViewById(R.id.address_add);
        refreshLayout =findViewById(R.id.refreshLayout_address);
        imageView =findViewById(R.id.detail_back);

    }
}
