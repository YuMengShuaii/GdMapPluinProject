package com.enation.javashop.mapparents;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.enation.javashop.map.location.Location;
import com.enation.javashop.map.model.PathModel;
import com.enation.javashop.map.monitor.Listener;
import com.enation.javashop.map.path.GetPathUtils;
import com.enation.javashop.map.utils.MapType;
import com.enation.javashop.mapparents.application.Application;
import com.enation.javashop.mapparents.fragment.MapFragment;
import com.enation.javashop.mapparents.utils.CheckPermissionsActivity;

public class MainActivity extends CheckPermissionsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MapFragment fragment = new MapFragment();
        fragmentTransaction.add(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }
}
