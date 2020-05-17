package com.example.trading_android.activity;

import android.annotation.SuppressLint;
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

import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.adapter.UserAddressAdapter;
import com.example.trading_android.model.UserAddress;
import com.example.trading_android.util.AddressPickTask;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonAddressChangeActivity  extends AppCompatActivity {

    private static final String TAG = "AddressChangeActivity" ;
    private  static final int UPDATE_address= 1;
    private  static final int  UPDATE_showAddress= 2;
    private  static final int  INSERT_address= 3;
    private  static final int  Delete_address= 4;
    private  String zipCode,name,phone,address,showAddress,id,uid;
    private  Boolean isADD=true;
    private EditText exName,exPhone,exZipCode,exaddress;
    private TextView showChoseAddress,addressSave;
    private Button deleteAddress;
    private ImageView imageView;
    private LinearLayout choseaddress;


    private List<UserAddress> userAddresses ;
    private ServerResponse<Boolean> serverResponseaddress;

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INSERT_address:
                    Toast.makeText(PersonAddressChangeActivity.this,"插入成功！！！",Toast.LENGTH_LONG).show();
                    break;
                case UPDATE_address:
                    Toast.makeText(PersonAddressChangeActivity.this,"更新成功！！！",Toast.LENGTH_LONG).show();
                    break;
                case Delete_address:
                    Toast.makeText(PersonAddressChangeActivity.this,"删除成功！！！",Toast.LENGTH_LONG).show();
                    break;
                case UPDATE_showAddress:
                        showChoseAddress.setText(showAddress);
                    break;
                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_address_change);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        Bundle mbundle = intent.getExtras();
        id = mbundle.getString("id");
        uid = mbundle.getString("uid");
        name = mbundle.getString("name");
        phone = mbundle.getString("Phone");
        address = mbundle.getString("address");
        zipCode = mbundle.getString("zipCode");
        Log.d(TAG ,name + phone+address+zipCode);
        initViewID();
        initPost();
        if (name !=null&&phone!=null&&address!=null&&zipCode!=null)
        {
            exName.setText(name);
            exPhone.setText(phone);
            exaddress.setText(address);
            exZipCode.setText(zipCode);
            isADD =false;
        }
        choseaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressPick();
            }
        });
        addressSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isADD!=false) {
                    name = exName.getText().toString();
                    phone = exPhone.getText().toString();
                    address =showAddress+exaddress.getText().toString();
                    zipCode =exZipCode.getText().toString();
                    insertUserAddress();
                }else {
                    name = exName.getText().toString();
                    phone = exPhone.getText().toString();
                    if (showAddress!=null) {
                        String First =showAddress;
                        String content = exaddress.getText().toString().replace(First,"");
                        address =First+content;
                    }
                    zipCode =exZipCode.getText().toString();
                    SharedPreferences sharedPreferences=getSharedPreferences("loginCentent", Context.MODE_PRIVATE);
                    uid=sharedPreferences.getString("uid",null);
                    updateUserAddress();
                }
            }
        });
        deleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isADD ==false)
                {
                    deletUserAddress();
                }

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void deletUserAddress() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.Delete_USER_ADDRESS_URL;
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
                    serverResponseaddress = gson.fromJson(responsedate,new TypeToken<ServerResponse<Boolean>>(){}.getType());
                    if (serverResponseaddress.getStatu()==200)
                    {
                        Message msg= Message.obtain();
                        msg.what=Delete_address;
                        mHandler.sendMessage(msg);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateUserAddress() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.UPDATE_USER_ADDRESS_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("id",id)
                            .add("uid",uid)
                            .add("name",name)
                            .add("phone",phone)
                            .add("zipCode",zipCode)
                            .add("address",address)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new  Gson();
                    serverResponseaddress = gson.fromJson(responsedate,new TypeToken<ServerResponse<Boolean>>(){}.getType());
                    if (serverResponseaddress.getStatu()==200)
                    {
                        Message msg= Message.obtain();
                        msg.what=UPDATE_address;
                        mHandler.sendMessage(msg);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void insertUserAddress() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.INSERT_USER_ADDRESS_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("uid",uid)
                            .add("name",name)
                            .add("phone",phone)
                            .add("zipCode",zipCode)
                            .add("address",address)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new  Gson();
                    serverResponseaddress = gson.fromJson(responsedate,new TypeToken<ServerResponse<Boolean>>(){}.getType());
                   if (serverResponseaddress.getStatu()==200)
                   {
                       Message msg= Message.obtain();
                       msg.what=INSERT_address;
                       mHandler.sendMessage(msg);
                       finish();
                   }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initPost() {

    }


    private void initViewID() {
        exName =findViewById(R.id.et_name);
        exaddress = findViewById(R.id.et_address);
        exPhone =findViewById(R.id.et_phone);
        exZipCode =findViewById(R.id.et_zipCode);
        choseaddress =findViewById(R.id.choseAddress);
        showChoseAddress = findViewById(R.id.et_showaddress);
        addressSave =findViewById(R.id.address_save);
        deleteAddress =findViewById(R.id.et_delete_address);
        imageView =findViewById(R.id.detail_back);
    }
    //地址选择器
    public void addressPick() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                Toast.makeText(PersonAddressChangeActivity.this,"数据初始化失败！！！",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    showAddress = province.getAreaName() + city.getAreaName();
                    Message msg=Message.obtain();
                    msg.what=UPDATE_showAddress;
                    mHandler.sendMessage(msg);
                    Toast.makeText(PersonAddressChangeActivity.this,province.getAreaName() + city.getAreaName(),Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PersonAddressChangeActivity.this,province.getAreaName() + city.getAreaName() + county.getAreaName(),Toast.LENGTH_LONG).show();
                    showAddress = province.getAreaName() + city.getAreaName()+county.getAreaName();
                    Message msg=Message.obtain();
                    msg.what=UPDATE_showAddress;
                    mHandler.sendMessage(msg);
                }
            }
        });
        task.execute("贵州", "毕节", "纳雍");
    }
}

