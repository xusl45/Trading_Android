package com.example.trading_android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.model.CommoditySort;

import java.util.List;

import static com.example.trading_android.tableView.Fragment2.TAG;

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
                    Log.d(TAG, "onClick: "+"你点击了选项"+listData.get(position).getSortName());//bottom会覆盖item的焦点，所以要在xml里面配置android:focusable="false"
                }
            });

            return convertView;
        }//

    static class ViewHolder {
    private TextView proName;
    private ImageView imgUrl;
    }
}
