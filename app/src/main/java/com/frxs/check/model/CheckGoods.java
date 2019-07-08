package com.frxs.check.model;

import java.io.Serializable;

/**
 * Created by Endoon on 2016/5/10.
 */
public class CheckGoods implements Serializable{
    private String ID;
    private String OrderID;//": "105000000292",
    private String WID;//: 105,
    private String Package1Qty;//: null,
    private String Package2Qty;//: null,
    private String Package3Qty;//: null,
    private String Remark;//: 0,
    private String ShelfAreaID;//: 1,
    private String ShelfAreaCode;//: "02",
    private String ShelfAreaName;//: "大货区",
    private String PickUserID;//: null,
    private String PickUserName;//: null,
    private String BeginTime;//: "2016-05-05 14:10:37.830000",
    private String EndTime;//: "2016-05-05 14:16:25.767000",
    private String ModifyTime;//: "2016-05-05 14:10:04.737000",
    private String ModifyUserID;//: 69,
    private String ModifyUserName;//: "星沙物流-张丽",
    private String CheckTime;//: null,
    private String CheckUserID;//: null,
    private String CheckUserName;//: null

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getWID() {
        return WID;
    }

    public void setWID(String WID) {
        this.WID = WID;
    }

    public String getPackage1Qty() {
        return Package1Qty;
    }

    public void setPackage1Qty(String package1Qty) {
        Package1Qty = package1Qty;
    }

    public String getPackage2Qty() {
        return Package2Qty;
    }

    public void setPackage2Qty(String package2Qty) {
        Package2Qty = package2Qty;
    }

    public String getPackage3Qty() {
        return Package3Qty;
    }

    public void setPackage3Qty(String package3Qty) {
        Package3Qty = package3Qty;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getShelfAreaID() {
        return ShelfAreaID;
    }

    public void setShelfAreaID(String shelfAreaID) {
        ShelfAreaID = shelfAreaID;
    }

    public String getShelfAreaCode() {
        return ShelfAreaCode;
    }

    public void setShelfAreaCode(String shelfAreaCode) {
        ShelfAreaCode = shelfAreaCode;
    }

    public String getShelfAreaName() {
        return ShelfAreaName;
    }

    public void setShelfAreaName(String shelfAreaName) {
        ShelfAreaName = shelfAreaName;
    }

    public String getPickUserID() {
        return PickUserID;
    }

    public void setPickUserID(String pickUserID) {
        PickUserID = pickUserID;
    }

    public String getPickUserName() {
        return PickUserName;
    }

    public void setPickUserName(String pickUserName) {
        PickUserName = pickUserName;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getModifyTime() {
        return ModifyTime;
    }

    public void setModifyTime(String modifyTime) {
        ModifyTime = modifyTime;
    }

    public String getModifyUserID() {
        return ModifyUserID;
    }

    public void setModifyUserID(String modifyUserID) {
        ModifyUserID = modifyUserID;
    }

    public String getModifyUserName() {
        return ModifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        ModifyUserName = modifyUserName;
    }

    public String getCheckTime() {
        return CheckTime;
    }

    public void setCheckTime(String checkTime) {
        CheckTime = checkTime;
    }

    public String getCheckUserID() {
        return CheckUserID;
    }

    public void setCheckUserID(String checkUserID) {
        CheckUserID = checkUserID;
    }

    public String getCheckUserName() {
        return CheckUserName;
    }

    public void setCheckUserName(String checkUserName) {
        CheckUserName = checkUserName;
    }
}
