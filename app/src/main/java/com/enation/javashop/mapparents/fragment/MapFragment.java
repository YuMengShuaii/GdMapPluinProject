package com.enation.javashop.mapparents.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.enation.javashop.map.location.Location;
import com.enation.javashop.map.monitor.Listener;
import com.enation.javashop.mapparents.R;
import com.enation.javashop.mapparents.activity.Map;
import com.enation.javashop.mapparents.activity.QueryActivity;
import com.enation.javashop.mapparents.activity.QueryMapListActivity;
import com.enation.javashop.mapparents.application.Application;

/**
 * Created by LDD on 17/3/8.
 */

public class MapFragment extends Fragment {
    private TextView textView;
    private Button getlocation,getlocations,stop,toMap,find,findList;
    private Location location;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_lay,null);
        textView = (TextView) view.findViewById(R.id.location);
        getlocation = (Button)view.findViewById(R.id.get);
        getlocations = (Button) view.findViewById(R.id.gets);
        find = (Button)view. findViewById(R.id.toQuery);
        toMap = (Button) view.findViewById(R.id.toMap);
        stop = (Button) view.findViewById(R.id.stop);
        findList = (Button)view.findViewById(R.id.toQueryList);
        location = Location.getLocation();
        findList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),QueryMapListActivity.class));
            }
        });
        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestLocation(true);
            }
        });
        getlocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestLocation(false);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.stopLocation();
            }
        });
        toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Application.put("title","大保健");
                startActivity(new Intent(getActivity(),Map.class));
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Application.put("title","大保健");
                startActivity(new Intent(getActivity(),QueryActivity.class));
            }
        });
        TestLocation(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        location.stopLocation();
        super.onDestroyView();
    }

    /**
     * 测试定位方法
     * @param isSingle true执行单次定位 false 多次不间断定位
     */
    private void TestLocation(boolean isSingle){
        location.getLocationDetail(getActivity(),isSingle, new Listener.DataLisener<AMapLocation>() {
            @Override
            public void success(AMapLocation aMapLocation) {
                Application.mylocation = aMapLocation;
                textView.setText(aMapLocation.getLatitude()+"  "+aMapLocation.getLongitude());
                Toast.makeText(getActivity(),aMapLocation.getAoiName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void faild(String error) {
                Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
