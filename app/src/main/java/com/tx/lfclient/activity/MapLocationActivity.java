package com.tx.lfclient.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.event.LocationEvent;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/20
 */
@ContentView(R.layout.activity_map_location)
public class MapLocationActivity extends ImpActivity {
    @ViewInject(R.id.mapView)
    private MapView mapView;
    @ViewInject(R.id.location_center)
    private ImageView center_mark;
    @ViewInject(R.id.location_name)
    private TextView location_name;
    private BaiduMap baiduMap;
    private GeoCoder geoCoder;
    private LocationClient locationClient;

    private double centerLatitude;

    private double centerLongitude;

    private String locationName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState, this);
        try{
            init();
            initData();
            initView();

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    void init() {
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        geoCoder = GeoCoder.newInstance();
        locationClient = new LocationClient(this);

    }

    @Override
    void initData() {
        baiduMap.setMyLocationEnabled(true);
    }

    @Override
    void initView() {
        baiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);
        baiduMap.setOnMapRenderCallbadk(onMapRenderCallback);
        geoCoder.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
        locationClient.registerLocationListener(bdLocationListener);
        initLocation();
        locationClient.start();
    }

    @Event(type = View.OnClickListener.class, value = {R.id.map_location_back, R.id.map_location_finish})
    private void onClick(View v) {
        LocationEvent event = new LocationEvent();
        event.setLatitude(centerLatitude);
        event.setLongitude(centerLongitude);
        event.setLocationName(locationName);
        EventBus.getDefault().post(event);
        myApplication.finishActivity(MapLocationActivity.class);

    }


    private BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {

            if(mapStatus==null){
                return;
            }
            TranslateAnimation alphaAnimation = new TranslateAnimation(0f, 0f, 0f, -40f);
            alphaAnimation.setDuration(200);  //设置时间
            //为重复执行的次数。如果设置为n，则动画将执行n+1次。Animation.INFINITE为无限制播放
            alphaAnimation.setRepeatCount(1); //设置按钮弹跳的次数，也就是快慢
//		    alphaAnimation2.setRepeatCount(Animation.INFINITE);

            //为动画效果的重复模式，常用的取值如下。RESTART：重新从头开始执行。REVERSE：反方向执行
            alphaAnimation.setRepeatMode(Animation.REVERSE);
            alphaAnimation.setInterpolator(new LinearInterpolator());//动画结束的时候弹起

            center_mark.setAnimation(alphaAnimation);
            alphaAnimation.start();

            LatLng latLng = mapStatus.target;
            centerLatitude = latLng.latitude;
            centerLongitude = latLng.longitude;
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(centerLatitude, centerLongitude)));
        }
    };


    private OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            locationName = reverseGeoCodeResult.getAddress();
            location_name.setText(locationName);
        }
    };

    private BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                ToastShow.showShort(MapLocationActivity.this, "定位失败");
                return;
            }

            try{

                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();

                // 设置定位数据
                baiduMap.setMyLocationData(locData);
                // 设置定位数据
                centerLatitude = bdLocation.getLatitude();
                centerLongitude = bdLocation.getLongitude();
                LatLng ll = new LatLng(centerLatitude, centerLongitude);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                baiduMap.animateMapStatus(u);
                locationName = bdLocation.getAddrStr();
                location_name.setText(locationName);
                baiduMap.setMyLocationEnabled(false);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        locationClient.setLocOption(option);
    }

    private BaiduMap.OnMapRenderCallback onMapRenderCallback = new BaiduMap.OnMapRenderCallback() {
        @Override
        public void onMapRenderFinished() {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
