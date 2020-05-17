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

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.adapter.UserAddressAdapter;
import com.example.trading_android.model.CommodityStorage;
import com.example.trading_android.model.User;
import com.example.trading_android.model.UserAddress;
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

public class UserMessageActivity  extends AppCompatActivity {

    private static final String TAG = "UserMessageActivity" ;
    private  static final int UPDATE_message= 1;
    private  String name;

    private  ImageView tvImg,imageView;
    private  TextView  tvNickname,tvSex,tvAge,tvIntroduce,tvEmail;
    private  TextView  messageChange;
    private  RefreshLayout refreshLayout;
    private User user ;
    private ServerResponse<User> serverResponseUser;

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_message:
                    tvNickname.setText(user.getUserMessages().getNickName());
                    tvSex.setText(user.getUserMessages().getSex());
                    tvAge.setText(user.getUserMessages().getAge());
                    tvIntroduce.setText(user.getUserMessages().getIntroduce());
                    tvEmail.setText(user.getUserMessages().getEmail());
                    Glide.with(UserMessageActivity.this)
                            .load(user.getUserMessages().getImg())
                            .into(tvImg);

                    break;
                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        SharedPreferences sharedPreferences=getSharedPreferences("loginCentent", Context.MODE_PRIVATE);
        name=sharedPreferences.getString("name",null);
        initViewID();
        initPost();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        messageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mBundle = new Bundle();
                mBundle.putString("img",user.getUserMessages().getImg());
                mBundle.putString("nickName",user.getUserMessages().getNickName());
                mBundle.putString("age",user.getUserMessages().getAge());
                mBundle.putString("sex",user.getUserMessages().getSex());
                mBundle.putString("email",user.getUserMessages().getEmail());
                mBundle.putString("introduce",user.getUserMessages().getIntroduce());
                Intent intent = new Intent(UserMessageActivity.this, UserMessageChangeActivity.class);
                intent.putExtras(mBundle);
                startActivity(intent);
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

    private void initPost() {
        getUserMessage();
    }

    private void  getUserMessage() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_USER_INFO_BY_UID_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userName",name)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    serverResponseUser = gson.fromJson(responsedate,new TypeToken<ServerResponse<User>>(){}.getType());
                    user = (User) serverResponseUser.getData();
                    Message msg= Message.obtain();
                    msg.what=UPDATE_message;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initViewID() {
        tvAge = findViewById(R.id.tv_age);
        tvSex = findViewById(R.id.tv_sex);
        tvEmail = findViewById(R.id.tv_email);
        tvImg = findViewById(R.id.tv_img);
        tvIntroduce = findViewById(R.id.tv_introduce);
        tvNickname = findViewById(R.id.tv_username);

        imageView =findViewById(R.id.detail_back);
        messageChange =findViewById(R.id.message_change);
        refreshLayout =findViewById(R.id.refreshLayout_message);
    }
}
