package com.example.trading_android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.adapter.CommodityHotSearchAdapter;
import com.example.trading_android.adapter.CommodityInfoDetailAdapter;
import com.example.trading_android.adapter.CommoditySortAdapter;
import com.example.trading_android.adapter.CommodityStorageAdapter;
import com.example.trading_android.fragment.Fragment2;
import com.example.trading_android.model.Commodity;
import com.example.trading_android.model.CommodityBanner;
import com.example.trading_android.model.CommoditySort;
import com.example.trading_android.model.CommodityStorage;
import com.example.trading_android.model.ShowImages;
import com.example.trading_android.model.User;
import com.example.trading_android.util.ServerResponse;
import com.example.trading_android.view.CustomDialog;
import com.example.trading_android.view.ExpandableListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private  static final int UPDATE_Commodity= 1;
    private final int  UPDATE_Size= 2;
    private  static final int UPDATE_Parameter= 3;
    private  static final int ALERT_ADDsize= 4;
    private RefreshLayout refreshLayout;
    private MZBannerView mMZBanner;
    private ImageView imageBACK;
    private TextView commodityName,commodityPrice,choseButton,dialog_goods_price;
    private TextView sortName,commodityType,sendPrice,commodityDetail,commoditySell;
    private  TextView addBuy;
    private CustomDialog dialog;
    private LinearLayout showCommodity;
    private ListView listView;
    private String id ;
    private String[] pictures ;
    private List<ShowImages> listPictures = new ArrayList<>();
    private CommodityStorage commodityStorageID;
    private Commodity commodities;
    private CommoditySort commoditiesSort;
    private List<CommodityStorage> commodityStorages ;
    private ServerResponse<Commodity> serverResponseCommodity;
    private ServerResponse<CommoditySort> serverResponseCommoditySort;
    private ServerResponse<List<CommodityStorage>> serverResponseCommodityStorage;
    private ImageView dialog_img;



    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_Commodity:
                    pictures = commodities.getSubImages().split(";");
                    for(String test  :pictures)
                    {
                        Log.d(TAG,test);
                        listPictures.add(new  ShowImages(test));
                    }
                    for(int i =0;i<listPictures.size();i++)
                    {
                        Log.d(TAG,listPictures.get(i).getImage());
                    }
                    initView();
                    commodityName.setText(commodities.getName());
                    commodityPrice.setText("￥"+commodities.getMinPrice());
                    CommodityInfoDetailAdapter Ladapter =  new CommodityInfoDetailAdapter(listPictures,CommodityInfoActivity.this);
                    listView = (ListView) findViewById(R.id.images_list);
                    listView.setAdapter(Ladapter);
                    break;
                case UPDATE_Size:
                    Bundle bundle = (Bundle) msg.obj;
