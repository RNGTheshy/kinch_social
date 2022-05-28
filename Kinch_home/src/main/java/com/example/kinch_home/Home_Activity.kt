package com.example.kinch_home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
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
import com.chaoshan.data_center.SettingsPreferencesDataStore.getCurrentUserObjetID
import com.chaoshan.data_center.activitymanger.ActivityManager
import com.chaoshan.data_center.togetname.Headport
import com.chaoshan.socialforum.activity.SocialForumActivity
import com.example.chat.ChatActivity
import com.example.chat.ChatActivity.Companion.goToChat
import com.example.friend.friendMainActivity
import com.example.setting.SettingMainActivity
import com.yubinma.person_center.PersonCenter2Activity
import com.yubinma.person_center.Personal_data
import com.baidu.platform.comapi.basestruct.GeoPoint
import android.location.*


import com.baidu.mapapi.map.MarkerOptions


class Home_Activity : AppCompatActivity(), View.OnClickListener, ActivityManager.IRecordPage {
    private var mAddress: Address? = null
    private var flag = 1
    private var mLocation: BDLocation? = null
    private var mMapView: MapView? = null
    private var mBaiduMap: BaiduMap? = null
    private var mLocationClient: LocationClient? = null
    private var mTextView: TextView? = null
    private var mLatitude = 0.0
    private var mLongitude = 0.0
    private var mLocateButton: ImageButton? = null
    private var mAddButton: ImageButton? = null
    private var mMinusButton: ImageButton? = null
    private var mSettingButton: ImageButton? = null
    private var mMessageButton: ImageButton? = null
    private var mTrendsButton: ImageButton? = null
    private var mFriendButton: ImageButton? = null
    private var mMeButton: ImageButton? = null
    private var mapStatus: MapStatus? = null

    // 是否是第一次定位
    private var isFirstLocate = true

    // 当前定位模式
    private var locationMode: MyLocationConfiguration.LocationMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置为竖屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        window.navigationBarColor = resources.getColor(R.color.gray)
        //提供隐私权限
        SDKInitializer.setAgreePrivacy(application, true)
        SDKInitializer.initialize(application)
        //提供隐私权限
        LocationClient.setAgreePrivacy(true)
        setContentView(R.layout.activity_home_layout)

        //获取定位按钮
        mLocateButton = findViewById(R.id.locate)
        //获取放大按钮 设置点击事件
        mAddButton = findViewById(R.id.add)
        mAddButton?.setOnClickListener(this)
        //获取缩小按钮 设置点击事件
        mMinusButton = findViewById(R.id.minus)
        mMinusButton?.setOnClickListener(this)
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

        //初始化主界面按钮
        mSettingButton = findViewById(R.id.setting)
        mMessageButton = findViewById(R.id.message)
        mTrendsButton = findViewById(R.id.trends)
        mFriendButton = findViewById(R.id.friend)
        mMeButton = findViewById(R.id.me)

        //通过LocationClientOption设置LocationClient相关参数
        val option = LocationClientOption()
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(100)
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

        hideAddMinusBtn()
        initClickListener()

    }

    //    fun getLocationInfo(longitude: String, latitude: String): MutableMap<*, *>? {
//        val ak ="Aq3e7gMjuyBfnc4no0N0vTtRmHYXyBVo"
//        val urlString =
//            "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + ak + "&output=json&coordtype=wgs84ll&location=" + latitude + "," + longitude
//        val res = StringBuilder()
//        var `in`: BufferedReader? = null
//        try {
//            val url = URL(urlString)
//            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
//            conn.setDoOutput(true)
//            conn.setRequestMethod("GET")
//            `in` = BufferedReader(InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))
//            var line: String?
//            while (`in`.readLine().also { line = it } != null) {
//                res.append(line).append("\n")
//            }
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        } finally {
//            if (`in` != null) {
//                try {
//                    `in`.close()
//                } catch (e: IOException) {
//                    throw RuntimeException(e)
//                }
//            }
//        }
//        return JSON.parseObject(res.toString(), MutableMap::class.java)
//    }
    //跳转到朋友定位
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            flag = 0
            Toast.makeText(this@Home_Activity, "朋友定位成功", Toast.LENGTH_SHORT).show()
            mBaiduMap?.clear()
            val bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.locate_icon);
            val returnedLongitude = data?.getDoubleExtra("longitude", 115.416827)
            val returnedLatitude = data?.getDoubleExtra("latitude", 39.442078)
            val returnedId = data?.getStringExtra("id")
            val latLng = LatLng(returnedLatitude!!, returnedLongitude!!)
            val msu = MapStatusUpdateFactory.newLatLng(latLng)
            mBaiduMap!!.setMapStatus(msu)
