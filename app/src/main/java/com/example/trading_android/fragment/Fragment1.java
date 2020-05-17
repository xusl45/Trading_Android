package com.example.trading_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.GridView;
import android.widget.Toast;

import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.activity.LoginActivity;
import com.example.trading_android.activity.PersonAddressChangeActivity;
import com.example.trading_android.activity.SignUPActivity;
import com.example.trading_android.activity.UserMessageChangeActivity;
import com.example.trading_android.adapter.CommodityHotSearchAdapter;
import com.example.trading_android.adapter.CommoditySortAdapter;
import com.example.trading_android.adapter.PostSortAdapter;
import com.example.trading_android.model.CommoditySort;
import com.example.trading_android.model.PostSort;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Fragment1 extends Fragment {
    private static final int UPDATE_SORT= 1;
    private static final int UPDATE_BANNER= 2;
    private static final int UPDATE_Search= 3;
    private  GridView gridView;
    private List<PostSort> postSorts = new ArrayList<>();
    private ServerResponse<List<PostSort>> serverResponse;


    private Button button;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SORT:
                    //初始化gridview
                    PostSortAdapter mAdapter=new PostSortAdapter(postSorts, getActivity());
                    gridView.setAdapter(mAdapter);
                    gridView.setNumColumns(3);
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
        View view = inflater.inflate(R.layout.fragment_item1, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPost();
        initViewID();
    }

    private void initViewID() {
        gridView = (GridView) getActivity().findViewById(R.id.gridSortView);

    }

    private void initPost() {
        getPostSORT();
    }

    private void getPostSORT() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_POSTTYPES_URL;
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
                    serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<List<PostSort>>>(){}.getType());
                    postSorts = (List<PostSort>) serverResponse.getData();
                    Message msg=Message.obtain();
                    msg.what=UPDATE_SORT;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
