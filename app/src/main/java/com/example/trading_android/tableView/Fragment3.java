package com.example.trading_android.tableView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.model.Fram;
import com.example.trading_android.model.User;
import com.example.trading_android.model.UserMessage;
import com.example.trading_android.util.GsonUtil;
import com.example.trading_android.util.ImageTools;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Fragment3 extends Fragment {
    private static final int UPDATE_TECT = 1;
    private static final String TAG = "Fragment3";
    private Button button;
    private ImageView person_img;
    private TextView person_name;
    private LinearLayout person_message;

    private Bitmap result;
    private User user;
    private ServerResponse<User> serverResponse;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TECT:
                    Fram test2 =  (Fram) msg.obj;
                    Log.d("test12311111111", "黑白彩舍" + test2.getNickname()+test2.getResult());
                    person_img.setImageBitmap(test2.getResult());
                    person_name.setText(test2.getNickname());
                    break;
//                case 0x11:
//                    person_img.setImageBitmap((Bitmap) msg.obj);
////                    UploadHelper uploadHelper = new UploadHelper();
//                    break;
                default:
                    break;
            }
        }

        ;
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item3, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        person_img = getActivity().findViewById(R.id.person_img);
        person_name = getActivity().findViewById(R.id.person_name);
        person_message = getActivity().findViewById(R.id.menu_user);
        person_message.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"test",Toast.LENGTH_SHORT).show();
            }
        });
        initPost();
        
    }

    private void initPost() {getMessage();
    }

    private void getMessage() {
        final String urlFindMessage = URLpath.BASE_URL+URLpath.GET_USER_INFO_BY_UID_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String username = ((MainActivity) getActivity()).getUsername();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userName", username)
                            .build();
                    Request request = new Request.Builder()
                            .url(urlFindMessage)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<User>>(){}.getType());
                    user = (User) serverResponse.getData();
                    URL uil = new URL(user.getUserMessages().getImg());
                    InputStream is = uil.openStream();
                    result = BitmapFactory.decodeStream(is);
                    Fram all = new Fram(user.getUserMessages().getNickName(),result);
                    Message msg=Message.obtain();
                    msg.what=UPDATE_TECT;
                    msg.obj=all;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
