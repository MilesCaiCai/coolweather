package com.fionacos.myapp.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Miles on 2017/8/17.
 */

/*城市类*/
public class City extends DataSupport {

    private int id;
    private String cName;
    private int cId;
    private int pId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }
}
