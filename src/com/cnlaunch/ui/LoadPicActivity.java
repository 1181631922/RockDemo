package com.cnlaunch.ui;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class LoadPicActivity extends Activity implements OnClickListener {
	private Button loadpic;
	private final static String ALBUM_PATH = Environment.getExternalStorageDirectory()
			+ "/fanyafeng/";
	private ImageView mImageView;
	private ProgressDialog mSaveDialog = null;
	private Bitmap mBitmap;
	private String mFileName;
	private String mSaveMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_pic);

		initView();

		new Thread(connectNet).start();
		
//		new Handler().postDelayed(new Runnable(){
//	  		@Override
//	  		public void run(){
//	  			new Thread(saveFileRunnable).start();
//	  		}
//	  	}, 5000);
		

	}

	private void initView() {
		this.loadpic = (Button) this.findViewById(R.id.loadpic);
		this.loadpic.setOnClickListener(this);
	}

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return readStream(inStream);
		}
		return null;
	}

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return InputStream
	 * @throws Exception
	 */
	public InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}

	/**
	 * Get data from stream
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(ALBUM_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(ALBUM_PATH + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	private Runnable saveFileRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				saveFile(mBitmap, mFileName);
				mSaveMessage = "图片保存成功！";
			} catch (IOException e) {
				mSaveMessage = "图片保存失败！";
				e.printStackTrace();
			}
			messageHandler.sendMessage(messageHandler.obtainMessage());
		}
	};

	private Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
//			mSaveDialog.dismiss();
			Toast.makeText(LoadPicActivity.this, mSaveMessage,
					Toast.LENGTH_SHORT).show();
		}
	};
	/*
	 * 连接网络 由于在4.0中不允许在主线程中访问网络，所以需要在子线程中访问
	 */
	private Runnable connectNet = new Runnable() {
		@Override
		public void run() {
			try {
				String filePath = "http://a4.qpic.cn/psb?/V12cYG6y4OfMYd/ClrxP.s0MLWcT2Lg6E0JEqupDgPeM2HAU1VhSnniV0A!/m/dGMAAAAAAAAAnull&bo=IwGhAQAAAAADB6A!&rf=photolist&t=5";
				mFileName = "fanyafeng.png";

				// 以下是取得图片的两种方法
				// ////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap
				byte[] data = getImage(filePath);
				if (data != null) {
					mBitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);// bitmap
				} else {
					Toast.makeText(LoadPicActivity.this, "Image error!", 1)
							.show();
				}
				// ******** 方法2：取得的是InputStream，直接从InputStream生成bitmap
				mBitmap = BitmapFactory.decodeStream(getImageStream(filePath));
				// 发送消息，通知handler在主线程中更新UI
			} catch (Exception e) {
				Toast.makeText(LoadPicActivity.this, "无法链接网络！", 1).show();
				e.printStackTrace();
			}

		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.loadpic:
//			mSaveDialog = ProgressDialog.show(LoadPicActivity.this, "保存图片","图片正在保存中，请稍等...", true);
			new Thread(saveFileRunnable).start();
			break;
		default:
			break;
		}

	}

}
