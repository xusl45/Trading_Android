package com.example.trading_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trading_android.R;
import com.example.trading_android.activity.PersonAddressChangeActivity;
import com.example.trading_android.activity.SearchCommodityActivity;
import com.example.trading_android.model.UserAddress;

import java.util.List;

public class UserAddressAdapter extends BaseAdapter {
    private List<UserAddress> listData;
    private LayoutInflater inflater;
    private Context context;

    public UserAddressAdapter(List<UserAddress> listData, Context context) {
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
            convertView = inflater.inflate(R.layout.item_address, null);
            holder.addressName = convertView.findViewById(R.id.address_name);
            holder.addressPhone = convertView.findViewById(R.id.address_Phone);
            holder.addressPlace = convertView.findViewById(R.id.address_place);
            holder.addressChange = convertView.findViewById(R.id.address_change);
            holder.addressImage = convertView.findViewById(R.id.address_image);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();

        }
        char words[] =listData.get(position).getName().toCharArray();
        holder.addressImage.setText(String.valueOf(words[0]));
        holder.addressName.setText(listData.get(position).getName());
        holder.addressPlace.setText(listData.get(position).getAddress());
        holder.addressPhone.setText(listData.get(position).getPhone());
        holder.addressChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"许舒隆",Toast.LENGTH_SHORT).show();
                Bundle mBundle = new Bundle();
                mBundle.putString("id",String.valueOf(listData.get(position).getId()));
                mBundle.putString("name",String.valueOf(listData.get(position).getName()));
                mBundle.putString("Phone",String.valueOf(listData.get(position).getPhone()));
                mBundle.putString("address",String.valueOf(listData.get(position).getAddress()));
                mBundle.putString("zipCode",String.valueOf(listData.get(position).getZipCode()));
                Intent intent = new Intent(context, PersonAddressChangeActivity.class);
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        });
        convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {

        private TextView addressName,addressPhone,addressPlace,addressChange,addressImage;
    }
}
