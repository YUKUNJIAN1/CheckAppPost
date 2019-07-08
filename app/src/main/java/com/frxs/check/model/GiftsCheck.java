package com.frxs.check.model;

import java.util.List;

/**
 * Created by Endoon on 2016/9/13.
 */
public class GiftsCheck {
    private String ShelfAreaCode;//: "sample string 1",
    private String ShelfAreaName;//": "sample string 2",
    private List<GiftsCheckItem> Items;

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

    public List<GiftsCheckItem> getItems() {
        return Items;
    }

    public void setItems(List<GiftsCheckItem> items) {
        Items = items;
    }
}
