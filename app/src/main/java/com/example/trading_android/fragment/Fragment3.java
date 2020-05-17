package com.example.trading_android.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.activity.LoginActivity;
import com.example.trading_android.activity.PersonCommodityBuyActivity;
import com.example.trading_android.activity.PersonCommoditySellActivity;
import com.example.trading_android.activity.PersonMessageActivity;
import com.example.trading_android.activity.PersonSellOrederActivity;
import com.example.trading_android.model.User;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Fragment3 extends Fragment {
    private static final int UPDATE_ALL = 1;
    private static final int UPDATE_NAME = 2;
    private static final String TAG = "Fragment3";
    private Button button;
    private ImageView person_img;
    private TextView person_name;
    private LinearLayout person_message,person_Buyed,person_Selled;
    private RelativeLayout person_title;

    private Bitmap result;
    private User user;
    private ServerResponse<User> serverResponse;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_ALL:
                    person_img.setImageBitmap(result);
                    person_name.setText(user.getUserMessages().getNickName());
                    Log.d("test123123",user.getUserMessages().getNickName());
                    break;
                case UPDATE_NAME:
                    person_name.setText(user.getUserMessages().getNickName());
                    Log.d("test123123",user.getUserMessages().getNickName());
                    break;
//                case 0x11:
//                    person_img.setImageBitmap((Bitmap) msg.obj);
////                    UploadHelper uploadHelper = new UploadHelper();
//                    break;
                default:
                    break;
            }
        }

        ;
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item3, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewID();
        initPost();
        person_message.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getActivity(), PersonMessageActivity.class);
                startActivity(intent);
            }
        });
        person_Selled.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getActivity(), PersonSellOrederActivity.class);
                startActivity(intent);
            }
        });
        person_Buyed.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getActivity(), PersonCommodityBuyActivity.class);
                startActivity(intent);
            }
        });
    }

        private void initViewID() {
        person_img = getActivity().findViewById(R.id.person_img);
        person_name = getActivity().findViewById(R.id.person_name);
        person_Buyed = getActivity().findViewById(R.id.menu_bought);
        person_Selled = getActivity().findViewById(R.id.menu_sell);
        person_message = getActivity().findViewById(R.id.menu_user);
        person_title = getActivity().findViewById(R.id.person_rela);
    }

    private void initPost() {getMessage();
    }

    private void getMessage() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_USER_INFO_BY_UID_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String username = ((MainActivity) getActivity()).getUsername();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userName", username)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<User>>(){}.getType());
                    user = (User) serverResponse.getData();
                    if(user.getUserMessages().getImg() != null)
                    {
                        URL uil = new URL(user.getUserMessages().getImg());
                        InputStream is = uil.openStream();
                        result = BitmapFactory.decodeStream(is);
                        Message msg=Message.obtain();
                        msg.what=UPDATE_ALL;
                        mHandler.sendMessage(msg);
                    }else {
                        Message msg=Message.obtain();
                        msg.what=UPDATE_NAME;
                        mHandler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
