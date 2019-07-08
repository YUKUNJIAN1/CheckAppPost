package com.frxs.check.model;

/**
 * Created by Endoon on 2016/9/13.
 */
public class GiftsCheckItem {
    private String ProductName;//: 1,
    private String Qty;//: 4.0,
    private String ProductID;//: 1,
    private String BuyQty;//: 2.0,
    private String Unit;//: "sample string 3",
    private String SaleUnit;//: "sample string 4",
    private String PackingQty;//: 5.0
    private String SKU;//: 5.0

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getBuyQty() {
        return BuyQty;
    }

    public void setBuyQty(String buyQty) {
        BuyQty = buyQty;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getSaleUnit() {
        return SaleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        SaleUnit = saleUnit;
    }

    public String getPackingQty() {
        return PackingQty;
    }

    public void setPackingQty(String packingQty) {
        PackingQty = packingQty;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }
}
