package com.frxs.check.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Endoon on 2016/6/17.
 */
public class GoodsInfoList implements Serializable{
    private List<GoodsInfo> goodsInfoList;

    public List<GoodsInfo> getGoodsInfoList() {
        return goodsInfoList;
    }

    public void setGoodsInfoList(List<GoodsInfo> goodsInfoList) {
        this.goodsInfoList = goodsInfoList;
    }
}
