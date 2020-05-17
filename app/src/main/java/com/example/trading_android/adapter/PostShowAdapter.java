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
import com.example.trading_android.activity.PostActivity;
import com.example.trading_android.activity.PostInfoActivity;
import com.example.trading_android.model.Post;
import com.example.trading_android.model.PostSort;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostShowAdapter extends BaseAdapter {
    private List<Post> listData;
    private LayoutInflater inflater;
    private Context context;

    public PostShowAdapter(List<Post> listData, Context context) {
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
            convertView = inflater.inflate(R.layout.item_post, null);
            holder.postName = convertView.findViewById(R.id.post_userName);
            holder.imgUrl = convertView.findViewById(R.id.post_userImg);
            holder.postDate = convertView.findViewById(R.id.post_Date);
            holder.postTitle = convertView.findViewById(R.id.post_title);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();

        }
        holder.postName.setText(String.valueOf(listData.get(position).getUserMessage().getNickName()));

        holder.postTitle.setText(String.valueOf(listData.get(position).getTitle()));
        Date time =listData.get(position).getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        final String timeFormat = sdf.format(time);
        holder.postDate.setText(timeFormat);
        Glide.with(this.context)
                .load((String) listData.get(position).getUserMessage().getImg())
                .into(holder.imgUrl);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UserMessageActivity",String.valueOf(listData.get(position).getMainImages()));
                Bundle mBundle = new Bundle();
                mBundle.putString("id",String.valueOf(listData.get(position).getId()));
                mBundle.putString("img",String.valueOf(listData.get(position).getUserMessage().getImg()));
                mBundle.putString("name",String.valueOf(listData.get(position).getUserMessage().getNickName()));
                mBundle.putString("title",String.valueOf(listData.get(position).getTitle()));
                mBundle.putString("content",String.valueOf(listData.get(position).getContent()));
                mBundle.putString("mainimages",String.valueOf(listData.get(position).getMainImages()));
                mBundle.putString("time",timeFormat);
                mBundle.putString("fromid",String.valueOf(listData.get(position).getFromUid()));
                mBundle.putString("relyNum",String.valueOf(listData.get(position).getReplyNum()));
                mBundle.putString("nice",String.valueOf(listData.get(position).getNice()));
                Intent intent = new Intent(context, PostInfoActivity.class);
                intent.putExtras(mBundle);
                context.startActivity(intent);

            }
        });

        return convertView;
    }//

    static class ViewHolder {
        private TextView postName,postDate,postTitle;
        private ImageView imgUrl;
    }
}
