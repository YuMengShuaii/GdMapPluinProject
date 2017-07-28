package com.enation.javashop.mapparents.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudResult;
import com.enation.javashop.map.cloudmap.MapQueryUtils;
import com.enation.javashop.map.monitor.Listener;
import com.enation.javashop.map.utils.MapConst;
import com.enation.javashop.map.map.MapUtils;
import com.enation.javashop.mapparents.R;
import com.enation.javashop.mapparents.application.Application;
import com.enation.javashop.mapparents.utils.Utils;

public class QueryActivity extends AppCompatActivity {
    /**
     * 地图控件
     */
    private MapView mapView;
    /**
     * 地图配置对象
     */
    private AMap aMap;
    private TextView find;
    private EditText condtion;
    /**
     * 地图工具类
     */
    private MapUtils mapUtils;

    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        activity = this;
        mapView = (MapView) findViewById(R.id.queryMap);
        mapView.onCreate(savedInstanceState);
        condtion = (EditText) findViewById(R.id.condtion);
        find = (TextView) findViewById(R.id.find);
        if (aMap == null){
            aMap = mapView.getMap();
        }
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                Utils.createDialog(marker.getTitle() + "正在优惠！", "取消", "去这里", activity, new Utils.DialogInterface() {
                    @Override
                    public void no() {

                    }

                    @Override
                    public void yes() {
                        Application.latlon = marker.getPosition();
                        Application.put("title",marker.getTitle());
                        startActivity(new Intent(getBaseContext(),Map.class));
                    }
                });
            }
        });

        mapUtils = MapUtils.getMapUtils(this,aMap,Application.latlon, (String) Application.get("title",true),false);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.show();
                MapQueryUtils.Query(getBaseContext(), Application.mylocation,0, condtion.getText().toString().trim(),null,0,null,null, new Listener.DataLisener<CloudResult>() {
                    @Override
                    public void success(CloudResult cloudResult) {
                        aMap.clear();
                        mapUtils.setMarker(null,false);
                        /**
                         * 显示范围
                         */
                        LatLngBounds.Builder latLngBounds =new LatLngBounds.Builder();
                        for (CloudItem cloudItem : cloudResult.getClouds()) {
                            LatLng latLng = new LatLng(cloudItem.getLatLonPoint().getLatitude(),cloudItem.getLatLonPoint().getLongitude());
                            /**
                             * 添加范围点
                             */
                            latLngBounds.include(latLng);
                            /**
                             * 添加搜索标记
                             */
                            if (cloudResult.getClouds().size()==1){
                                aMap.addMarker(new MarkerOptions().position(latLng).title(cloudItem.getTitle()+"距离您"+cloudItem.getDistance()+"m"+"地址："+cloudItem.getSnippet()+"Storeid:"+cloudItem.getCustomfield().get("sid")).icon(BitmapDescriptorFactory.fromResource(R.drawable.adresss))).showInfoWindow();
                            }else{
                                aMap.addMarker(new MarkerOptions().position(latLng).title(cloudItem.getTitle()+"距离您"+cloudItem.getDistance()+"m"+"地址："+cloudItem.getSnippet()+"Storeid:"+cloudItem.getCustomfield().get("sid")).icon(BitmapDescriptorFactory.fromResource(R.drawable.adresss)));

                            }
                        }
                        /**
                         * 移动缩放
                         */
                        if (cloudResult.getClouds().size()==1){
                            LatLng latLng = new LatLng(MapConst.MYLOCATION.getLatitude(),MapConst.MYLOCATION.getLongitude());
                            latLngBounds.include(latLng);
                        }
                        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(),150));
                        progressDialog.dismiss();
                    }

                    @Override
                    public void faild(String errorMessage) {
                            Toast.makeText(getBaseContext(),errorMessage, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                    }
                });
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("on","222");
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        //销毁mapUtils
        mapUtils.finshMapUtils();
        mapUtils = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("on","333");
        mapView.onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("on","444");
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("on","555");
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
}
