package com.example.trading_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.model.ShowImages;

import java.util.List;


public class CommodityInfoDetailAdapter  extends BaseAdapter {
    private List<ShowImages> listData;
    private LayoutInflater inflater;
    private Context context;

    public CommodityInfoDetailAdapter(List<ShowImages> listData, Context context) {
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
            convertView = inflater.inflate(R.layout.item_showimages, null);
            holder.commodityImg = convertView.findViewById(R.id.commodityImgUrl);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();

        }

        Glide.with(this.context)
                .load((String) listData.get(position).getImage())
                .into(holder.commodityImg);
        convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {

        private ImageView commodityImg;
    }
}
