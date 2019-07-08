package com.frxs.check.model;

import com.frxs.check.greendao.entity.ProductEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 商品信息 by Tiepier
 */
public class ProductData implements Serializable {

    private static final long serialVersionUID = 8411254904385284855L;

    private String ShelfAreaType;//商品类型

    private List<ProductEntity> ProdcutDetailList;//商品信息

    public String getShelfAreaType() {
        return ShelfAreaType;
    }

    public void setShelfAreaType(String shelfAreaType) {
        ShelfAreaType = shelfAreaType;
    }

    public List<ProductEntity> getProdcutDetailList() {
        return ProdcutDetailList;
    }

    public void setProdcutDetailList(List<ProductEntity> prodcutDetailList) {
        ProdcutDetailList = prodcutDetailList;
    }
}
