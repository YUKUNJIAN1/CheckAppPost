package com.frxs.check.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ewu on 2016/3/23.
 */
public class UserInfo implements Serializable{
    @SerializedName("UserId")
    private String UserId;

    private String EmpID;//":"10",//用户编号
    private String ShelfID;//:"20",//货位编号(Shelf.ShelfID)
    private String ShelfAreaID;//:"30",//货区编号(ShelfArea.ShelfAreaID)
    private String WID;//:"10000",//仓库ID(Warehouse.WID)
    private String ShelfAreaCode;//:"1010",//仓库货区编码
    private String ShelfAreaName;//:"大货区",//货区名称
    private String PickingMaxRecord;//:"1",//拣货APP最大显示数
    private String Remark;//:“zhangsan“,//备注
    private int IsMaster;//:0,//是否是组长
    private String UserMobile;//:”13974974751”//用户联络手机号码
    private String EmpName;//用户
    private int Multiple = 2;// 装箱数量的最大倍数，默认为2

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public String getShelfID() {
        return ShelfID;
    }

    public void setShelfID(String shelfID) {
        ShelfID = shelfID;
    }

    public String getShelfAreaID() {
        return ShelfAreaID;
    }

    public void setShelfAreaID(String shelfAreaID) {
        ShelfAreaID = shelfAreaID;
    }

    public String getWID() {
        return WID;
    }

    public void setWID(String WID) {
        this.WID = WID;
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

    public String getPickingMaxRecord() {
        return PickingMaxRecord;
    }

    public void setPickingMaxRecord(String pickingMaxRecord) {
        PickingMaxRecord = pickingMaxRecord;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getIsMaster() {
        return IsMaster;
    }

    public void setIsMaster(int isMaster) {
        IsMaster = isMaster;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public int getMultiple() {
        return Multiple;
    }

    public void setMultiple(int multiple) {
        Multiple = multiple;
    }
}
