package com.example.trading_android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.util.ServerResponse;
import com.example.trading_android.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUPActivity extends AppCompatActivity {
    private  static final int UPDATE_TECT = 1 ;
    private  static final int Tent_TECT = 2 ;
    private  static final int No_True =3;
    private static final String TAG = "SIGNActivity";
    private Button buttonSignUp;
    private TextView textView;
    private EditText accounttext;     //输入账号框的信息
    private String account;
    private EditText pwdtext;         //输入密码框的信息
    private String pwd;
    private EditText Npwdtext;         //输入确认密码框的信息
    private String Npwd;
    private ServerResponse serverResponse;
    private  User user;


    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TECT:
                    Toast.makeText(SignUPActivity.this,"注册成功！！！",Toast.LENGTH_LONG).show();
                    accounttext.invalidate();
                    break;
                case   Tent_TECT:
                    Toast.makeText(SignUPActivity.this,"用户名已存在请更换账号！！！",Toast.LENGTH_LONG).show();
                    accounttext.invalidate();
                    break;
                case  No_True:
                    Toast.makeText(SignUPActivity.this,"两次输入的密码不同",Toast.LENGTH_LONG).show();
                    accounttext.invalidate();
                    break;
                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        buttonSignUp = findViewById(R.id.btn_signup);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Message message = new Message();
                accounttext = findViewById(R.id.input_mobile);
                account = accounttext.getText().toString();
                pwdtext = findViewById(R.id.input_password);
                pwd = pwdtext.getText().toString();
                Npwdtext = findViewById(R.id.input_verify);
                Npwd = Npwdtext.getText().toString();
                if (pwd.equals(Npwd)) {
                    if(account!=null) {
                            sendRequestwithOKhttp();
                    }
                }
                else{
                    message.what = No_True;
                    mHandler.sendMessage(message);
                }
            }
        });

        textView = findViewById(R.id.link_login);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();

            }
        });

    };
    private void sendRequestwithOKhttp() {
        final String urlSignUp = URLpath.BASE_URL+URLpath.REGISTER_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userName",account)
                            .add("password",pwd )
                            .build();
                    Request request =new Request.Builder()
                            .url(urlSignUp)
                            .post(requestBody)
                            .build();
                    Response response =client.newCall(request).execute();
                    String  responsedate = response.body().string();
                    Gson gson = new  Gson();
                    serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse>(){}.getType());
                    Log.d(TAG ,serverResponse.toString());
                    Message message = new Message();
                    if(serverResponse.getStatu() ==200){
                        message.what = UPDATE_TECT;
                        mHandler.sendMessage(message);
                        finish();
                    }else {
                        message.what = Tent_TECT;
                        mHandler.sendMessage(message);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
