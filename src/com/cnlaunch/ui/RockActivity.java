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

//ʵ��SensorEventListener�ӿ� ����ʵ�ֶ���ӿڣ�����ֻ�ܼ̳�һ���࣬
public class RockActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Vibrator mVibrator;
	private final int ROCKPOWER = 15;// ���Ǵ�����ϵ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// ��ȡ�������������
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// �𶯷���
		mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE); // ����Ҫ��androidmainfest����ע��Ŷ��
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ���ٶȴ�����
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		// ����SENSOR_DELAY_UI��SENSOR_DELAY_FASTEST��SENSOR_DELAY_GAME�ȣ�
		// ���ݲ�ͬӦ�ã���Ҫ�ķ�Ӧ���ʲ�ͬ���������ʵ������趨
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);// �˳�����󣬰Ѵ������ͷš�
		super.onStop();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		// values[0]:X�ᣬvalues[1]��Y�ᣬvalues[2]��Z��
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			// �� ���if����д������дҪҡһҡ��ô�ӣ�֪��ô����ͷ~~~
			if ((Math.abs(values[0]) > ROCKPOWER || Math.abs(values[1]) > ROCKPOWER || Math.abs(values[2]) > ROCKPOWER)) {
				System.out.println("YYYYYYYYYYYY   Math.abs(values[0]=" + Math.abs(values[0]) + "     Math.abs(values[1]=" + Math.abs(values[1]) + "       Math.abs(values[2]" + Math.abs(values[2]));
				mVibrator.vibrate(500);// �����𶯡�
				Toast.makeText(RockActivity.this, "��Ѿ��ҡһ������~~~~����", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

}
