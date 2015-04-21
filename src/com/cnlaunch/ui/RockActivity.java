package com.cnlaunch.ui;

import android.app.Activity;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

//实现SensorEventListener接口 可以实现多个接口，但是只能继承一个类，
public class RockActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Vibrator mVibrator;
	private final int ROCKPOWER = 15;// 这是传感器系数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 获取传感器管理服务
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// 震动服务
		mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE); // 震动需要在androidmainfest里面注册哦亲
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 加速度传感器
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		// 还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
		// 根据不同应用，需要的反应速率不同，具体根据实际情况设定
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);// 退出界面后，把传感器释放。
		super.onStop();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		// values[0]:X轴，values[1]：Y轴，values[2]：Z轴
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			// 在 这个if里面写监听，写要摇一摇干么子，知道么？猪头~~~
			if ((Math.abs(values[0]) > ROCKPOWER || Math.abs(values[1]) > ROCKPOWER || Math.abs(values[2]) > ROCKPOWER)) {
				System.out.println("YYYYYYYYYYYY   Math.abs(values[0]=" + Math.abs(values[0]) + "     Math.abs(values[1]=" + Math.abs(values[1]) + "       Math.abs(values[2]" + Math.abs(values[2]));
				mVibrator.vibrate(500);// 设置震动。
				Toast.makeText(RockActivity.this, "你丫再摇一下试试~~~~！！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

}
