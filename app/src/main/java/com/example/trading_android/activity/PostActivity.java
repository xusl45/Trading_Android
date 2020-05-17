package com.example.trading_android.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.adapter.PostShowAdapter;
import com.example.trading_android.model.Post;
import com.example.trading_android.util.ServerResponse;
import com.example.trading_android.view.ExpandableListView;
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

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "UserMessageActivity" ;
    private  static final int UPDATE_Post= 1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private  String id,img,name;
    private ImageView imageView;
    private TextView sortName,postNum;
    private ListView listView;
    private FloatingActionButton FFab;
    private List<Post> posts;
    private ServerResponse<List<Post>> serverResponse;

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_Post:
                    postNum.setText(String.valueOf(posts.size()));
                    PostShowAdapter ladpter = new PostShowAdapter(posts,PostActivity.this);
                    listView.setAdapter(ladpter);
                    break;
                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        Bundle mbundle = intent.getExtras();
        id = mbundle.getString("id");
        img = mbundle.getString("img");
        name = mbundle.getString("name");
        if(actionBar != null){
            actionBar.hide();
        }

        initPost();
        initViewID();
        Glide.with(PostActivity.this)
                .load(img).into(imageView);
        sortName.setText(name);
        FFab.setOnClickListener(new View.OnClickListener() {
            /****************************这里跳转到new  post活动************************8*/
            @Override
            public void onClick(View v) {
                Toast.makeText(PostActivity.this, "您点击了悬浮按钮", Toast.LENGTH_SHORT).show();
                Bundle mBundle = new Bundle();
                mBundle.putString("id",id);
                Intent intent = new Intent(PostActivity.this, PostSendActivity.class);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.common_red);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               initPost();
            }
        });
    }

    private void initViewID() {
        imageView = findViewById(R.id.title_img);
        sortName =findViewById(R.id.title_post_nickname);
        postNum =findViewById(R.id.title_post_count);
        listView =findViewById(R.id.Post_Plist);
        FFab = findViewById(R.id.first_FAB);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.first_Refresh);
    }

    private void initPost() {
        getPostBYid();
    }

    private void getPostBYid() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_Post_HISTORY_URL;
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
                    Log.d("UserMessageActivity",responsedate);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                     serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<Post>>>(){}.getType());
                    posts = (List<Post>)serverResponse.getData();
                    for (Post post :posts)
                    {
                        Log.d("UserMessageActivity",post.toString());
                    }
                    Message msg= Message.obtain();
                    msg.what=UPDATE_Post;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
