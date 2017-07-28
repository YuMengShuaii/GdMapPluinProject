package com.enation.javashop.mapparents.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.enation.javashop.map.path.GetPathUtils;
import com.enation.javashop.map.model.PathModel;
import com.enation.javashop.map.monitor.Listener;
import com.enation.javashop.map.utils.MapType;
import com.enation.javashop.map.map.MapUtils;
import com.enation.javashop.mapparents.R;
import com.enation.javashop.mapparents.application.Application;
import com.enation.javashop.mapparents.utils.Utils;

public class Map extends AppCompatActivity {
    private MapView mMapView;
    private AMap aMap = null;
    private MapUtils mapUtils;
    private TextView busminte, tv1, tv2, tv3;
    private LinearLayout bus, bar, lay2, lay3;
    private ImageView image1, image2, image3;
    private PathModel busModel;
    private ProgressDialog dialog;
    private GetPathUtils getPathUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("on", "111");
        setContentView(R.layout.activity_map);
        //初始化加载对话框
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        getPathUtils = GetPathUtils.getPathUtils(getBaseContext());
        /**
         * ==============================================================
         */
        bar = (LinearLayout) findViewById(R.id.bottom_bar);
        bus = (LinearLayout) findViewById(R.id.bus);
        mMapView = (MapView) findViewById(R.id.map);
        busminte = (TextView) findViewById(R.id.busMinite);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        tv1 = (TextView) findViewById(R.id.image1_text);
        tv2 = (TextView) findViewById(R.id.image2_text);
        tv3 = (TextView) findViewById(R.id.image3_text);
        lay2 = (LinearLayout) findViewById(R.id.lay2);
        lay3 = (LinearLayout) findViewById(R.id.lay3);
        /**
         * ==============================================================
         */
        //初始化mapview生命周期
        mMapView.onCreate(savedInstanceState);
        //获取Amap对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //设置地图加载完成监听事件
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                //移动地图中心点到制定location
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(Application.latlon));
                //缩放指定层级
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            }
        });
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Utils.toastS(getBaseContext(),latLng.toString());
            }
        });
        //获取我的定位到目的地之间的距离，单位是米
        float distance = AMapUtils.calculateLineDistance(new LatLng(Application.mylocation.getLatitude(), Application.mylocation.getLongitude()), Application.latlon);
        //显示加载框
        dialog.show();
        //初始化MapUtils工具类
        mapUtils = MapUtils.getMapUtils(this, aMap, Application.latlon, (String) Application.get("title",false), true);
        //当距离大于500米时使用公交路线，小于500米时使用步行路线，驾车永远是备选
        if (distance > 500) {
            //获取公交信息方法
            getPathUtils.getPathData(Application.latlon, MapType.BUS, new Listener.DataLisener<PathModel>() {
                @Override
                public void success(PathModel cloudResult) {
                    if (cloudResult != null && cloudResult.getPaths() != null && cloudResult.getPaths().size() > 0) {
                        busModel = cloudResult;
                        //设置分钟数
                        busminte.setText(cloudResult.getPaths().get(0).getBusRotueTitle());
                        //移动地图中心点到目的地
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(Application.latlon));
                        //缩放地图
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                        dialog.dismiss();
                        //显示bar
                        bar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void faild(String errorMessage) {

                }
            },true);

            bus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    busLisener(MapType.BUS);
                }
            });
            lay2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    busLisener(MapType.DRIVE);
                }
            });
            lay3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    busLisener(MapType.WALK);
                }
            });
        } else {
            //获取步行信息
            getPathUtils.getPathData(Application.latlon, MapType.WALK, new Listener.DataLisener<PathModel>() {
                @Override
                public void success(PathModel walkModel) {
                    //设置步行的bar布局
                    //=============================================================
                    image1.setImageResource(R.drawable.walk);
                    image2.setImageResource(R.drawable.bus);
                    image3.setImageResource(R.drawable.car);
                    tv1.setText("步行");
                    tv2.setText("公交");
                    tv3.setText("驾车");
                    //=============================================================
                    //移动地图中心店
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(Application.latlon));
                    //改变地图缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //设置信息
                    busminte.setText(walkModel.getPaths().get(0).getWalkTitle());
                    busModel = walkModel;
                    dialog.dismiss();
                    //显示bar
                    bar.setVisibility(View.VISIBLE);
                }

                @Override
                public void faild(String errorMessage) {

                }
            },true);

            bus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    walkLisener(MapType.WALK);
                }
            });
            lay2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    walkLisener(MapType.BUS);
                }
            });
            lay3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    walkLisener(MapType.DRIVE);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("on", "222");
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        //销毁mapUtils
        mapUtils.finshMapUtils();
        mapUtils = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("on", "333");
        mMapView.onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("on", "444");
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("on", "555");
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private void busLisener(@MapType.MT final int mapType) {
        if (busModel == null) {
            Toast.makeText(getBaseContext(), "路线信息没有获取完毕!", Toast.LENGTH_SHORT).show();
        }
        dialog.show();
        //获取自驾信息
        getPathUtils.getPathData(Application.latlon, MapType.DRIVE, new Listener.DataLisener<PathModel>() {
            @Override
            public void success(final PathModel drivermodel) {
                //获取步行信息
                getPathUtils.getPathData(Application.latlon, MapType.WALK, new Listener.DataLisener<PathModel>() {
                    @Override
                    public void success(PathModel walkModel) {
                        dialog.dismiss();
                        //传输数据，跳转页面
                        Application.put("bus", busModel);
                        Application.put("car", drivermodel);
                        Application.put("walk", walkModel);
                        Application.put("type", mapType);
                        Intent intent = new Intent(getBaseContext(), RouteListActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void faild(String errorMessage) {

                    }
                },true);
            }

            @Override
            public void faild(String errorMessage) {

            }

        },true);
    }

    private void walkLisener(@MapType.MT final int mapType) {
        if (busModel == null) {
            Toast.makeText(getBaseContext(), "路线信息没有获取完毕!", Toast.LENGTH_SHORT).show();
        }
        dialog.show();
        //获取自驾信息
        getPathUtils.getPathData(Application.latlon, MapType.DRIVE, new Listener.DataLisener<PathModel>() {
            @Override
            public void success(final PathModel drivermodel) {
                //获取公交信息
                getPathUtils.getPathData(Application.latlon, MapType.BUS, new Listener.DataLisener<PathModel>() {
                    @Override
                    public void success(PathModel busModel1) {
                        dialog.dismiss();
                        //传输数据，跳转页面。
                        Application.put("bus", busModel1);
                        Application.put("car", drivermodel);
                        Application.put("walk", busModel);
                        Application.put("type", mapType);
                        Intent intent = new Intent(getBaseContext(), RouteListActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void faild(String errorMessage) {

                    }
                },true);
            }

            @Override
            public void faild(String errorMessage) {

            }
        },true);
    }
}
