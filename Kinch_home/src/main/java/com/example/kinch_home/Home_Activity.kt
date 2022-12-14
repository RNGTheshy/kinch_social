package com.example.kinch_home


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.*
import android.os.Bundle
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
import com.baidu.mapapi.map.MyLocationConfiguration
import com.baidu.mapapi.model.LatLng
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chaoshan.data_center.SettingsPreferencesDataStore.getCurrentUserObjetID
import com.chaoshan.data_center.activitymanger.ActivityManager
import com.chaoshan.data_center.friend.Friend
import com.chaoshan.data_center.friend.GetAllDataListener
import com.chaoshan.data_center.friend.GetAllMyFirendCallBack
import com.chaoshan.data_center.friend.GetAllUer
import com.chaoshan.data_center.togetname.getPersonal_data.getplace
import com.chaoshan.data_center.togetname.getPersonal_data.geturl
import com.chaoshan.socialforum.activity.SocialForumActivity
import com.example.chat.ChatActivity
import com.example.friend.friendMainActivity
import com.example.kinch_home.instance.SensorInstance
import com.example.setting.SettingMainActivity
import com.yubinma.person_center.PersonCenter2Activity
import com.yubinma.person_center.Personal_data
import kotlin.concurrent.thread


class Home_Activity : AppCompatActivity(), View.OnClickListener, ActivityManager.IRecordPage {
    private var friends = listOf<Friend>()
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

    // ????????????????????????
    private var isFirstLocate = true

    // ??????????????????
    private var locationMode: MyLocationConfiguration.LocationMode? = null

    // ???????????????
    private var mSensorInstance : SensorInstance ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //???????????????
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        window.navigationBarColor = resources.getColor(R.color.gray)
        //??????????????????
        SDKInitializer.setAgreePrivacy(application, true)
        SDKInitializer.initialize(application)
        //??????????????????
        LocationClient.setAgreePrivacy(true)
        setContentView(R.layout.activity_home_layout)

        //??????????????????
        mLocateButton = findViewById(R.id.locate)
        //?????????????????? ??????????????????
        mAddButton = findViewById(R.id.add)
        mAddButton?.setOnClickListener(this)
        //?????????????????? ??????????????????
        mMinusButton = findViewById(R.id.minus)
        mMinusButton?.setOnClickListener(this)
        //????????????????????????
        mMapView = findViewById(R.id.bmapView)
        // ????????????
        mBaiduMap = mMapView?.map
        // ??????????????????
        mBaiduMap?.isMyLocationEnabled = true
        //???????????????
        mLocationClient = LocationClient(this)
        //???????????????
        mTextView = findViewById(R.id.location_name)

        //????????????????????????
        mSettingButton = findViewById(R.id.setting)
        mMessageButton = findViewById(R.id.message)
        mTrendsButton = findViewById(R.id.trends)
        mFriendButton = findViewById(R.id.friend)
        mMeButton = findViewById(R.id.me)

        //??????LocationClientOption??????LocationClient????????????
        val option = LocationClientOption()
        option.isOpenGps = true // ??????gps
        option.setCoorType("bd09ll") // ??????????????????
        option.setScanSpan(2000)
        // ???????????????????????????
        option.setIsNeedAddress(true)
        //???????????????????????????????????????
        option.setIsNeedLocationDescribe(true)
        //??????locationClientOption
        mLocationClient!!.locOption = option

        //??????LocationListener?????????
        val myLocationListener: MyLocationListener = MyLocationListener()
        mLocationClient!!.registerLocationListener(myLocationListener)

        //?????????????????????
        ChatActivity.initUserChat(this@Home_Activity)

        initSensorInstance()
        hideAddMinusBtn()
        initClickListener()
        setFriendLocation()
    }

    private fun initSensorInstance() {
        mSensorInstance = SensorInstance(applicationContext)
        mSensorInstance!!.setOnOrientationChangedListener(object :
            SensorInstance.OnOrientationChangedListener {
            override fun onOrientation(x: Float) {
                //??????????????????
                if (mLocation == null) { //?????????????????????????????????????????????
                    return
                }
                //??????????????????
                // ??????????????????
                val locData = MyLocationData.Builder()
                    .accuracy(mLocation!!.radius) // ?????????????????????????????????????????????????????????0-360
                    .direction(x).latitude(mLocation!!.latitude)
                    .longitude(mLocation!!.longitude).build()

                // ??????????????????
                mBaiduMap!!.setMyLocationData(locData)

                // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                        .fromResource(R.drawable.navi_map_gps_locked);
                //????????????????????????
                val config = MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL,
                    true, null
                )
                mBaiduMap!!.setMyLocationConfiguration(config)
                /**
                 * ????????????????????????????????????????????????
                 */
                if (isFirstLocate) {
                    isFirstLocate = false
                    //????????????
                    val point = LatLng(mLocation!!.latitude, mLocation!!.longitude)
                    mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(point))
                }
            }
        })
    }

    private fun setFriendLocation() {
        mBaiduMap?.clear()
        GetAllUer.getAllFriend(
            getCurrentUserObjetID(),
            object : GetAllMyFirendCallBack {
                override fun success(list: List<String>) {
                    if(list.isNotEmpty())
                    GetAllUer.getFriendDao(object : GetAllDataListener {
                        override fun success(friendList: List<Friend>) {
                            friends = friendList //????????????
                            for (friend in friends) {
                                //??????????????????
                                getplace(friend.id) { longitude, latitude ->
                                    // ???????????????URL
                                    geturl(friend.id) { url ->
                                        thread {
                                            // ????????????
                                            val ll = LatLng(latitude, longitude)
                                            //????????????
                                            val roundedCorners = RoundedCorners(15)
                                            val option =
                                                RequestOptions.bitmapTransform(roundedCorners)
                                            //???????????????bitmap
                                            val b = Glide.with(this@Home_Activity)
                                                .asBitmap()
                                                .load(url)
                                                .apply(option)
                                                .submit(80, 80)
                                                .get()
                                            val bitmap = BitmapDescriptorFactory.fromBitmap(b)
                                            //????????????????????????
                                            val options = MarkerOptions().position(ll)
                                                .icon(bitmap)
                                            //????????????????????????
                                            mBaiduMap!!.addOverlay(options)
                                        }
                                    }


                                }
                            }

                        }

                        override fun fail() {

                        }
                    }, list)
                }

            })

    }

    //?????????????????????
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            flag = 0
            Toast.makeText(this@Home_Activity, "??????????????????", Toast.LENGTH_SHORT).show()
            val returnedId = data?.getStringExtra("id")
            val returnedLongitude = data?.getDoubleExtra("longitude", 115.416827)
            val returnedLatitude = data?.getDoubleExtra("latitude", 39.442078)
            val latLng = LatLng(returnedLatitude!!, returnedLongitude!!)
            //????????????
            val msu = MapStatusUpdateFactory.newLatLng(latLng)
            //???????????????????????????
            mBaiduMap!!.setMapStatus(msu)


