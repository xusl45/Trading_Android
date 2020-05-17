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
import com.example.trading_android.model.CommodityStorage;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommoditySellOrderAdapter  extends BaseAdapter {
    private List<CommodityStorage> listData;
    private LayoutInflater inflater;
    private Context context;

    public CommoditySellOrderAdapter(List<CommodityStorage> listData, Context context) {
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
            convertView = inflater.inflate(R.layout.item_sell_order, null);
            holder.commodityName = convertView.findViewById(R.id.commoditySell_name);
            holder.commodityImg = convertView.findViewById(R.id.commoditySell_img);
            holder.commoditySize = convertView.findViewById(R.id.commoditySell_Size);
            holder.commodityPrice = convertView.findViewById(R.id.commoditySell_price);
            holder.commodityPost = convertView.findViewById(R.id.commoditysell_delete);
            holder.commodityCentent = convertView.findViewById(R.id.commodity_centent);
            holder.commodityCreatTime =convertView.findViewById(R.id.commodity_time);
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
        Date time =listData.get(position).getSellTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeFormat = sdf.format(time);
        holder.commodityCreatTime.setText(timeFormat);
        holder.commodityPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xushulong0321", "onClick: "+"你点击了选项"+listData.get(position).toString());//bottom会覆盖item的焦点，所以要在xml里面配置android:focusable="false"
                deleteSellOrder();
            }

            private void deleteSellOrder() {
                final String urlFindMessage = URLpath.BASE_URL+URLpath.Delete_CommoditySELL_Order_URL;
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
        private TextView commodityPost;
        private TextView commodityCreatTime;
        private LinearLayout commodityCentent;
    }
}
