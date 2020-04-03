package com.example.trading_android.model;


public class ServerResponse<T> {


    //状态码
    private int statu;
    //数据
    private T data;
    //描述信息
    private String msg;

    //setter、getter方法
    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public String toString() {
        return "ServerResponse{" +
                "statu=" + statu +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}