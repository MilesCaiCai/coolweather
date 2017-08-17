package com.fionacos.myapp.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Miles on 2017/8/17.
 * 省份类
 */

public class Province extends DataSupport{
    //ID
    private int id;

    private String pName;
    //省份ID
    private int pId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }
}
