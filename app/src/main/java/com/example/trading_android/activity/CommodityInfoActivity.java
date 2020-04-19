package com.example.trading_android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.adapter.CommodityHotSearchAdapter;
import com.example.trading_android.fragment.Fragment2;
import com.example.trading_android.model.Commodity;
import com.example.trading_android.model.CommodityBanner;
import com.example.trading_android.model.CommodityStorage;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommodityInfoActivity extends AppCompatActivity {
    private static final String TAG = "CommodityINFOctivity";
    private MZBannerView mMZBanner;
    private ImageView imageBACK;
    private TextView commodityName;
    private TextView commodityPrice;
    private  static final int UPDATE_Commodity= 1;
    private  String id;
    private  String[] pictures ;
    private Commodity commodities;
    private List<CommodityStorage> commodityStorages = new ArrayList<>();
    private ServerResponse<Commodity> serverResponseCommodity;


    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_Commodity:
                    pictures = commodities.getSubImages().split(";");
                    for(String test  :pictures)
                    {
                        Log.d(TAG ,test);
                    }
                    initView();
                    commodityName.setText(commodities.getName());
                    commodityPrice.setText("￥"+commodities.getMinPrice());
                    break;
                default:
                    break;
            }
        };
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_info);
        //取消头部框
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        Bundle mbundle = intent.getExtras();
        id = mbundle.getString("id");
        initViewID();
        initPost();
    }

    private void initViewID() {
        imageBACK= findViewById(R.id.imageBack);
        imageBACK.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commodityName= findViewById(R.id.commodityInfoName);
        commodityPrice = findViewById(R.id.commodityInfoPrice);
    }

    private void initPost() {
        getCommodity();
//        getCommodityStore();
    }

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
                        Log.d(TAG,commodities.toString());
                    Message msg=Message.obtain();
                    msg.what=UPDATE_Commodity;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //轮播图定义类
    public static class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            // 数据绑定
            Glide.with(context)
                    .load(data)
                    .into(mImageView);
        }
    }
    //轮播图设置数据
    private void initView() {

        mMZBanner = (MZBannerView) findViewById(R.id.commodityBanner);
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Toast.makeText(CommodityInfoActivity.this,"click page:"+position,Toast.LENGTH_LONG).show();
            }
        });
        mMZBanner.addPageChangeLisnter(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d(TAG,"----->addPageChangeLisnter:"+position + "positionOffset:"+positionOffset+ "positionOffsetPixels:"+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
//                Log.d(TAG,"addPageChangeLisnter:"+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        List<String> bannerList = new ArrayList<>();
        bannerList.add(commodities.getMainImage());
        int i = 0;
        for (String img : pictures){
            bannerList.add(img);
            i++;
            if (i == 3){break;}
        }
        mMZBanner.setIndicatorVisible(true);
        // 代码中更改indicator 的位置
        //mMZBanner.setIndicatorAlign(MZBannerView.IndicatorAlign.LEFT);
        //mMZBanner.setIndicatorPadding(10,0,0,150);
        mMZBanner.setPages(bannerList, new MZHolderCreator<Fragment2.BannerViewHolder>() {
            @Override
            public Fragment2.BannerViewHolder createViewHolder() {
                return new Fragment2.BannerViewHolder();
            }
        });
    }
}
