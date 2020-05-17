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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.example.trading_android.adapter.CommodityInfoDetailAdapter;
import com.example.trading_android.adapter.PostShowAdapter;
import com.example.trading_android.adapter.ReplyAdapter;
import com.example.trading_android.model.Post;
import com.example.trading_android.model.ReplyReturn;
import com.example.trading_android.model.ShowImages;
import com.example.trading_android.util.ServerResponse;
import com.example.trading_android.view.ExpandableListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostInfoActivity extends AppCompatActivity {

    private  static final int UPDATE_Reply= 1;
    private  static final int INSERT_Reply= 2;
    private  static final int UPDATE_Post= 3;
    private RefreshLayout refreshLayout;
    private  String id,img,name,title,content,mainImages,nice,replyNum,time;
    private ImageView postUserImg;
    private Button button;
    private TextView  postName,postTime,postTitle,postContent,postReplyNum,postNice;
    private EditText replyadd;
    private LinearLayout linearLayout,postGood;
    private ExpandableListView listView,replyListView;
    private ImageView imageBACK;
    private SimpleDateFormat sdf;
    private int  toSOMEONE =-1;
    private String toUid,Uid,fromUId,replyContent;
    private String[] pictures;
    private List<ShowImages> listPictures = new ArrayList<>();
    private List<ReplyReturn> replyReturns;
    private Post post;
    private ServerResponse<Post> serverResponsePost;
    private ServerResponse<List<ReplyReturn>> serverResponse;
    private ServerResponse<Boolean> serverResponseReply;

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_Reply:
                    ReplyAdapter lapter = new  ReplyAdapter(replyReturns,PostInfoActivity.this);
                    replyListView.setAdapter(lapter);
                    replyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           toSOMEONE=position;
                           Log.d("xushuloong",String.valueOf(toSOMEONE));
                           replyadd.setHint("回复"+String.valueOf(replyReturns.get(position).getUserName()));
                        }
                    });
                    break;
                case INSERT_Reply:
                    replyadd.setFocusable(false);
                    replyadd.setText("");
                    Toast.makeText(PostInfoActivity.this,"回复发表成功！",Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_Post:
                    postNice.setText(String.valueOf(post.getNice()));
                    postReplyNum.setText(String.valueOf(post.getReplyNum()));
                    break;
                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);
        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        Bundle mbundle = intent.getExtras();
        id = mbundle.getString("id");
        img = mbundle.getString("img");
        name = mbundle.getString("name");
        title = mbundle.getString("title");
        content= mbundle.getString("content");
        mainImages = mbundle.getString("mainimages");
        nice = mbundle.getString("nice");
        replyNum = mbundle.getString("relyNum");
        time = mbundle.getString("time");
        fromUId = mbundle.getString("fromid");
        SharedPreferences sharedPreferences=getSharedPreferences("loginCentent", Context.MODE_PRIVATE);
        Uid=sharedPreferences.getString("uid",null);
        if(actionBar != null){
            actionBar.hide();
        }

        initPost();
        initViewID();
        Glide.with(PostInfoActivity.this)
                .load(img).into(postUserImg);
        postNice.setText(nice);
        postName.setText(name);
        postReplyNum.setText(replyNum);
        postContent.setText(content);
        postTitle.setText(title);
        postTime.setText(time);
        if(mainImages.equals("null")) {
            listView.setVisibility(View.GONE);
        }else {
            pictures = mainImages.split(";");
            for(String test  :pictures)
            {
                listPictures.add(new  ShowImages(test));
            }
            CommodityInfoDetailAdapter Ladapter =  new CommodityInfoDetailAdapter(listPictures,PostInfoActivity.this);
            listView.setAdapter(Ladapter);
        }
        imageBACK.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSOMEONE=-1;
                replyadd.setHint("");
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toSOMEONE ==-1) {
                    toUid = fromUId;
                }else {
                    toUid = String.valueOf(replyReturns.get(toSOMEONE).getReply().getFormUid());
                }
                replyContent  = replyadd.getText().toString();
                sendReply();
            }
        });
        postGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoods();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                initPost();
                getPostONE();
            }
        });
    }

    private void getPostONE() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_Post_one_HISTORY_URL;
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
                    serverResponsePost = gson.fromJson(responsedate,new TypeToken<ServerResponse<Post>>(){}.getType());
                    post = serverResponsePost.getData();
                    Message msg= Message.obtain();
                    msg.what=UPDATE_Post;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void addGoods() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.POSR_INSERT_GOOD_URL;
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
                    serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<ReplyReturn>>>(){}.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getReply() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.Get_Post_reply_URL;
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
                    serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<ReplyReturn>>>(){}.getType());
                    replyReturns = serverResponse.getData();
                    Message msg= Message.obtain();
                    msg.what=UPDATE_Reply;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initViewID() {
        postUserImg = findViewById(R.id.post_inner_img);
        postName =findViewById(R.id.post_inner_name);
        postTime =findViewById(R.id.post_inner_date);
        postTitle =findViewById(R.id.post_inner_title);
        postContent =findViewById(R.id.post_inner_cont);
        postNice =findViewById(R.id.post_inner_reply_count);
        postReplyNum =findViewById(R.id.post_inner_good_count);
        listView =findViewById(R.id.Post_inner_Plist);
        imageBACK =findViewById(R.id.imageBack);
        replyListView =findViewById(R.id.reply_list);
        replyadd = findViewById(R.id.post_inner_edit);
        linearLayout =findViewById(R.id.chose_content);
        button = findViewById(R.id.post_inner_post);
        refreshLayout =findViewById(R.id.refresh_POSTINFO);
        postGood = findViewById(R.id.post_good);
    }

    private void initPost() {
        getReply();
    }

    private void sendReply() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.POSR_Reply_INSERT_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("uid",Uid)
                            .add("toUid",toUid)
                            .add("content",replyContent)
                            .add("topicID",id)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    serverResponseReply = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<ReplyReturn>>>(){}.getType());
                    Message msg= Message.obtain();
                    msg.what=INSERT_Reply;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
