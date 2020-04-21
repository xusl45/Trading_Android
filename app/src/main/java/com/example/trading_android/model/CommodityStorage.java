package com.example.trading_android.model;


import java.util.Date;

public class CommodityStorage {
    private int id;//库存序列id
    private int uid;//发售者id
    private int price;//出售价格
    private float size;//鞋码
    private Date sellTime;//出售时间
    private int commodityID;//商品id
    private String orderNumber;//订单编号
    private Commodity commodity;//商品的详细信息

    @Override
    public String toString() {
        return "CommodityStorage{" +
                "id=" + id +
                ", uid=" + uid +
                ", price=" + price +
                ", size=" + size +
                ", sellTime=" + sellTime +
                ", commodityID=" + commodityID +
                '}';
    }

    public CommodityStorage() {
    }

    public CommodityStorage(int uid, int price, float size, int commodityID) {
        this.uid = uid;
        this.price = price;
        this.size = size;
        this.commodityID = commodityID;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public Date getSellTime() {
        return sellTime;
    }

    public void setSellTime(Date sellTime) {
        this.sellTime = sellTime;
    }

    public int getCommodityID() {
        return commodityID;
    }

    public void setCommodityID(int commodityID) {
        this.commodityID = commodityID;
    }
}
