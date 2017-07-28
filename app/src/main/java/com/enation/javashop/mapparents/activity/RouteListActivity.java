package com.enation.javashop.mapparents.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.amap.api.maps2d.model.LatLng;
import com.enation.javashop.map.path.GetPathUtils;
import com.enation.javashop.map.model.PathModel;
import com.enation.javashop.map.monitor.Listener;
import com.enation.javashop.map.utils.MapType;
import com.enation.javashop.mapparents.R;
import com.enation.javashop.mapparents.adapter.BusAdapter;
import com.enation.javashop.mapparents.application.Application;

public class RouteListActivity extends AppCompatActivity {
    /**
     * 数据容器
     */
    private PathModel carModel,walkModel,busModel;
    /**
     * listview
     */
    private ListView listView;
    /**
     * 反转按钮
     */
    private ImageView each;
    /**
     * 分类指示
     */
    private View index1,index2,index3;
    /**
     * TEXTVIEW
     */
    private TextView busButton,driverButton,walkButton,start,end,noData;
    /**
     * 初始类别
     */
    private  int mapType;
    /**
     * 获取路线工具类
     */
    private GetPathUtils getPathUtils;
    /**
     * 反转路线flag
     */
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        /**
         * 初始化控件及对象
         */
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        getPathUtils = GetPathUtils.getPathUtils(getBaseContext());
        listView = (ListView) findViewById(R.id.list);
        busButton = (TextView) findViewById(R.id.bus);
        driverButton = (TextView) findViewById(R.id.driver);
        walkButton = (TextView) findViewById(R.id.walk);
        each = (ImageView) findViewById(R.id.each);
        index1 = findViewById(R.id.index1);
        index2 = findViewById(R.id.index2);
        index3 = findViewById(R.id.index3);
        noData = (TextView) findViewById(R.id.noData);
        start = (TextView) findViewById(R.id.start);
        end = (TextView) findViewById(R.id.end);
        String title = (String) Application.get("title",true);
        if (title==null){
            title="大保健";
        }
        end.setText(title);
        /**
         * 获取数据
         */
        busModel = (PathModel) Application.get("bus",true);
        carModel = (PathModel) Application.get("car",true);
        walkModel = (PathModel) Application.get("walk",true);
        mapType = (int) Application.get("type",true);

        /**
         * 区分类别进行相应操作
         */
        if (mapType == MapType.BUS){
            lisener(1);
        }
        if (mapType == MapType.DRIVE){
            lisener(2);
        }
        if (mapType == MapType.WALK){
            lisener(3);
        }
        /**
         * 设置监听事件
         */
        busButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lisener(1);
            }
        });
        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lisener(2);
            }
        });
        walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lisener(3);
            }
        });
        each.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                //获取公交数据
                getPathUtils.getPathData(Application.latlon, MapType.BUS,flag, new Listener.DataLisener<PathModel>() {
                    @Override
                    public void success(PathModel busModel1) {
                        busModel = busModel1;
                        //获取自驾数据
                        getPathUtils.getPathData(Application.latlon, MapType.DRIVE,flag, new Listener.DataLisener<PathModel>() {
                            @Override
                            public void success(PathModel drivermodel) {
                                carModel = drivermodel;
                                //获取步行数据
                                getPathUtils.getPathData(Application.latlon, MapType.WALK,flag, new Listener.DataLisener<PathModel>() {
                                    @Override
                                    public void success(PathModel walkModel1) {
                                        walkModel = walkModel1;
                                        if (index1.getVisibility()== View.VISIBLE){
                                            lisener(1);
                                        }
                                        if (index2.getVisibility()== View.VISIBLE){
                                            lisener(2);
                                        }
                                        if (index3.getVisibility()== View.VISIBLE){
                                            lisener(3);
                                        }
                                        if (flag){
                                            start.setText("我的位置");
                                            end.setText(start.getText().toString().trim());
                                        }else{
                                            start.setText(end.getText().toString().trim());
                                            end.setText("我的位置");
                                        }
                                        dialog.dismiss();
                                        flag=!flag;
                                    }

                                    @Override
                                    public void faild(String errorMessage) {

                                    }

                                });
                            }

                            @Override
                            public void faild(String errorMessage) {

                            }
                        });
                    }

                    @Override
                    public void faild(String errorMessage) {

                    }
                });
            }
        });
    }
    //更改数据 公共监听事件
    private void lisener(final int type){
        //区分Type更改界面，刷新适配器数据
        switch (type){
            case 1:
                index1.setVisibility(View.VISIBLE);
                index2.setVisibility(View.INVISIBLE);
                index3.setVisibility(View.INVISIBLE);
                listView.setAdapter(new BusAdapter(getBaseContext(),busModel));
                if (busModel==null||busModel.getPaths().size()==0){
                    noData.setVisibility(View.VISIBLE);
                }else{
                    noData.setVisibility(View.GONE);
                }
            break;
            case 2:
                index1.setVisibility(View.INVISIBLE);
                index2.setVisibility(View.VISIBLE);
                index3.setVisibility(View.INVISIBLE);
                listView.setAdapter(new BusAdapter(getBaseContext(),carModel));
                if (carModel==null||carModel.getPaths().size()==0){
                    noData.setVisibility(View.VISIBLE);
                }else{
                    noData.setVisibility(View.GONE);
                }
            break;
            case 3:
                index1.setVisibility(View.INVISIBLE);
                index2.setVisibility(View.INVISIBLE);
                index3.setVisibility(View.VISIBLE);
                listView.setAdapter(new BusAdapter(getBaseContext(),walkModel));
                if (walkModel==null||walkModel.getPaths().size()==0){
                    noData.setVisibility(View.VISIBLE);
                }else{
                    noData.setVisibility(View.GONE);
                }
            break;
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //根据Type传输相应数据
                switch (type){
                    case 1:
                        Application.put("bus",busModel);
                    break;
                    case 2:
                        Application.put("bus",carModel);
                    break;
                    case 3:
                        Application.put("bus",walkModel);
                    break;
                }
                if (end.getText().toString().trim().equals("我的位置")){
                    Application.put("endLocation",Application.latlon);
                }else{
                    Application.put("endLocation",new LatLng(Application.mylocation.getLatitude(),Application.mylocation.getLongitude()));
                }
                Application.put("title",end.getText().toString().trim());
                Application.put("position",position);
                startActivity(new Intent(getBaseContext(),RouteDetaiAct.class));
            }
        });
    }
}
