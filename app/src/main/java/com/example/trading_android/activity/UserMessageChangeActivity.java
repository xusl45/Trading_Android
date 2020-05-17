package com.example.trading_android.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trading_android.R;
import com.example.trading_android.URLpath;
import com.example.trading_android.model.Commodity;
import com.example.trading_android.model.User;
import com.example.trading_android.model.UserAddress;
import com.example.trading_android.util.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.functions.Consumer;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserMessageChangeActivity extends AppCompatActivity {
    private static final String TAG = "userChangeActivity" ;
    private  static final int UPDATE_message= 1;
    private  static final int Save_message= 2;
    private int maxSelectNum = 9;
    private List<LocalMedia> selectList = new ArrayList<>();
    private String img,nickName,sex,age,email,introduce,uid;
    private RecyclerView mRecyclerView;
    private PopupWindow pop;

    private ImageView imgSET;
    private EditText  userNickname,userSex,userAge,userIntroduce,userEmail;
    private TextView messageSave;
    private String sendImg;
    private String pictureSrc;
    private ServerResponse<String> serverResponse;
    private ServerResponse<Boolean> serverResponseMESSAGE;


    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_message:
                    pictureSrc = URLpath.BASE_IMAGES_URL+sendImg;
                    Glide.with(UserMessageChangeActivity.this)
                            .load(pictureSrc)
                            .into(imgSET);
                    break;
                case Save_message:
                    Toast.makeText(UserMessageChangeActivity.this,"修改个人成功！！！",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message_change);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        SharedPreferences sharedPreferences=getSharedPreferences("loginCentent", Context.MODE_PRIVATE);
        uid=sharedPreferences.getString("uid",null);
        final Intent intent = getIntent();
        Bundle mbundle = intent.getExtras();
        img = mbundle.getString("img");
        nickName = mbundle.getString("nickName");
        sex = mbundle.getString("sex");
        age = mbundle.getString("age");
        email = mbundle.getString("email");
        introduce = mbundle.getString("introduce");
        initViewID();
        initPost();

        userNickname.setText(nickName);
        userAge.setText(age);
        userSex.setText(sex);
        userIntroduce.setText(introduce);
        userEmail.setText(email);
        Glide.with(UserMessageChangeActivity.this)
                .load(img)
                .into(imgSET);
        imgSET.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(UserMessageChangeActivity.this,"test",Toast.LENGTH_SHORT).show();
                RxPermissions rxPermission = new RxPermissions(UserMessageChangeActivity.this);
                rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) {
                                if (permission.granted) {// 用户已经同意该权限
                                    //第一种方式，弹出选择和拍照的dialog
                                    showPop();

                                    //第二种方式，直接进入相册，但是 是有拍照得按钮的
//                                showAlbum();
                                } else {
                                    Toast.makeText(UserMessageChangeActivity.this, "拒绝", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        messageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pictureSrc!= null){
                    img =pictureSrc;
                }
                nickName = userNickname.getText().toString();
                age = userAge.getText().toString();
                sex = userSex.getText().toString();
                introduce = userIntroduce.getText().toString();
                email = userEmail.getText().toString();
                updateMessage();
            }
        });


    }

    private void updateMessage() {
        final String url = URLpath.BASE_URL+URLpath.MODIFY_USER_INFO_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("img",img)
                            .add("uid",uid)
                            .add("email",email)
                            .add("age",age)
                            .add("sex",sex)
                            .add("introduce",introduce)
                            .add("nickName",nickName)
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedate = response.body().string();
                    Gson gson = new  Gson();
                    serverResponseMESSAGE = gson.fromJson(responsedate,new TypeToken<ServerResponse<Boolean>>(){}.getType());
                    if(serverResponseMESSAGE.getStatu() ==200){
                        Message msg= Message.obtain();
                        msg.what=Save_message;
                        mHandler.sendMessage(msg);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initPost() {
    }

    private void initViewID() {
        imgSET = findViewById(R.id.tv_img);
        userEmail=(EditText) findViewById(R.id.user_email);
        userNickname=(EditText) findViewById(R.id.user_nickName);
        userSex=(EditText) findViewById(R.id.user_sex);
        userAge=(EditText) findViewById(R.id.user_age);
        userIntroduce=(EditText) findViewById(R.id.userIntroduce);
        messageSave =findViewById(R.id.message_save);
    }

    private void showPop() {
        View bottomView = View.inflate(UserMessageChangeActivity.this, R.layout.layout_bottom_dialog, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        PictureSelector.create(UserMessageChangeActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum)
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_camera:
                        //拍照
                        PictureSelector.create(UserMessageChangeActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        Log.d("xushulong",String.valueOf(resultCode));
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调

                images = PictureSelector.obtainMultipleResult(data);
                for (LocalMedia localMedia :images)
                {
                    Log.d("xushulong",String.valueOf(localMedia.getPath()));
                }
                selectList.addAll(images);
                for (LocalMedia localMedia: selectList)
                {
                    sendUploadFileRequest(localMedia.getPath());
                }

                //selectList = PictureSelector.obtainMultipleResult(data);

                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
            }
        }
    }


    //处理文件上传 -- 每次 发送一个文件
    public  void sendUploadFileRequest(String img){
        Log.d("xushulong",img);
        final String[] strs = img.split("/");
        final File file = new File(img);
        if (file.exists())
        {
            Log.d("xushulong",String.valueOf(file));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String urlFindMessage = URLpath.BASE_URL+URLpath.POST_IMG_URL;
                        OkHttpClient client = new OkHttpClient();//创建一个client
                        MediaType mediaType = MediaType.parse("application/octet-stream");//类型为八字节流
                        RequestBody requestBody = RequestBody.create(mediaType,file);//把文件与类型放入请求体
                        MultipartBody.Builder builder = new MultipartBody.Builder(); //MultiparBody的建造者
                        builder.setType(MultipartBody.FORM);
                        builder.addFormDataPart("file",file.getName(),requestBody);//设置参数名，后台接收一个“file”的文件参数
                        builder.addFormDataPart("fileName",strs[(strs.length-1)]);
                        builder.addFormDataPart("actionpart","headsculpture");//头像

                        MultipartBody multipartBody = builder.build();//建造一个请求体
                        Request request = new Request.Builder()
                                .url(urlFindMessage)//指定url
                                .post(multipartBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String responsedate = response.body().string();
                        Gson gson = new  Gson();
                        serverResponse = gson.fromJson(responsedate,new TypeToken<ServerResponse<String>>(){}.getType());
                        sendImg = (String) serverResponse.getData();
                        Log.d(TAG,sendImg+"xushulong123456");
                        Message msg= Message.obtain();
                        msg.what=UPDATE_message;
                        mHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            Log.d("xushulong","picture is not");
        }


    }
}
