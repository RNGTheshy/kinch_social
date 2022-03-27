package com.example.kinch_home

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.map.MapStatusUpdateFactory

import com.baidu.mapapi.map.MapStatusUpdate




class Home_Activity : AppCompatActivity() {
    private var mMapView: MapView? = null
    private var mBaiduMap: BaiduMap? = null
    private var mLocationClient: LocationClient? = null
    private var mTextView: TextView? = null
    private var mLatitude = 0.0
    private var mLongitude = 0.0
    private var mLocateButton : ImageButton? = null
    private var mAddButton : ImageButton? = null
    private var mMinusButton : ImageButton? = null
    private var mapStatus :MapStatus ? =null
    // 是否是第一次定位
    private var isFirstLocate = true

    // 当前定位模式
    private var locationMode: MyLocationConfiguration.LocationMode? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED //设置为竖屏
        window.navigationBarColor = resources.getColor(R.color.gray)
        SDKInitializer.setAgreePrivacy(applicationContext, true)
        SDKInitializer.initialize(applicationContext)
        LocationClient.setAgreePrivacy(true);
        setContentView(R.layout.activity_home_layout)

        //获取定位按钮
        mLocateButton = findViewById(R.id.locate)
        //获取放大缩小按钮
        mAddButton = findViewById(R.id.add)
        mMinusButton =findViewById(R.id.minus)
        //获取地图控件引用
        mMapView = findViewById(R.id.bmapView)
        // 得到地图
        mBaiduMap = mMapView?.map
        // 开启定位图层
        mBaiduMap?.isMyLocationEnabled = true
        //定位初始化
        mLocationClient = LocationClient(this)
        //标题初始化
        mTextView = findViewById(R.id.location_name)

        //通过LocationClientOption设置LocationClient相关参数
        val option = LocationClientOption()
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(1000)
        // 可选，设置地址信息
        option.setIsNeedAddress(true)
        //可选，设置是否需要地址描述
        option.setIsNeedLocationDescribe(true)
        //设置locationClientOption
        mLocationClient!!.locOption = option

        //注册LocationListener监听器
        val myLocationListener: MyLocationListener = MyLocationListener()
        mLocationClient!!.registerLocationListener(myLocationListener)
        //开启地图定位图层
        mLocationClient!!.start()

        initAddMinusBtn()
    }

    // 继承抽象类BDAbstractListener并重写其onReceieveLocation方法来获取定位数据，并将其传给MapView
    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return
            }

            // 如果是第一次定位
            val ll = LatLng(location.latitude, location.longitude)
            if (isFirstLocate) {
                isFirstLocate = false
                //给地图设置状态
                mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll))
            }
            mLongitude=location.longitude
            mLatitude=location.latitude
            val locData = MyLocationData.Builder()
                .accuracy(location.radius) // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.direction).latitude(location.latitude)
                .longitude(location.longitude).build()
            mBaiduMap!!.setMyLocationData(locData)

            // ------------------  以下是可选部分 ------------------
            // 自定义地图样式，可选
            // 更换定位图标，这里的图片是放在 drawble 文件下的
            val mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.locate)
            // 定位模式 地图SDK支持三种定位模式：NORMAL（普通态）, FOLLOWING（跟随态）, COMPASS（罗盘态）
            locationMode = MyLocationConfiguration.LocationMode.NORMAL
            // 定位模式、是否开启方向、设置自定义定位图标、精度圈填充颜色以及精度圈边框颜色5个属性（此处只设置了前三个）。
            val mLocationConfiguration = MyLocationConfiguration(
                locationMode, true, mCurrentMarker,
                R.color.teal_700, R.color.teal_700
            )
            // 使自定义的配置生效
            mBaiduMap!!.setMyLocationConfiguration(mLocationConfiguration)
            // ------------------  可选部分结束 ------------------

            //设置标题
            mTextView?.setText(location.getDistrict())
            //设置定位按钮点击事件
            mLocateButton?.setOnClickListener {
                getMyLocation()
                Toast.makeText(this@Home_Activity, location.addrStr,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        mMapView!!.onResume()
        super.onResume()
    }

    override fun onPause() {
        mMapView!!.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mLocationClient!!.stop()
        mBaiduMap!!.isMyLocationEnabled = false
        mMapView!!.onDestroy()
        mMapView = null
        super.onDestroy()
    }

    fun getMyLocation() {
        val latLng = LatLng(mLatitude, mLongitude)
        val msu = MapStatusUpdateFactory.newLatLng(latLng)
        mBaiduMap!!.setMapStatus(msu)
    }
    fun initAddMinusBtn(){
        mMapView?.showZoomControls(false) //隐藏原有UI

        mAddButton?.setOnClickListener {
            mapStatus=mBaiduMap?.mapStatus;
            mBaiduMap?.setMapStatus(MapStatusUpdateFactory.zoomTo(mapStatus?.zoom!!.plus(1)))
        }

        mMinusButton?.setOnClickListener {
            mapStatus=mBaiduMap?.mapStatus;
            mBaiduMap?.setMapStatus(MapStatusUpdateFactory.zoomTo(mapStatus?.zoom!!.minus(1)))
        }

    }
}


