package com.example.trading_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.activity.CommodityInfoActivity;
import com.example.trading_android.model.Commodity;
import com.example.trading_android.model.CommodityStorage;

import java.util.List;

public class CommodityStorageAdapter extends BaseAdapter {
    private List<CommodityStorage> listData;
    private LayoutInflater inflater;
    private Context context;
    private int selectorPosition;
    private Bundle bundle;

    public CommodityStorageAdapter(List<CommodityStorage> listData, Context context) {
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
            convertView = inflater.inflate(R.layout.item_stroage, null);
            holder.commoditySize = convertView.findViewById(R.id.commodityStroageSize);
            holder.commodityStroagePrice = convertView.findViewById(R.id.commodityStroagePrice);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.commoditySize.setText( String.valueOf(listData.get(position).getSize()));
        holder.commodityStroagePrice.setText( String.valueOf(listData.get(position).getPrice()) );

        convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }
    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();

    }
    static class ViewHolder {
        private TextView commoditySize;
        private TextView commodityStroagePrice;
    }
}
