package com.example.trading_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.activity.components.SearchViewActivity;
import com.example.trading_android.adapter.CommodityHotSearchAdapter;
import com.example.trading_android.model.Commodity;
import com.example.trading_android.util.ServerResponse;
import com.example.trading_android.view.ExpandableGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchCommodityActivity extends AppCompatActivity {
    private  static final int UPDATE_Sort= 1;
    private  static final int UPDATE_Search = 2 ;
    private  static final int UPDATE_Send = 3 ;
    private  static final int UPDATE_Price = 4 ;
    public static final String TAG = "SearchCommodity";
    private RefreshLayout refreshLayout;
    private TextView textView,goBack;
    private TextView textView1;
    private TextView textView2;
    private  String name;
    private  String id;
    private String sortName;
    private SearchView searchView;
    private CommodityHotSearchAdapter hAdapter;

    private ServerResponse<List<Commodity>> serverResponseCommodity;
    //商品数据源
    private List<Commodity> commodities = new ArrayList<>();
    private ExpandableGridView gridViewCommodity;

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_Sort:
                    hAdapter=new CommodityHotSearchAdapter(commodities, SearchCommodityActivity.this);
                    gridViewCommodity.setAdapter(hAdapter);
                    gridViewCommodity.setNumColumns(2);
                    searchView.setQueryHint(sortName);
                    break;
                case  UPDATE_Search:
                    hAdapter=new CommodityHotSearchAdapter(commodities, SearchCommodityActivity.this);
                    gridViewCommodity.setAdapter(hAdapter);
                    gridViewCommodity.setNumColumns(2);
                    searchView.setQueryHint(name);
                    break;
                case UPDATE_Send:
                    Collections.sort(commodities, new Comparator<Commodity>() {
                        // 按销量排序
                        @Override
                        public int compare(Commodity p1, Commodity p2) {
                            return p1.getSellNum() == p2.getSellNum() ? 0 : (p1.getSellNum() < p2.getSellNum() ? 1 : -1);
                        }

                    });
                    hAdapter=new CommodityHotSearchAdapter(commodities, SearchCommodityActivity.this);
                    gridViewCommodity.setAdapter(hAdapter);
                    gridViewCommodity.setNumColumns(2);
                    break;
                case UPDATE_Price:
                    Collections.sort(commodities, new Comparator<Commodity>() {
                        // 按价格排序
                        @Override
                        public int compare(Commodity p1, Commodity p2) {
                            return p1.getMinPrice() == p2.getMinPrice() ? 0 : (p1.getMinPrice() > p2.getMinPrice()? 1 : -1);
                        }

                    });
                    hAdapter=new CommodityHotSearchAdapter(commodities, SearchCommodityActivity.this);
                    gridViewCommodity.setAdapter(hAdapter);
                    gridViewCommodity.setNumColumns(2);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        Bundle mbundle = intent.getExtras();
        name = mbundle.getString("name");
        id = mbundle.getString("id");
        sortName = mbundle.getString("sortName");
        initViewID();
        setSearchViewIntent();

        initPost();
        textView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Message msg=Message.obtain();
                msg.what=UPDATE_Sort;
                mHandler.sendMessage(msg);
            }
        });
        textView1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Message msg=Message.obtain();
                msg.what=UPDATE_Send;
                mHandler.sendMessage(msg);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Message msg=Message.obtain();
                msg.what=UPDATE_Price;
                mHandler.sendMessage(msg);
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                initPost();
            }
        });
    }

    private void initViewID() {
        gridViewCommodity = (ExpandableGridView) findViewById(R.id.gridviewCommmodity);
        searchView = findViewById(R.id.home_serachview);
        textView =(TextView) findViewById(R.id.commodityByOne);
        textView1 =(TextView) findViewById(R.id.commodityBysend);
        textView2 =(TextView) findViewById(R.id.commodityChoose);
        refreshLayout =findViewById(R.id.refresh_search);
        goBack = findViewById(R.id.imageBack_home);
    }

    private void initPost() {
        if (id == null && name != null)
        {
            getCommodityBySearch();
        }
        if (id !=null && name == null)
        {
            getCommodityBySort();
        }
    }

    private void getCommodityBySort() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_COMMODITY_BY_SORT_URL;
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
                    serverResponseCommodity = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<Commodity>>>(){}.getType());
                    commodities = (List<Commodity>) serverResponseCommodity.getData();
                    for (Commodity commodity:commodities)
                    {
                        Log.d(TAG,commodity.toString());
                    }
                    Message msg=Message.obtain();
                    msg.what=UPDATE_Sort;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getCommodityBySearch() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_COMMODITY_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name",name)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new  Gson();
                    serverResponseCommodity = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<Commodity>>>(){}.getType());
                    commodities = (List<Commodity>) serverResponseCommodity.getData();
                    for (Commodity commodity:commodities)
                    {
                        Log.d(TAG,commodity.toString());
                    }
                    Message msg=Message.obtain();
                    msg.what=UPDATE_Search;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //   searchView点击跳转
    private void setSearchViewIntent() {
        searchView.setFocusable(false);
        searchView.clearFocus();

        //设置搜索框焦点
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    searchView.clearFocus();
                else {
                    Intent intent = new Intent(SearchCommodityActivity.this,SearchViewActivity.class);
                    startActivityForResult(intent, 1);
//                    startActivity(intent);
                }
            }
        });
    }
}
