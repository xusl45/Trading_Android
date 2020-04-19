package com.example.trading_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.model.Commodity;

import java.util.List;

import static com.example.trading_android.fragment.Fragment2.TAG;

public class CommodityHotSearchAdapter extends BaseAdapter {
    private List<Commodity> listData;
    private LayoutInflater inflater;
    private Context context;

    public CommodityHotSearchAdapter(List<Commodity> listData, Context context) {
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
            convertView = inflater.inflate(R.layout.item_commodity, null);
            holder.commodityName = convertView.findViewById(R.id.commodityName);
            holder.commodityImg = convertView.findViewById(R.id.commodityUrl);
            holder.minPrice = convertView.findViewById(R.id.commodityPrice);
            holder.sendNum = convertView.findViewById(R.id.commodityNum);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.commodityName.setText( listData.get(position).getName());
        holder.sendNum.setText( listData.get(position).getSellNum() +"人付款");
        holder.minPrice.setText( "￥"+listData.get(position).getMinPrice());

        Glide.with(this.context)
                .load((String) listData.get(position).getMainImage())
                .into(holder.commodityImg);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+"你点击了选项"+listData.get(position).getName());//bottom会覆盖item的焦点，所以要在xml里面配置android:focusable="false"
            }
        });
        convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {
        private TextView commodityName;
        private ImageView commodityImg;
        private TextView minPrice;
        private TextView sendNum;
    }
}
