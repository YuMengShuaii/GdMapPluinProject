package com.enation.javashop.mapparents.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.cloud.CloudItem;
import com.bumptech.glide.Glide;
import com.enation.javashop.mapparents.R;
import com.enation.javashop.mapparents.application.Application;

public class LIstDatail extends AppCompatActivity {
    private TextView title,address,dicance;
    private ImageView icon;
    private Button go;
    private CloudItem cloudItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_datail);
        initView();
        LoadData();
        initOper();
    }
    private void initView(){
        title = (TextView) findViewById(R.id.title);
        icon = (ImageView) findViewById(R.id.icona);
        address = (TextView) findViewById(R.id.address);
        dicance  = (TextView) findViewById(R.id.diance);
        go = (Button) findViewById(R.id.gogogo);
    }
    private void LoadData(){
        cloudItem = (CloudItem) Application.get("itemData",true);
    }
    private void initOper(){
        if (cloudItem.getCloudImage().size()>0){
            Glide.with(getBaseContext()).load(cloudItem.getCloudImage().get(0).getUrl()).centerCrop().dontAnimate().into(icon);
        }
        title.setText(cloudItem.getTitle());
        address.setText(cloudItem.getSnippet());
        dicance.setText("距离你"+cloudItem.getDistance()+"m");
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Application.latlon = new LatLng(cloudItem.getLatLonPoint().getLatitude(),cloudItem.getLatLonPoint().getLongitude());
                Application.put("title",cloudItem.getTitle());
                startActivity(new Intent(getBaseContext(),Map.class));
            }
        });
    }
}
