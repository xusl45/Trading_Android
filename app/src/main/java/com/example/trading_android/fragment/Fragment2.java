package com.example.trading_android.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.activity.components.SearchViewActivity;
import com.example.trading_android.adapter.CommodityHotSearchAdapter;
import com.example.trading_android.adapter.CommoditySortAdapter;
import com.example.trading_android.model.Commodity;
import com.example.trading_android.model.CommodityBanner;
import com.example.trading_android.model.CommoditySort;
import com.example.trading_android.view.ExpandableGridView;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Fragment2 extends Fragment {
    private static final int UPDATE_SORT= 1;
    private static final int UPDATE_BANNER= 2;
    private static final int UPDATE_Search= 3;
    public static final String TAG = "MZModeBannerFragment";
    private RefreshLayout refreshLayout;
    private MZBannerView mMZBanner;
    private SearchView searchView;
    private ServerResponse<List<CommoditySort>> serverResponse;
    private ServerResponse<List<CommodityBanner>>  serverResponseBanner;
    private ServerResponse<List<Commodity>> serverResponseCommodity;
    //商品数据源
    private List<Commodity> commodities = new ArrayList<>();
    private List<CommoditySort> commoditySorts = new ArrayList<>();
    private List<CommodityBanner> commodityBanners = new ArrayList<>();
    private  GridView gridView;
    private ExpandableGridView gridViewCommodity;
    /**
     * 下拉刷新的layout
     */
    private SwipeRefreshLayout mRefreshLayout;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SORT:
                    //初始化gridview
                    CommoditySortAdapter mAdapter=new CommoditySortAdapter(commoditySorts, getActivity());
                    gridView.setAdapter(mAdapter);
                    gridView.setNumColumns(4);
                    break;
                case UPDATE_BANNER:
                    //初始化gridview
                    initView(getView());
                    break;
                case UPDATE_Search:
                    CommodityHotSearchAdapter hAdapter=new CommodityHotSearchAdapter(commodities, getActivity());
                    gridViewCommodity.setAdapter(hAdapter);
                    gridViewCommodity.setNumColumns(2);
                    break;
                default:
                    break;
            }
        }

        ;
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item2, null);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridViewCommodity = (ExpandableGridView) view.findViewById(R.id.gridviewCommmodity);
        refreshLayout = view.findViewById(R.id.refresh_homepage);
        initPost();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                initPost();
            }
        });
        searchView =  view.findViewById(R.id.home_serachview);
        setSearchViewIntent();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void initPost() {
        getBanners();
        getCommodityType();
        getCommodity();

    }

    private void getCommodity() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_COMMODITY_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String username = ((MainActivity) getActivity()).getUsername();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name","詹姆斯")
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

    private void getBanners() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.BASE_ADS_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String username = ((MainActivity) getActivity()).getUsername();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new  Gson();
                    serverResponseBanner = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<CommodityBanner>>>(){}.getType());
                    commodityBanners = (List<CommodityBanner>) serverResponseBanner.getData();
                    for (CommodityBanner commoditySort:commodityBanners)
                    {
                        Log.d(TAG,commoditySort.toString());
                    }
                    Message msg=Message.obtain();
                    msg.what=UPDATE_BANNER;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getCommodityType() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_COMMODITY_TYPES_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String username = ((MainActivity) getActivity()).getUsername();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new  Gson();
                    serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<CommoditySort>>>(){}.getType());
                    commoditySorts = (List<CommoditySort>) serverResponse.getData();
//                    for (CommoditySort commoditySort:commoditySorts)
//                    {
//                        Log.d(TAG,commoditySort.toString());
//                    }
                    Message msg=Message.obtain();
                    msg.what=UPDATE_SORT;
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
    private void initView(View view) {

        mMZBanner = (MZBannerView) view.findViewById(R.id.banner);
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Toast.makeText(getContext(),"click page:"+position,Toast.LENGTH_LONG).show();
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
        for (CommodityBanner commodityBanner :commodityBanners)
        {
            bannerList.add(commodityBanner.getImgPath());
        }
        mMZBanner.setIndicatorVisible(true);
        // 代码中更改indicator 的位置
        //mMZBanner.setIndicatorAlign(MZBannerView.IndicatorAlign.LEFT);
        //mMZBanner.setIndicatorPadding(10,0,0,150);
        mMZBanner.setPages(bannerList, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
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
                    Intent intent = new Intent(getActivity(), SearchViewActivity.class);
//                    startActivityForResult(intent, 2);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
//        mMZBanner.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
//        mMZBanner.start();//开始轮播
    }

}
