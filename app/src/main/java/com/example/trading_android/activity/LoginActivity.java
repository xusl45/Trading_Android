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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.util.ServerResponse;
import com.example.trading_android.model.User;
import com.example.trading_android.fragment.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private  static final int UPDATE_TECT = 1 ;
    private  static final int Tent_TECT = 2 ;
    private static final String TAG = "LoginActivity";
    private Button buttonLogin;
    private TextView textView;
    private EditText accounttext;     //输入账号框的信息
    private String account;//账号
    private EditText pwdtext;         //输入密码框的信息
    private String pwd;//密码
    private String uid;
    private ServerResponse<User> serverResponse;
    private  User user;


    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TECT:
                    Toast.makeText(LoginActivity.this,"登录成功！！！",Toast.LENGTH_LONG).show();
                    break;
                case   Tent_TECT:
                    Toast.makeText(LoginActivity.this,"账号不存在或密码错误！！！",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        accounttext = findViewById(R.id.input_mobile);
        pwdtext = findViewById(R.id.input_password);

        SharedPreferences sharedPreferences=getSharedPreferences("loginCentent", Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("name",null);
        String password=sharedPreferences.getString("password",null);
        Log.d(TAG,name+password +"xushulong");
        if (name !=null &&password !=null)
        {
            account = name;
            pwd = password;
            sendRequestwithOKhttp();
        }
        buttonLogin = findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                account = accounttext.getText().toString();
                pwd = pwdtext.getText().toString();
                sendRequestwithOKhttp();
            }
        });

        textView = findViewById(R.id.link_signup);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUPActivity.class);
                startActivity(intent);

            }
        });

    };

    private void sendRequestwithOKhttp() {
        final String urlLogin = URLpath.BASE_URL+URLpath.LOGIN_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userName",account)
                            .add("password",pwd)
                            .build();
                    Request request =new Request.Builder()
                            .url(urlLogin)
                            .post(requestBody)
                            .build();
                    Response response =client.newCall(request).execute();
                    String  responsedate = response.body().string();
                    Log.d(TAG,responsedate);
                    parseJSNOWITHgson(responsedate);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void parseJSNOWITHgson(String responsedate) {
        Gson gson = new  Gson();
        serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<User>>(){}.getType());
        Message message = new Message();
        if(serverResponse.getStatu() ==200){
            user = (User) serverResponse.getData();
            message.what = UPDATE_TECT;
            mHandler.sendMessage(message);
            uid = String.valueOf(user.getId());

            String value = String.valueOf(user.getUsername());
            Bundle mBundle = new Bundle();
            mBundle.putString("username",value);
            //保存登录状态
            loginCentent();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtras(mBundle);
            startActivity(intent);
            finish();
        }else{
            message.what = Tent_TECT;
            mHandler.sendMessage(message);
        }
    }

    private void loginCentent() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginCentent", Context.MODE_PRIVATE); //私有数据

        SharedPreferences.Editor editor = sharedPreferences.edit(); //获取编辑器
        editor.putString("uid",uid);
        editor.putString("name", account);
        editor.putString("password",pwd);
        editor.commit();

    }

}