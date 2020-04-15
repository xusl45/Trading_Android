package com.example.trading_android.model;

import android.graphics.Bitmap;

public class Fram {
    public String nickname;
    public Bitmap result;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Bitmap getResult() {
        return result;
    }

    public void setResult(Bitmap result) {
        this.result = result;
    }

    public Fram(String nickname, Bitmap result) {
        this.nickname = nickname;
        this.result = result;
    }
}
