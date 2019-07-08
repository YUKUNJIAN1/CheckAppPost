package com.frxs.check.model;

import java.io.Serializable;
import java.util.List;

/**
 * 拣货员信息实体
 * Created by Endoon on 2016/3/31.
 */
public class Warehouse implements Serializable {
    private String UserMobile;

    public String getUserMobile() {
        return UserMobile;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }
}
