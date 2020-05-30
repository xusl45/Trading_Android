package com.example.trading_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.trading_android.R;

public class SplashActivity extends AppCompatActivity {

    private MyCountDownTimer mc;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }


        tv = findViewById(R.id.testview1);
        mc = new MyCountDownTimer(3000,1000);       //总的时间3000毫秒，间隔1000毫秒
        mc.start();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        },3000);            //倒计时3秒
    }

    private Handler handler = new Handler();
    /**
     * 继承CountDonTimer方法
     *
     * 重写 父类的方法 onTick() onFinish()**/

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture
         * 表示以毫秒为单位 倒计时的总数
         *
         * 例如 millisInFuture=1000 表示1秒
         *
         * @param countDownInterval
         * 表示 间隔 多少毫秒调用一次 onTick()方法
         *
         * 例如：countDownInterval=1000;表示每1000毫秒调用一次onTick()**/
        public MyCountDownTimer(long millisInFuture,long countDownInterval){
            super(millisInFuture,countDownInterval);
        }
        public void onTick(long millisUntiFinished){
            tv.setText("倒计时("+ millisUntiFinished / 1000 + ")");
        }
        public void onFinish(){
            tv.setText("正在跳转");
            finish();
        }
    }




}
