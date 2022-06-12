package com.example.kinch_home.instance;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 这是传感器的一个类
 * 传出x
 * 设置位置指针的走向
 */
public class SensorInstance implements SensorEventListener {
    private Context mContext;
    SensorManager mSensorManager;

    public SensorInstance(Context mContext) {
        this.mContext = mContext;
    }

    public void start() {
        //开启传感器服务
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {//防止有些手机不支持
            //拿到sensor对象,方向传感器
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            if (sensor != null) {
                mSensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    public void stop() {
        //停止服务
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float x = sensorEvent.values[SensorManager.DATA_X];
            if (mListener != null) {
                //把x传出去
                mListener.onOrientation(x);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /***
     * OnOrientationChangedListener 利用这个方法来将实时改变的x传出去
     */

    private OnOrientationChangedListener mListener;

    public void setOnOrientationChangedListener(OnOrientationChangedListener listener) {
        mListener = listener;
    }

    public static interface OnOrientationChangedListener {
        void onOrientation(float x);
    }
}
