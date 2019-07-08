package com.frxs.check.model;

/**
 * Created by Endoon on 2016/4/7.
 */
public class Box {
    private String OrderId;
    private String StoreName;
    private String PickTime;

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getPickTime() {
        return PickTime;
    }

    public void setPickTime(String pickTime) {
        PickTime = pickTime;
    }
}
