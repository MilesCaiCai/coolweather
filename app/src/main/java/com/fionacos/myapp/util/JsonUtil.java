package com.fionacos.myapp.util;

import android.text.TextUtils;

import com.fionacos.myapp.db.City;
import com.fionacos.myapp.db.County;
import com.fionacos.myapp.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Miles on 2017/8/17.
 * Json解析类
 */

public class JsonUtil {

    /*解析省信息*/
    public static boolean handleProvinceResponse(String reponse){
        if (!TextUtils.isEmpty(reponse)){
            try {
                JSONArray allPrivonces = new JSONArray(reponse);
                for (int i = 0; i < allPrivonces.length(); i++) {
                    JSONObject objProvince = allPrivonces.getJSONObject(i);
                    Province pro = new Province();
                    pro.setpName(objProvince.getString("name"));
                    pro.setpId(objProvince.getInt("id"));
                    pro.delete();
                    pro.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*解析市级信息*/
    public static boolean handleCityResponse(String reponse,int pid){
        if (!TextUtils.isEmpty(reponse)) {
            try {
                JSONArray allCity = new JSONArray(reponse);
                for (int i = 0; i < allCity.length(); i++) {
                    JSONObject objCity = allCity.getJSONObject(i);
                    City ci = new City();
                    ci.setcName(objCity.getString("name"));
                    ci.setcId(objCity.getInt("id"));
                    ci.setpId(pid);
                    ci.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*解析区县级信息*/
    public static boolean handleCountyResponse(String reponse,int cid){
        if (!TextUtils.isEmpty(reponse)) {
            try {
                JSONArray allCounties = new JSONArray(reponse);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject objCounty = allCounties.getJSONObject(i);
                    County co = new County();
                    co.setCountyName(objCounty.getString("name"));
                    co.setWeatherId(objCounty.getString("weather_id"));
                    co.setcId(cid);
                    co.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
