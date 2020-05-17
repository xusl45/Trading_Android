package com.example.trading_android.model;


public class ReplyReturn {
    private  int id;
    private  String userName;//发送者的名字
    private  String img;//发送者的头像
    private  String fromName;//接收者的名字
    private Reply reply;

    @Override
    public String toString() {
        return "ReplyReturn{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", img='" + img + '\'' +
                ", fromName='" + fromName + '\'' +
                ", reply=" + reply +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }
}
