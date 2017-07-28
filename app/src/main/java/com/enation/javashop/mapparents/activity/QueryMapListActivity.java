package com.enation.javashop.mapparents.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudResult;
import com.enation.javashop.map.cloudmap.MapQueryUtils;
import com.enation.javashop.map.monitor.Listener;
import com.enation.javashop.mapparents.R;
import com.enation.javashop.mapparents.adapter.QueryListAdapter;
import com.enation.javashop.mapparents.application.Application;

import java.util.ArrayList;

public class QueryMapListActivity extends AppCompatActivity {
    private EditText condtion;
    private TextView find,noData;
    private ListView list;
    private QueryListAdapter adapter;
    private ArrayList<CloudItem> data;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_map_list);
        initView();
    }
    private void initView(){
        dialog = new ProgressDialog(this);
        condtion = (EditText) findViewById(R.id.condtion);
        find = (TextView) findViewById(R.id.find);
        list = (ListView) findViewById(R.id.querylist);
        noData = (TextView) findViewById(R.id.noData);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if (data==null){
                   data = new ArrayList<CloudItem>();
                }
                data.clear();
                LoadData();
            }
        });
    }
    private void LoadData(){

        MapQueryUtils.Query(getBaseContext(), Application.mylocation, 0, condtion.getText().toString().trim(),null,0,null,null, new Listener.DataLisener<CloudResult>() {
            @Override
            public void success(CloudResult cloudResult) {
                data.addAll(cloudResult.getClouds());
                initOper();
                dialog.dismiss();
            }

            @Override
            public void faild(String errorMessage) {
                Toast.makeText(getBaseContext(),errorMessage, Toast.LENGTH_SHORT).show();
                initOper();
                dialog.dismiss();
            }
        });
    }
    private void initOper(){
        if (data.size()==0){
            noData.setVisibility(View.VISIBLE);
        }else{
            noData.setVisibility(View.GONE);
        }
        adapter = new QueryListAdapter(getBaseContext(),data);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Application.put("itemData",adapter.getItem(position));
                    startActivity(new Intent(getBaseContext(),LIstDatail.class));
            }
        });
    }
}
