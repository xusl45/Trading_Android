package com.example.trading_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.activity.PostActivity;
import com.example.trading_android.model.PostSort;
import com.example.trading_android.model.ReplyReturn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReplyAdapter extends BaseAdapter {
    private List<ReplyReturn> listData;
    private LayoutInflater inflater;
    private Context context;

    public ReplyAdapter(List<ReplyReturn> listData, Context context) {
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
                convertView = inflater.inflate(R.layout.item_reply, null);
                holder.imgUrl = convertView.findViewById(R.id.post_reply_userImg);
                holder.replyName = convertView.findViewById(R.id.post_inner_reply_name);
                holder.replyTime = convertView.findViewById(R.id.reply_date);
                holder.replyContent = convertView.findViewById(R.id.inner_cont);
                holder.replyHEIGHT = convertView.findViewById(R.id.reply_building);
                holder.replyToName = convertView.findViewById(R.id.inner_name);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();

            }
            holder.replyName.setText(String.valueOf(listData.get(position).getUserName()));
            holder.replyContent.setText(String.valueOf(listData.get(position).getReply().getContent()));
            holder.replyHEIGHT.setText((position+1)+"楼");
            if (listData.get(position).getReply().getId() != listData.get(position).getReply().getToUid())
            {
                holder.replyToName.setText("回复@"+String.valueOf(listData.get(position).getFromName()+":"));
            }else {
                holder.replyToName.setVisibility(View.GONE);
            }
            Date time =listData.get(position).getReply().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            final String timeFormat = sdf.format(time);
            holder.replyTime.setText(timeFormat);
            Glide.with(this.context)
                .load((String) listData.get(position).getImg())
                .into(holder.imgUrl);

            return convertView;
        }//

    static class ViewHolder {
    private TextView replyName,replyTime,replyContent,replyToName,replyHEIGHT;
    private ImageView imgUrl;
    }
}
