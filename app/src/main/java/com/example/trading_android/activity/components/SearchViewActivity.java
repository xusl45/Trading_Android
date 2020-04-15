package com.example.trading_android.activity.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.trading_android.R;
import com.example.trading_android.activity.LoginActivity;
import com.example.trading_android.activity.SearchCommodityActivity;
import com.example.trading_android.tableView.MainActivity;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

public class SearchViewActivity extends AppCompatActivity {
    // 1. 初始化搜索框变量
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 2. 绑定视图
        setContentView(R.layout.search_api);

        // 3. 绑定组件
        searchView = (SearchView) findViewById(R.id.search_view);

        // 4. 设置点击键盘上的搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                Bundle mBundle = new Bundle();
                mBundle.putString("name",string);
                Intent intent = new Intent(SearchViewActivity.this, SearchCommodityActivity.class);
                intent.putExtras(mBundle);
                startActivity(intent);
                finish();
            }
        });

        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }
}