//            val locData = MyLocationData.Builder()
//                .latitude(returnedLatitude)
//                .longitude(returnedLongitude).build()
//            mBaiduMap!!.setMyLocationData(locData)
            val geocoder = Geocoder(this)
            val address = geocoder.getFromLocation(returnedLatitude, returnedLongitude, 100)
            // 设置标题
            mAddress = address[0]
            if (mapStatus?.zoom!! < 5)
                mTextView?.text = mAddress?.countryName
            else if (mapStatus?.zoom!! < 7)
                mTextView?.text = mAddress?.adminArea
            else if (mapStatus?.zoom!! < 10)
                mTextView?.text = mAddress?.locality
            else if (mapStatus?.zoom!! < 14)
                mTextView?.text = mAddress?.featureName
            else
                mTextView?.text = mAddress?.featureName
            val options = MarkerOptions().position(latLng)
                .icon(bitmap)
            // 在地图上添加Marker，并显示
            //mBaiduMap.addOverlay(options);

            mBaiduMap!!.addOverlay(options)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initClickListener() {
        mSettingButton?.setOnClickListener {
            val intent = Intent(this, SettingMainActivity::class.java)
            startActivity(intent)

        }
        mMessageButton?.setOnClickListener {
            goToChat(this, "147", "147")
//            val intent = Intent(this, ChatActivity::class.java)
//            startActivity(intent)

        }
        mTrendsButton?.setOnClickListener {
            val intent = Intent(this, SocialForumActivity::class.java)
            startActivity(intent)

        }
        mFriendButton?.setOnClickListener {
            val intent = Intent(this, friendMainActivity::class.java)
            startActivityForResult(intent, 1)
        }
        mMeButton?.setOnClickListener {
            val intent = Intent(this, PersonCenter2Activity::class.java)
            startActivity(intent)

        }

    }

    // 继承抽象类BDAbstractListener并重写其onReceieveLocation方法来获取定位数据，并将其传给MapView
    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //mapView 销毁后不在处理新接收的位置
            if (mMapView == null) {
                return
            }
            mLocation = location
            // 如果是第一次定位
            val ll = LatLng(location.latitude, location.longitude)
            mLongitude = location.longitude
            mLatitude = location.latitude
            if (isFirstLocate) {
                isFirstLocate = false
                //给地图设置状态
                mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll))
                //存经纬度
                val userId = getCurrentUserObjetID()
                val personal_data = Personal_data()
                personal_data.saveplace(mLongitude, mLatitude, userId);
            }
            val locData = MyLocationData.Builder()
                .accuracy(location.radius) // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.direction).latitude(location.latitude)
                .longitude(location.longitude).build()
            mBaiduMap!!.setMyLocationData(locData)
            mapStatus = mBaiduMap?.mapStatus
            if(flag == 1){
                if (mapStatus?.zoom!! < 5)
                    mTextView?.text = mLocation?.country
                else if (mapStatus?.zoom!! < 7)
                    mTextView?.text = mLocation?.province
                else if (mapStatus?.zoom!! < 10)
                    mTextView?.text = mLocation?.city
                else if (mapStatus?.zoom!! < 14)
                    mTextView?.text = mLocation?.district
                else
                    mTextView?.text = mLocation?.street
            }
