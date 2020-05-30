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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.adapter.CommodityStorageAdapter;
import com.example.trading_android.adapter.UserAddressAdapter;
import com.example.trading_android.model.UserAddress;
import com.example.trading_android.util.ServerResponse;
import com.example.trading_android.view.AddressDialog;
import com.example.trading_android.view.CustomDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommodityBuyACtivity   extends AppCompatActivity {
    private static final String TAG = "COMMODITYBUYActivity" ;
    private  static final int UPDATE_address= 1;
    private  static final int Save_ADDRESS= 2;
    private  static final int UPDATE_BUY= 3;
    private String bid,uid,size,price,commodityID,commodityStorageID,img,name,addressID ,orderNum;
    private TextView commodityName,commoditySize,commodityPrice,finalPrice;
    private TextView addressName,addressPhone,addressPlace,addressImage;
    private TextView commodityBuy;
    private AlertDialog.Builder builder;
    private ImageView imageBACK,commodityIMG;
    private LinearLayout linearLayout;
    private AddressDialog dialog;
    private UserAddress setUserADDress;
    private List<UserAddress> userAddresses ;
    private ServerResponse<List<UserAddress>> serverResponseaddress;
    private ServerResponse<Boolean> serverResponseBuy;


    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_address:
                    addressID=String.valueOf(userAddresses.get(0).getId());
                    char words[] =userAddresses.get(0).getName().toCharArray();
                    addressName.setText(userAddresses.get(0).getName());
                    addressPhone.setText(userAddresses.get(0).getPhone());
                    addressPlace.setText(userAddresses.get(0).getAddress());
                    addressImage.setText(String.valueOf(words[0]));
                    break;
                case Save_ADDRESS:
                    char Title[] =setUserADDress.getName().toCharArray();
                    addressName.setText(setUserADDress.getName());
                    addressPhone.setText(setUserADDress.getPhone());
                    addressPlace.setText(setUserADDress.getAddress());
                    addressImage.setText(String.valueOf(Title[0]));
                    break;
                case UPDATE_BUY:
                    Toast.makeText(CommodityBuyACtivity.this, "购买商品成功！！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_buy);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        SharedPreferences sharedPreferences=getSharedPreferences("loginCentent", Context.MODE_PRIVATE);
        bid=sharedPreferences.getString("uid",null);
        final Intent intent = getIntent();
        Bundle mbundle = intent.getExtras();
        size = mbundle.getString("size");
        price = mbundle.getString("price");
        commodityID = mbundle.getString("commodityID");
        commodityStorageID = mbundle.getString("commodityStorageID");
        uid = mbundle.getString("uid");
        img = mbundle.getString("img");
        name = mbundle.getString("name");
        orderNum = mbundle.getString("ordernum");
        Log.d(TAG,uid+size+price+commodityID +commodityStorageID);
        initViewID();
        initPost();
        Glide.with(CommodityBuyACtivity.this)
                .load(img)
                .into(commodityIMG);
        commoditySize.setText(Double.parseDouble(size)+"码");
        commodityPrice.setText("￥"+price);
        commodityName.setText(name);
        finalPrice.setText("￥"+(int)(Integer.parseInt(price)+Integer.parseInt(price)*0.05+23));
        imageBACK.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userAddresses.size() != 0) {
                    Dialog();
                }else {
                    Toast.makeText(CommodityBuyACtivity.this ,"请设置地址",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CommodityBuyACtivity.this,PersonAddressActivity.class);
                    startActivity(intent);
                }
            }
        });
        commodityBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyCommodityDialog();
            }
        });

    }

    private void buyCommodityDialog() {
        builder = new AlertDialog.Builder(this).setIcon(R.drawable.cutecat).setTitle("订单")
                .setMessage("你确定要购买？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        BuyCommodity();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        Toast.makeText(CommodityBuyACtivity.this, "关闭按钮", Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void Dialog() {
        dialog = new AddressDialog(CommodityBuyACtivity.this,R.style.Dialog);//设置dialog的样式
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
        ListView dialog_ListView = (ListView) dialog.findViewById(R.id.address_list2);
        UserAddressAdapter Ladapter =  new  UserAddressAdapter(userAddresses,CommodityBuyACtivity.this);
        dialog_ListView.setAdapter(Ladapter);
        dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"XUSHULONG " +position);
                addressID = String.valueOf(userAddresses.get(position).getId());
                setUserADDress = userAddresses.get(position);
                PayMent();
                dialog.dismiss();
            }
        });
    }

    public void PayMent(){
        Message msg=Message.obtain();
        msg.what=Save_ADDRESS;
        mHandler.sendMessage(msg);

    }
    private void initPost() {
        getallAddress();
    }

    private void BuyCommodity() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.POST_BuyCOMMODITY_URL;
        final int cententPrice = (int) (Integer.parseInt(price)+Integer.parseInt(price)*0.05+23);
        final String tradingPrice = String.valueOf(cententPrice);
        final int centenSize = (int) Float.parseFloat(size);
        final String finalSize = String.valueOf(centenSize);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("uid",uid)
                            .add("bid",bid)
                            .add("price",price)
                            .add("tradingPrice",tradingPrice)
                            .add("commodityID",commodityID)
                            .add("storageID",commodityStorageID)
                            .add("addressID",addressID)
                            .add("size",finalSize)
                            .add("pay","1")
                            .add("orderNumber",orderNum )
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new  Gson();
                    serverResponseBuy = gson.fromJson(responsedate,new TypeToken<ServerResponse<Boolean>>(){}.getType());
                    if (serverResponseBuy.getStatu() ==200){
                        Message msg= Message.obtain();
                        msg.what=UPDATE_BUY;
                        mHandler.sendMessage(msg);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getallAddress() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_USER_ADDRESS_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("uid",bid)
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
                    if (userAddresses.size()!=0){
                        Message msg= Message.obtain();
                        msg.what=UPDATE_address;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initViewID() {
        imageBACK= findViewById(R.id.imageBack);
        commodityIMG =findViewById(R.id.commoditySell_img);
        commodityName =findViewById(R.id.commoditySell_name);
        commodityPrice =findViewById(R.id.commoditySell_price);
        commoditySize =findViewById(R.id.commoditySell_Size);
        addressName = findViewById(R.id.address_name);
        addressPhone = findViewById(R.id.address_Phone);
        addressPlace = findViewById(R.id.address_place);
        addressImage = findViewById(R.id.address_image);
        linearLayout = findViewById(R.id.change_address);
        finalPrice =findViewById(R.id.final_price);
        commodityBuy =findViewById(R.id.commodity_buy);
    }
}
