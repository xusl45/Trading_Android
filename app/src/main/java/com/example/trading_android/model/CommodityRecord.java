package com.example.trading_android.model;


import java.util.Date;

public class CommodityRecord {
    private int id;
    private int uid;
    private int bid;
    private int price;
    private int tradingPrice;
    private Date buyTime;//购买时间
    private int commodityID;
    private int addressID;
    private int storageID;
    private float size;
    private int state;
    private int pay;//支付方式
    private String orderNumber;//订单编号
    private String courierNumber;//快递单号
    private String isDelete;
    private  Commodity commodity;
    private  UserAddress address;

    @Override
    public String toString() {
        return "CommodityRecord{" +
                "id=" + id +
                ", uid=" + uid +
                ", bid=" + bid +
                ", price=" + price +
                ", tradingPrice=" + tradingPrice +
                ", buyTime=" + buyTime +
                ", commodityID=" + commodityID +
                ", size=" + size +
                ", state=" + state +
                ", pay=" + pay +
                ", courierNumber='" + courierNumber + '\'' +
                '}';
    }

    public CommodityRecord() {
    }

    public CommodityRecord(int id, String courierNumber,int state) {
        this.id = id;
        this.courierNumber = courierNumber;
        this.state = state;
    }

    public CommodityRecord(int uid, int bid, int price, int tradingPrice, int storageID, int commodityID, int addressID, float size, int pay, String orderNumber) {
        this.uid = uid;
        this.bid = bid;
        this.price = price;
        this.tradingPrice = tradingPrice;
        this.storageID = storageID;
        this.commodityID = commodityID;
        this.addressID = addressID;
        this.size = size;
        this.pay = pay;
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
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

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTradingPrice() {
        return tradingPrice;
    }

    public void setTradingPrice(int tradingPrice) {
        this.tradingPrice = tradingPrice;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public int getCommodityID() {
        return commodityID;
    }

    public void setCommodityID(int commodityID) {
        this.commodityID = commodityID;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getCourierNumber() {
        return courierNumber;
    }

    public void setCourierNumber(String courierNumber) {
        this.courierNumber = courierNumber;
    }


    public int getStorageID() {
        return storageID;
    }

    public void setStorageID(int storageID) {
        this.storageID = storageID;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
