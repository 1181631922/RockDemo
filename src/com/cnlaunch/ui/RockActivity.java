package com.cnlaunch.ui;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.res.XmlResourceParser;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class RockActivity extends Activity implements SensorEventListener,OnClickListener {
	private SensorManager mSensorManager;
	private Vibrator mVibrator;
	private final int ROCKPOWER = 15;
	private Button share;
//	private XmlResourceParser getPic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initView();
		initRock();
	}

	private void initView() {
		this.share = (Button) this.findViewById(R.id.share);
		this.share.setOnClickListener(this);
//		this.getPic=RockActivity.this.getResources().getLayout(R.drawable.fanyafeng);
	}
	
	private void initRock(){
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.share:
			showShare();
			break;

		default:
			break;
		}

	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://eqxiu.com/s/4NdsxskD");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("樊亚风测试分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/fanyafeng.png");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://eqxiu.com/s/4NdsxskD");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("樊亚风测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://eqxiu.com/s/4NdsxskD");

		// 启动分享GUI
		oks.show(this);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        // 因为Android版本问题，不得不用最新的方法，不然根本编译不过去
        if (mSensorManager != null) {// 注册监听器
            mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }
	
	@Override
    protected void onStop() {
        super.onStop();
        if (mSensorManager != null) {// 取消监听器
            mSensorManager.unregisterListener(sensorEventListener);
        }
    }
	
	 /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 如果不敏感请自行调低该数值,低于10的话就不行了,因为z轴上的加速度本身就已经达到10了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                mVibrator.vibrate(200);
                Message msg = new Message();
                msg.what = ROCKPOWER;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    
    /**
     * 动作执行
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ROCKPOWER:
                    Toast.makeText(RockActivity.this, "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };
    
    


	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
