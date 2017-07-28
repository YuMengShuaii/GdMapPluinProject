package com.enation.javashop.mapparents.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.WalkPath;
import com.enation.javashop.map.model.PathModel;
import com.enation.javashop.map.utils.MapType;
import com.enation.javashop.map.map.MapUtils;
import com.enation.javashop.mapparents.R;
import com.enation.javashop.mapparents.adapter.RouteAdapter;
import com.enation.javashop.mapparents.application.Application;

public class RouteDetaiAct extends AppCompatActivity {
    /**
     * 地图控件
     */
    private MapView mMapView;
    /**
     * 地图配置对象
     */
    private AMap aMap = null;
    /**
     * 地图工具类
     */
    private MapUtils mapUtils;
    /**
     * 数据容器
     */
    private PathModel busModel;
    /**
     * 数据索引
     */
    private int position;

    /**
     * 控件
     */
    private TextView busname,time;
    private ImageView flag;
    private ListView routelist;
    private LinearLayout isVisble;
    /**
     * 自驾路线信息
     */
    private DrivePath drivePath;
    /**
     * 步行路线信息
     */
    private WalkPath walkPath;
    /**
     * 公交路线信息
     */
    private BusPath busPath;
    /**
     * 终点坐标
     */
    private LatLng endLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detai);
        /**
         * 初始化控件及对象
         */
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        busname = (TextView) findViewById(R.id.bustext);
        time = (TextView) findViewById(R.id.time);
        flag = (ImageView) findViewById(R.id.flag);
        routelist = (ListView) findViewById(R.id.route_list);
        isVisble = (LinearLayout) findViewById(R.id.isVisble);
        /**
         * 获取数据
         */
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        position = (int) Application.get("position",true);
        busModel = (PathModel) Application.get("bus",true);
        endLocation = (LatLng) Application.get("endLocation",true);
        mapUtils = MapUtils.getMapUtils(this, aMap, Application.latlon, (String) Application.get("title",true),true);
        busPath = busModel.getPaths().get(position).getPath();
        drivePath = busModel.getPaths().get(position).getDrivePath();
        walkPath = busModel.getPaths().get(position).getWalkPath();
        routelist.setAdapter(new RouteAdapter(getBaseContext(),busModel.getPaths().get(position)));
        /**
         * 设置地图加载监听事件
         */
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                //设置地图中心店
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(endLocation));
                //设置缩放级别
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            }
        });
        /**
         * 公交标题不为空时执行以下代码，获取公交名称，所需时间及步行距离
         */
        if (busModel.getPaths().get(position).getBusRotueTitle()!=null&&!busModel.getPaths().get(position).getBusRotueTitle().equals("")){
            time.setText(busModel.getPaths().get(position).getBusRotueTitle());
            if (busModel.getPaths().get(position).getBusNames()!=null&&busModel.getPaths().get(position).getBusNames().size()>0){
                String bus = "";
                for (int i = 0; i < busModel.getPaths().get(position).getBusNames().size(); i++) {
                    if (i>=0&&i!=busModel.getPaths().get(position).getBusNames().size()-1){
                        bus+=busModel.getPaths().get(position).getBusNames().get(i)+">";
                    }else{
                        bus+=busModel.getPaths().get(position).getBusNames().get(i);
                    }
                }
                busname.setText(bus);
            }
            //绘制公交轨迹
            mapUtils.setPathTrajectory(MapType.BUS,busPath,busModel.getStart(),busModel.getEnd(),busModel.getPaths().get(position).getAllInfo());
        }
        /**
         * 当自驾标题不为空时执行以下代码，设置标题 ，所需时间，行驶距离
         */
        if (busModel.getPaths().get(position).getDriverTitle()!=null&&!busModel.getPaths().get(position).getDriverTitle().equals("")){
            time.setText(busModel.getPaths().get(position).getDriverTitle());
            busname.setText(busModel.getPaths().get(position).getDriver());
            //绘制驾驶轨迹
            mapUtils.setPathTrajectory(MapType.DRIVE,drivePath,busModel.getStart(),busModel.getEnd(),busModel.getPaths().get(position).getAllInfo());
        }
        /**
         * 当步行标题不为空时 设置标题 所需时间 步行距离
         */
        if (busModel.getPaths().get(position).getWalkTitle()!=null&&!busModel.getPaths().get(position).getWalkTitle().equals("")){
            time.setText(busModel.getPaths().get(position).getWalkTitle());
            busname.setText(busModel.getPaths().get(position).getWalkTitle());
            //绘制步行轨迹
            mapUtils.setPathTrajectory(MapType.WALK,walkPath,busModel.getStart(),busModel.getEnd(),busModel.getPaths().get(position).getAllInfo());
        }
        /**
         * 显示/隐藏Listview
         */
        isVisble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (routelist.getVisibility()== View.GONE){
                    routelist.setVisibility(View.VISIBLE);
                    flag.setImageResource(R.drawable.down);
                }else{
                    routelist.setVisibility(View.GONE);
                    flag.setImageResource(R.drawable.up);
                }
            }
        });
        /**
         * 设置item监听事件，判断坐标是否为空，并更改地图中心点。
         */
        routelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                if (busModel.getPaths().get(position).getAllInfo().get(position1).getBusStartPoint()!=null){
                    mapUtils.moveMap(busModel.getPaths().get(position).getAllInfo().get(position1).getBusStartPoint());
                }
                 if (busModel.getPaths().get(position).getAllInfo().get(position1).getWalkStartPoint()!=null){
                    mapUtils.moveMap(busModel.getPaths().get(position).getAllInfo().get(position1).getWalkStartPoint());
                }
                if (busModel.getPaths().get(position).getAllInfo().get(position1).getCarStartPoint()!=null){
                    mapUtils.moveMap(busModel.getPaths().get(position).getAllInfo().get(position1).getCarStartPoint());
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("on","222");
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        mapUtils.finshMapUtils();
        mapUtils = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("on","333");
        mMapView.onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("on","444");
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("on","555");
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


}
