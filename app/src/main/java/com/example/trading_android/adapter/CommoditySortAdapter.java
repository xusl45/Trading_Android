package com.example.trading_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.activity.SearchCommodityActivity;
import com.example.trading_android.model.CommoditySort;

import java.util.List;

public class CommoditySortAdapter extends BaseAdapter {
    private List<CommoditySort> listData;
    private LayoutInflater inflater;
    private Context context;

    public CommoditySortAdapter(List<CommoditySort> listData, Context context) {
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
                convertView = inflater.inflate(R.layout.item_gridview, null);
                holder.proName = convertView.findViewById(R.id.proName);
                holder.imgUrl = convertView.findViewById(R.id.imgUrl);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();

            }
            holder.proName.setText((String) listData.get(position).getSortName());
            Glide.with(this.context)
                .load((String) listData.get(position).getIconPath())
                .into(holder.imgUrl);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("xushulng", "onClick: "+"你点击了选项"+listData.get(position).getId());//bottom会覆盖item的焦点，所以要在xml里面配置android:focusable="false"
                    Bundle mBundle = new Bundle();
                    mBundle.putString("id",String.valueOf(listData.get(position).getId()));
                    Intent intent = new Intent(context, SearchCommodityActivity.class);
                    intent.putExtras(mBundle);
                    context.startActivity(intent);

                }
            });

            return convertView;
        }//

    static class ViewHolder {
    private TextView proName;
    private ImageView imgUrl;
    }
}
