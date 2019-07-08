package com.frxs.check.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by ewu on 2017/3/1.
 */

@Entity
public class ProductEntity implements Serializable{

    private static final long serialVersionUID = -1;

    @Id
    private String ID;//主键编号

    private String OrderId;

    private String ProductId;

    private String ProductName;

    private String ShelfAreaID;

    private String ShelfAreaName;

    private String BarCode;

    private String SKU;

    private double SaleQty;

    private String SaleUnit;

    private double SalePrice;

    private int IsGift;

    private double SalePackingQty;

    private int PickUserId;//: 1111,

    private String PickUserName;//: "杜红英"

    private String Remark;//:

    private String ShelfCode;// 商品货位号

    private boolean isChecked = true;  //表示是否已对货 true未对货 false已对货

    private double pickedQty = -1; //拣货数量，初始为SaleQty数量，不做变更

    private double PreQty;//订货数量

    private String PickEndTime;// 商品拣货时间

    private String AreaCheckTime;// 商品对货时间

    private String ShelfAreaCategoryName;// 商品父级货区名称

    @Generated(hash = 1301522764)
    public ProductEntity(String ID, String OrderId, String ProductId, String ProductName,
            String ShelfAreaID, String ShelfAreaName, String BarCode, String SKU, double SaleQty,
            String SaleUnit, double SalePrice, int IsGift, double SalePackingQty, int PickUserId,
            String PickUserName, String Remark, String ShelfCode, boolean isChecked, double pickedQty,
            double PreQty, String PickEndTime, String AreaCheckTime, String ShelfAreaCategoryName) {
        this.ID = ID;
        this.OrderId = OrderId;
        this.ProductId = ProductId;
        this.ProductName = ProductName;
        this.ShelfAreaID = ShelfAreaID;
        this.ShelfAreaName = ShelfAreaName;
        this.BarCode = BarCode;
        this.SKU = SKU;
        this.SaleQty = SaleQty;
        this.SaleUnit = SaleUnit;
        this.SalePrice = SalePrice;
        this.IsGift = IsGift;
        this.SalePackingQty = SalePackingQty;
        this.PickUserId = PickUserId;
        this.PickUserName = PickUserName;
        this.Remark = Remark;
        this.ShelfCode = ShelfCode;
        this.isChecked = isChecked;
        this.pickedQty = pickedQty;
        this.PreQty = PreQty;
        this.PickEndTime = PickEndTime;
        this.AreaCheckTime = AreaCheckTime;
        this.ShelfAreaCategoryName = ShelfAreaCategoryName;
    }

    @Generated(hash = 27353230)
    public ProductEntity() {
    }

    public String getOrderId() {
        return this.OrderId;
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }

    public String getProductName() {
        return this.ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public String getShelfAreaName() {
        return this.ShelfAreaName;
    }

    public void setShelfAreaName(String ShelfAreaName) {
        this.ShelfAreaName = ShelfAreaName;
    }

    public String getBarCode() {
        return this.BarCode;
    }

    public void setBarCode(String BarCode) {
        this.BarCode = BarCode;
    }

    public String getSKU() {
        return this.SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public double getSaleQty() {
        return this.SaleQty;
    }

    public void setSaleQty(double SaleQty) {
        this.SaleQty = SaleQty;
    }

    public String getSaleUnit() {
        return this.SaleUnit;
    }

    public void setSaleUnit(String SaleUnit) {
        this.SaleUnit = SaleUnit;
    }

    public int getIsGift() {
        return this.IsGift;
    }

    public void setIsGift(int IsGift) {
        this.IsGift = IsGift;
    }

    public double getSalePackingQty() {
        return this.SalePackingQty;
    }

    public void setSalePackingQty(double SalePackingQty) {
        this.SalePackingQty = SalePackingQty;
    }

    public int getPickUserId() {
        return this.PickUserId;
    }

    public void setPickUserId(int PickUserId) {
        this.PickUserId = PickUserId;
    }

    public String getPickUserName() {
        return this.PickUserName;
    }

    public void setPickUserName(String PickUserName) {
        this.PickUserName = PickUserName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public double getSalePrice() {
        return this.SalePrice;
    }

    public void setSalePrice(double SalePrice) {
        this.SalePrice = SalePrice;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProductId() {
        return this.ProductId;
    }

    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    public String getShelfAreaID() {
        return this.ShelfAreaID;
    }

    public void setShelfAreaID(String ShelfAreaID) {
        this.ShelfAreaID = ShelfAreaID;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getShelfCode() {
        return this.ShelfCode;
    }

    public void setShelfCode(String ShelfCode) {
        this.ShelfCode = ShelfCode;
    }

    public boolean getIsChecked() {
        return this.isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public double getPickedQty() {
        return this.pickedQty;
    }

    public void setPickedQty(double pickedQty) {
        this.pickedQty = pickedQty;
    }

    public double getPreQty() {
        return PreQty;
    }

    public void setPreQty(double preQty) {
        PreQty = preQty;
    }

    public String getPickEndTime() {
        return PickEndTime;
    }

    public void setPickEndTime(String pickEndTime) {
        PickEndTime = pickEndTime;
    }

    public String getAreaCheckTime() {
        return AreaCheckTime;
    }

    public void setAreaCheckTime(String areaCheckTime) {
        AreaCheckTime = areaCheckTime;
    }

    public String getShelfAreaCategoryName() {
        return ShelfAreaCategoryName;
    }

    public void setShelfAreaCategoryName(String shelfAreaCategoryName) {
        ShelfAreaCategoryName = shelfAreaCategoryName;
    }
}
