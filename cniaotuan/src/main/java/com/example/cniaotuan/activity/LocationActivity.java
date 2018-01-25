package com.example.cniaotuan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.example.cniaotuan.R;

/***
 * 集成POI的LBS检索的步骤:
 * 1.需要实现CloudListener
 * 2.初始化CloudManager
 * 3.实现检索poi功能(本地,周边,详情,矩形)
 * 4.在检索的回调结果中添加覆盖物
 *
 * 运行菜鸟团购项目需要注意的问题:
 * 1.修改百度地图的app-key值
 * 2.修改sha1值
 * 3.要注意包名是否正确
 * 4.集成poi检索必须要修改为自己申请的服务端key
 *
 */

public class LocationActivity extends AppCompatActivity implements CloudListener{

    // 定位相关
    LocationClient mLocClient;

    public MyLocationListenner myListener = new MyLocationListenner();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_location);
        CloudManager.getInstance().init(LocationActivity.this);

        initBaiduMap();
    }

    public void checkLBS(View view) {
        NearbySearchInfo info = new NearbySearchInfo();
        info.ak="TB2Eblvm7yajBvShbIvTfUtIcG1aSRVN";
        info.geoTableId = 143362;
        info.radius = 30000;
        info.location = "118.167012,24.518131";
        CloudManager.getInstance().nearbySearch(info);
    }


    //初始化地图
    private void initBaiduMap() {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    //检索的回调结果
    @Override
    public void onGetSearchResult(CloudSearchResult result, int i) {
        if (result != null && result.poiList != null && result.poiList.size() > 0){
            //先清除
            mBaiduMap.clear();
            LatLng latLng;
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (CloudPoiInfo info : result.poiList){
                latLng = new LatLng(info.latitude, info.longitude);
                MarkerOptions options = new MarkerOptions().icon(bd).position(latLng);
                mBaiduMap.addOverlay(options);
                builder.include(latLng);
            }
        }


    }

    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {

    }


    /***
     * 实现定位的sdk监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (mMapView == null || location == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }


}