//            // 自定义地图样式
//            // 更换定位图标，这里的图片是放在 drawble 文件下的
//            val mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.locate_icon)
//            // 定位模式 地图SDK支持三种定位模式：NORMAL（普通态）, FOLLOWING（跟随态）, COMPASS（罗盘态）
//            locationMode = MyLocationConfiguration.LocationMode.NORMAL
//            // 定位模式、是否开启方向、设置自定义定位图标、精度圈填充颜色以及精度圈边框颜色5个属性（此处只设置了前三个）。
//            val mLocationConfiguration = MyLocationConfiguration(
//                locationMode, true, mCurrentMarker,
//                R.color.teal_700, R.color.teal_700
//            )
//            // 使自定义的配置生效
//            mBaiduMap!!.setMyLocationConfiguration(mLocationConfiguration)

            // 设置定位按钮点击事件
            mLocateButton?.setOnClickListener {
                flag = 1
                mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll))
                if (mapStatus?.zoom!! < 5)
                    mTextView?.text = mLocation?.country
                else if (mapStatus?.zoom!! < 7)
                    mTextView?.text = mLocation?.province
                else if (mapStatus?.zoom!! < 10)
                    mTextView?.text = mLocation?.city
                else if (mapStatus?.zoom!! < 14)
                    mTextView?.text = mLocation?.district
                else
                    mTextView?.text = mLocation?.street
                Toast.makeText(this@Home_Activity, location.addrStr, Toast.LENGTH_SHORT).show()
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

    // get My Location
    fun getMyLocation() {
        val latLng = LatLng(mLatitude, mLongitude)
        val msu = MapStatusUpdateFactory.newLatLng(latLng)
        mBaiduMap!!.setMapStatus(msu)
    }

    fun hideAddMinusBtn() {
        mMapView?.showZoomControls(false) //隐藏原有UI
        mMapView?.showScaleControl(false)
    }

    override fun onClick(v: View?) {
        when (v) {
            //放大按钮
            mAddButton -> {
                mapStatus = mBaiduMap?.mapStatus
                mBaiduMap?.setMapStatus(MapStatusUpdateFactory.zoomTo(mapStatus?.zoom!!.plus(1)))
                // 设置标题 动态加载
                if (flag == 1) {
                    if (mapStatus?.zoom!! < 5)
                        mTextView?.text = mLocation?.country
                    else if (mapStatus?.zoom!! < 7)
                        mTextView?.text = mLocation?.province
                    else if (mapStatus?.zoom!! < 10)
                        mTextView?.text = mLocation?.city
                    else if (mapStatus?.zoom!! < 14)
                        mTextView?.text = mLocation?.district
                    else
                        mTextView?.text = mLocation?.street
                } else if (flag == 0) {
                    if (mapStatus?.zoom!! < 5)
                        mTextView?.text = mAddress?.countryName
                    else if (mapStatus?.zoom!! < 7)
                        mTextView?.text = mAddress?.adminArea
                    else if (mapStatus?.zoom!! < 10)
                        mTextView?.text = mAddress?.locality
                    else if (mapStatus?.zoom!! < 14)
                        mTextView?.text = mAddress?.featureName
                    else
                        mTextView?.text = mAddress?.featureName
                }

            }
            //缩小按钮
            mMinusButton -> {
                mapStatus = mBaiduMap?.mapStatus
                mBaiduMap?.setMapStatus(MapStatusUpdateFactory.zoomTo(mapStatus?.zoom!!.minus(1)))
                // 设置标题 动态加载
                if (flag == 1) {
                    if (mapStatus?.zoom!! < 5)
                        mTextView?.text = mLocation?.country
                    else if (mapStatus?.zoom!! < 7)
                        mTextView?.text = mLocation?.province
                    else if (mapStatus?.zoom!! < 10)
                        mTextView?.text = mLocation?.city
                    else if (mapStatus?.zoom!! < 14)
                        mTextView?.text = mLocation?.district
                    else
                        mTextView?.text = mLocation?.street
                } else if (flag == 0) {
                    if (mapStatus?.zoom!! < 5)
                        mTextView?.text = mAddress?.countryName
                    else if (mapStatus?.zoom!! < 7)
                        mTextView?.text = mAddress?.adminArea
                    else if (mapStatus?.zoom!! < 10)
                        mTextView?.text = mAddress?.locality
                    else if (mapStatus?.zoom!! < 14)
                        mTextView?.text = mAddress?.featureName
                    else
                        mTextView?.text = mAddress?.featureName
                }
            }
        }

    }

    companion object {
        fun goTo(context: Context) {
            val intent = Intent(context, Home_Activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun goToByActivity(context: Context) {
            val intent: Intent =

                Intent(context, Home_Activity::class.java)

            context.startActivity(intent)
        }
    }
}


