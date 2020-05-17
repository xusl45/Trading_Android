package com.example.trading_android.model;

public class UserAddress {
    private int id;
    private int uid;
    private String name;
    private String phone;
    private String zipCode;
    private String address;
    private String isDelete;

    @Override
    public String toString() {
        return "UserAddress{" +
                "id=" + id +
                ", uid=" + uid +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }

    public UserAddress() {
    }

    public UserAddress(int uid, String name, String phone, String zipCode, String address) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.zipCode = zipCode;
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
