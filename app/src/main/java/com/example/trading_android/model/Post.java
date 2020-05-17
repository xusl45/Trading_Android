package com.example.trading_android.model;
import java.util.Date;

public class Post {
    private  int id;
    private  int fromUid;
    private  int commodityID;
    private  int sortID;
    private  String title;
    private  String content;
    private  String mainImages;
    private  int  nice;
    private  int replyNum;
    private  Date time;//时间
    private  String isDelete;
    private UserMessage userMessage;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", fromUid=" + fromUid +
                ", commodityID=" + commodityID +
                ", sortID=" + sortID +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", mainImages='" + mainImages + '\'' +
                ", nice=" + nice +
                ", replyNum=" + replyNum +
                ", time=" + time +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }

    public Post() {
    }

    public Post(int fromUid, int commodityID, int sortID, String title, String content, String mainImages) {
        this.fromUid = fromUid;
        this.commodityID = commodityID;
        this.sortID = sortID;
        this.title = title;
        this.content = content;
        this.mainImages = mainImages;
    }

    public Post(int fromUid, int commodityID, int sortID, String title, String content) {
        this.fromUid = fromUid;
        this.commodityID = commodityID;
        this.sortID = sortID;
        this.title = title;
        this.content = content;
    }

    public Post(int fromUid, int sortID, String title, String content) {
        this.fromUid = fromUid;
        this.sortID = sortID;
        this.title = title;
        this.content = content;
    }

    public Post(int fromUid, int sortID, String title, String content, String mainImages) {
        this.fromUid = fromUid;
        this.sortID = sortID;
        this.title = title;
        this.content = content;
        this.mainImages = mainImages;
    }

    public String getMainImages() {
        return mainImages;
    }

    public void setMainImages(String mainImages) {
        this.mainImages = mainImages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromUid() {
        return fromUid;
    }

    public void setFromUid(int fromUid) {
        this.fromUid = fromUid;
    }

    public int getCommodityID() {
        return commodityID;
    }

    public void setCommodityID(int commodityID) {
        this.commodityID = commodityID;
    }

    public int getSortID() {
        return sortID;
    }

    public void setSortID(int sortID) {
        this.sortID = sortID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getNice() {
        return nice;
    }

    public void setNice(int nice) {
        this.nice = nice;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public UserMessage getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(UserMessage userMessage) {
        this.userMessage = userMessage;
    }
}