//            // ?????????????????????
//            // ????????????????????????????????????????????? drawble ????????????
//            val mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.locate_icon)
//            // ???????????? ??????SDK???????????????????????????NORMAL???????????????, FOLLOWING???????????????, COMPASS???????????????
//            locationMode = MyLocationConfiguration.LocationMode.NORMAL
//            // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????5?????????????????????????????????????????????
//            val mLocationConfiguration = MyLocationConfiguration(
//                locationMode, true, mCurrentMarker,
//                R.color.teal_700, R.color.teal_700
//            )
//            // ???????????????????????????
//            mBaiduMap!!.setMyLocationConfiguration(mLocationConfiguration)


            val geocoder = Geocoder(this)
            val address =
                geocoder.getFromLocation(returnedLatitude, returnedLongitude, 100) //???????????????????????????????????????
            mAddress = address[0]
            //??????????????????
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

            // ??????????????????Marker????????????
            //mBaiduMap.addOverlay(options);


        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initClickListener() {
        mSettingButton?.setOnClickListener {
            val intent = Intent(this, SettingMainActivity::class.java)
            startActivity(intent)
        }
        mMessageButton?.setOnClickListener {
            ChatActivity.goToChat(this@Home_Activity)
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

    // ???????????????BDAbstractListener????????????onReceieveLocation?????????????????????????????????????????????MapView
    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //mapView ???????????????????????????????????????
            if (mMapView == null) {
                return
            }
            mLocation = location
            // ????????????????????????
            val ll = LatLng(location.latitude, location.longitude)
            mLongitude = location.longitude
            mLatitude = location.latitude
            if (isFirstLocate) {
                val userId = getCurrentUserObjetID()
                val personal_data = Personal_data()
                personal_data.saveplace(mLongitude, mLatitude, userId)
            }

//            val locData = MyLocationData.Builder()
//                .accuracy(location.radius) // ?????????????????????????????????????????????????????????0-360
//                .direction(location.direction).latitude(location.latitude)
//                .longitude(location.longitude).build()
//            mBaiduMap!!.setMyLocationData(locData)
            mapStatus = mBaiduMap?.mapStatus
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
            }
//            // ?????????????????????
//            // ????????????????????????????????????????????? drawble ????????????
//            val mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.locate_icon)
//            // ???????????? ??????SDK???????????????????????????NORMAL???????????????, FOLLOWING???????????????, COMPASS???????????????
//            locationMode = MyLocationConfiguration.LocationMode.NORMAL
//            // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????5?????????????????????????????????????????????
//            val mLocationConfiguration = MyLocationConfiguration(
//                locationMode, true, mCurrentMarker,
//                R.color.teal_700, R.color.teal_700
//            )
//            // ???????????????????????????
//            mBaiduMap!!.setMyLocationConfiguration(mLocationConfiguration)

            // ??????????????????????????????
            mLocateButton?.setOnClickListener {
                flag = 1
                //????????????
                val userId = getCurrentUserObjetID()
                val personal_data = Personal_data()
                personal_data.saveplace(mLongitude, mLatitude, userId)
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

                setFriendLocation()
            }
        }
    }

    override fun onStart() {
        //????????????????????????
        mLocationClient!!.start()
        mSensorInstance!!.start()
        super.onStart()
    }

    override fun onStop() {
        mLocationClient!!.stop()
        mSensorInstance!!.stop()
        super.onStop()
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


    fun hideAddMinusBtn() {
        mMapView?.showZoomControls(false) //????????????UI
        mMapView?.showScaleControl(false)
    }

    override fun onClick(v: View?) {
        when (v) {
            //????????????
            mAddButton -> {
                mapStatus = mBaiduMap?.mapStatus
                mBaiduMap?.setMapStatus(MapStatusUpdateFactory.zoomTo(mapStatus?.zoom!!.plus(1)))
                // ???????????? ????????????
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
            //????????????
            mMinusButton -> {
                mapStatus = mBaiduMap?.mapStatus
                mBaiduMap?.setMapStatus(MapStatusUpdateFactory.zoomTo(mapStatus?.zoom!!.minus(1)))
                // ???????????? ????????????
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
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK //?????????????????????????????????
            context.startActivity(intent)
        }

        fun goToByActivity(context: Context) {
            val intent: Intent =

                Intent(context, Home_Activity::class.java)

            context.startActivity(intent)
        }
    }
}


