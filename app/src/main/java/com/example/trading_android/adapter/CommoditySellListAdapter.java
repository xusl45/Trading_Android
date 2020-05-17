package com.example.trading_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.activity.PersonCommoditySellInfoActivity;
import com.example.trading_android.model.CommodityRecord;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommoditySellListAdapter extends BaseAdapter {
    private List<CommodityRecord> listData;
    private LayoutInflater inflater;
    private Context context;

    public CommoditySellListAdapter(List<CommodityRecord> listData, Context context) {
        this.listData = listData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_trading, null);
            holder.commodityName = convertView.findViewById(R.id.commoditySell_name);
            holder.commodityImg = convertView.findViewById(R.id.commoditySell_img);
            holder.commoditySize = convertView.findViewById(R.id.commoditySell_Size);
            holder.commodityPrice = convertView.findViewById(R.id.commoditySell_price);
            holder.commodityChose = convertView.findViewById(R.id.commoditysell_chose);
            holder.commodityState = convertView.findViewById(R.id.commodity_state);
            holder.commodityPost = convertView.findViewById(R.id.commoditysell_post);
            holder.commodityCentent = convertView.findViewById(R.id.commodity_centent);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.commodityName.setText( listData.get(position).getCommodity().getName());
        holder.commodityPrice.setText( String.valueOf(listData.get(position).getPrice()));
        holder.commoditySize.setText(String.valueOf(listData.get(position).getSize())+"码");
        Glide.with(this.context)
                .load((String) listData.get(position).getCommodity().getMainImage())
                .into(holder.commodityImg);
        if (listData.get(position).getState() ==0) {
            holder.commodityState.setText("未发货");
            holder.commodityChose.setVisibility(View.VISIBLE);
            holder.commodityChose.setText("发货");
        }else if (listData.get(position).getState() ==1) {
            holder.commodityState.setText("待收货");
            holder.commodityChose.setVisibility(View.GONE);
        }else{
            holder.commodityState.setText("交易完成");
            holder.commodityChose.setVisibility(View.GONE);
        }

        holder.commodityChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xushulong0321", "onClick: "+"你点击了选项"+listData.get(position).toString());//bottom会覆盖item的焦点，所以要在xml里面配置android:focusable="false"
                sendCommodityReceiving();
            }


            private void sendCommodityReceiving() {
                final String urlFindMessage = URLpath.BASE_URL+URLpath.POSR_CommodityAFTER_URL;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("id",String.valueOf(listData.get(position).getId()))
                                    .build();
                            Request request = new Request.Builder()
                                    .url(urlFindMessage)
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responsedate = response.body().string();
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            ServerResponse<Boolean> serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<Boolean>>(){}.getType());
                            if (serverResponse.getStatu()==200)
                            {
                                listData.get(position).setState(2);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        holder.commodityCentent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xushulong0321", "onClick: "+"你点击"+listData.get(position).toString());//bottom会覆盖item的焦点，所以要在xml里面配置android:focusable="false"
                Bundle mBundle = new Bundle();
                mBundle.putString("id",String.valueOf(listData.get(position).getId()));
                Intent intent = new Intent(context, PersonCommoditySellInfoActivity.class);
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        });
        convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }


    static class ViewHolder {
        private TextView commodityName;
        private ImageView commodityImg;
        private TextView commoditySize;
        private TextView commodityPrice;
        private TextView commodityChose;
        private TextView commodityState;
        private TextView commodityPost;
        private LinearLayout commodityCentent;
    }
}