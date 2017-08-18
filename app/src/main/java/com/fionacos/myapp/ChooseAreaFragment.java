package com.fionacos.myapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fionacos.myapp.db.City;
import com.fionacos.myapp.db.County;
import com.fionacos.myapp.db.Province;
import com.fionacos.myapp.util.HttpUtil;
import com.fionacos.myapp.util.JsonUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Miles on 2017/8/17.
 * 遍历省市区的碎片
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVLE_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    /*选中的省份*/
    private Province selectedPro;
    /*选中的城市*/
    private City selectedCity;
    /*选中的级别*/
    private int selectedLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.title_button);
        listView = (ListView) view.findViewById(R.id.title_list);
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (selectedLevel == LEVEL_PROVINCE) {
                    selectedPro = provinceList.get(position);
                    //
                    queryCities();
                }else if (selectedLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    //
                    queryCounties();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedLevel == LEVLE_COUNTY) {
                    //
                    queryCities();
                }else if (selectedLevel == LEVEL_CITY) {
                    //
                    queryProvinces();
                }
            }
        });
        queryProvinces();
        /*deleteProvinces();*/
    }

    /*删除所有的省份*/
    private void deleteProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);

        DataSupport.deleteAll(Province.class);
            //

    };

    /*查询所有的省份，优先从数据库查，没有则从服务器查*/
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province pro : provinceList) {
                dataList.add(pro.getpName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            selectedLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            //
            queryFromServer(address,"province");
        }
    };

    /*查询所有的城市，优先从数据库查，没有则从服务器查*/
    private void queryCities(){
        titleText.setText(selectedPro.getpName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("pId = ?",String.valueOf(selectedPro.getpId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getcName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            selectedLevel = LEVEL_CITY;
        }else{
            int pId = selectedPro.getpId();
            String address = "http://guolin.tech/api/china/"+pId;
            //
            queryFromServer(address,"city");
        }
    }

    /*查询所有的区县，优先从数据库查，没有则从服务器查*/
    private void queryCounties(){
        titleText.setText(selectedCity.getcName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cId = ?",String.valueOf(selectedCity.getcId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County cou : countyList) {
                dataList.add(cou.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            selectedLevel = LEVLE_COUNTY;
        }else{
            int pId = selectedPro.getpId();
            int cId = selectedCity.getcId();
            String address = "http://guolin.tech/api/china/" + pId + "/" + cId;
            //
            queryFromServer(address,"county");
        }
    }

    /*根据地址和查询类型 从服务器查询省市县信息*/
    private void queryFromServer(String address,final String type){
        //
        showProgressDialog();
        HttpUtil.senOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = JsonUtil.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result = JsonUtil.handleCityResponse(responseText,selectedPro.getpId());
                }else if("county".equals(type)){
                    result = JsonUtil.handleCountyResponse(responseText,selectedCity.getcId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //
                        closeProgressDialog();
                        Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    /*显示进度框*/
    private void showProgressDialog(){
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("亲~骚等片刻~");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    /*关闭进度框*/
    private void closeProgressDialog(){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
