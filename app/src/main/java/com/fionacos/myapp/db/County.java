package com.fionacos.myapp.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Miles on 2017/8/17.
 * 区县类
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String weatherId;
    private int cId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }
}