//                    Log.d(TAG,commodityStorageID);
                    choseButton.setText("已选：" +bundle.getString("str"));
                    break;
                case UPDATE_Parameter:
                    sortName.setText(commoditiesSort.getSortName());
                    sendPrice.setText(String.valueOf(commodities.getSendPrice()));
                    commodityType.setText(commodities.getType());
                    commodityDetail.setText(commodities.getDetail());
                    break;
                case ALERT_ADDsize:
                    Toast.makeText(CommodityInfoActivity.this,"请选择你要购买的尺码！！！",Toast.LENGTH_LONG).show();
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
        imageBACK.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        showCommodity.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (commodityStorages != null) {
                    Dialog();
                }else {
                    Toast.makeText(CommodityInfoActivity.this ,"无预售商品及尺码",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commoditySell.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Bundle mBundle = new Bundle();
                mBundle.putString("id",id);
                Intent intent = new Intent(CommodityInfoActivity.this, CommoditySellActivity.class);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                initPost();
            }
        });
        addBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commodityStorageID !=null)
                {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("commodityID",String.valueOf(commodityStorageID.getCommodityID()));
                    mBundle.putString("price",String.valueOf(commodityStorageID.getPrice()));
                    mBundle.putString("uid",String.valueOf(commodityStorageID.getUid()));
                    mBundle.putString("size",String.valueOf(commodityStorageID.getSize()));
                    mBundle.putString("commodityStorageID",String.valueOf(commodityStorageID.getId()));
                    mBundle.putString("ordernum",String.valueOf(commodityStorageID.getOrderNumber()));
                    mBundle.putString("img",String.valueOf(commodities.getMainImage()));
                    mBundle.putString("name",String.valueOf(commodities.getName()));
                    Intent intent = new Intent(CommodityInfoActivity.this, CommodityBuyACtivity.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }else {
                    Message msg=Message.obtain();
                    msg.what=ALERT_ADDsize;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }
    //选择尺码的弹窗
    private void Dialog() {
        dialog = new CustomDialog(CommodityInfoActivity.this,R.style.Dialog);//设置dialog的样式
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.mystyle); // 添加动画
        dialog.show();
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams lp = window.getAttributes();
        //这句就是设置dialog横向满屏了。
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = (int) (dm.heightPixels*0.7);     //dialog屏幕占比
        window.setAttributes(lp);

        GridView dialog_gridView = (GridView) dialog.findViewById(R.id.gridviewDialog);
        RelativeLayout custom_dialog_close = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_close);
        TextView dialog_goods_name = (TextView) dialog.findViewById(R.id.dialog_goods_name);
        dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
        dialog_goods_price = (TextView) dialog.findViewById(R.id.dialog_goods_price);
        //涉资dialog里面内容
        custom_dialog_close.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog_goods_name.setText(commodities.getName());
        dialog_goods_price.setText("￥"+String.valueOf(commodities.getMinPrice()));
        Glide.with(this)
                .load(commodities.getMainImage())
                .into(dialog_img );

        //adater
         CommodityStorageAdapter mAdapter=new CommodityStorageAdapter(commodityStorages,CommodityInfoActivity.this);
        dialog_gridView.setAdapter(mAdapter);
        dialog_gridView.setNumColumns(4);
        dialog_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"XUSHULONG " +position);
                commodityStorageID = commodityStorages.get(position);
                PayMent(String.valueOf(commodityStorages.get(position).getSize()));
                dialog.dismiss();
            }
        });
    }
    public void PayMent(String str){
        Message msg=Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("str",str);
        msg.obj =bundle;
        msg.what=UPDATE_Size;
        mHandler.sendMessage(msg);

    }
    //绑定组件
    private void initViewID() {
        imageBACK= findViewById(R.id.imageBack);
        commodityName= findViewById(R.id.commodityInfoName);
        commodityPrice = findViewById(R.id.commodityInfoPrice);
        showCommodity = (LinearLayout) findViewById(R.id.showCommodity);
        choseButton  = findViewById(R.id.choseButton);
        sortName = findViewById(R.id.sortName);
        commodityType = findViewById(R.id.commodityType);
        sendPrice = findViewById(R.id.sendPrice);
        commodityDetail = findViewById(R.id.commodityDetail);
        commoditySell = findViewById(R.id.commodity_sell);
        refreshLayout =findViewById(R.id.refresh_commodityINFO);
        addBuy =findViewById(R.id.btn_add_shopping);
    }
    //获取信息
    private void initPost() {
        getCommodity();
        getCommodityStore();
        getCommodityParameter();
    }
    //获取商品参数
    private void getCommodityParameter() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_COMMODITYSORT_BY_ID_URL;
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
                    serverResponseCommoditySort = gson.fromJson(responsedate,new TypeToken<ServerResponse<CommoditySort>>(){}.getType());
                    commoditiesSort = (CommoditySort) serverResponseCommoditySort.getData();
                    Log.d(TAG,commoditiesSort.toString());
                    Message msg=Message.obtain();
                    msg.what=UPDATE_Parameter;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //商品预售
    private void getCommodityStore() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_COMMODITYSTORAGE_BY_ID_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("commodityId",id)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    serverResponseCommodityStorage = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<CommodityStorage>>>(){}.getType());
                    commodityStorages = (List<CommodityStorage>) serverResponseCommodityStorage.getData();
                    for (CommodityStorage commodityStorage :commodityStorages){
                        Log.d(TAG,commodityStorage.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
