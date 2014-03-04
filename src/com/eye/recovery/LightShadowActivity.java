package com.eye.recovery;

import java.util.Timer;
import java.util.TimerTask;

import com.eye.recovery.R;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class LightShadowActivity extends Activity {
	boolean mLightShadowIsStart = false;
	Camera mCamera = null;
	Timer mTimer = null;
	int mFlashTime = 5000;
	boolean mIsLight = false;
	
	void LightCtrl(boolean Open)
	{
		if(mCamera==null)return;
		Parameters p = mCamera.getParameters();
		p.setFlashMode(Open ? Parameters.FLASH_MODE_TORCH : Parameters.FLASH_MODE_OFF);
		mCamera.setParameters(p);
	}
	
	SurfaceTexture mSt = new SurfaceTexture(0);
	boolean OpenCamera()
	{
		CloseCamera();
		try
		{
			mCamera = Camera.open();
			mCamera.setPreviewTexture(mSt);
			mCamera.startPreview();
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
	void CloseCamera()
	{
		if(mCamera!=null)
		{
			try{
			mCamera.stopPreview();
			mCamera.release();
			}
			catch(Exception e)
			{
				;
			}
			mCamera = null;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button ButtonLightShadow = (Button)findViewById(R.id.buttonLightShadow);
		ButtonLightShadow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(v.getId())
				{
				case R.id.buttonLightShadow:
					if(mLightShadowIsStart)
					{
						CloseCamera();
						mTimer.cancel();
						mTimer = null;
						ButtonLightShadow.setText("¿ªÊ¼");
						mLightShadowIsStart = false;
					}
					else
					{
						if(OpenCamera())
						{
							ButtonLightShadow.setText("Í£Ö¹");
							mLightShadowIsStart = true;
							mIsLight = false;
							mTimer = new Timer();
							mTimer.schedule(new TimerTask() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									mIsLight = !mIsLight;
									LightCtrl(mIsLight);
								}
							}, 0,mFlashTime);
						}
						else
						{
							Toast.makeText(getApplicationContext(), "Open Camera Failed", Toast.LENGTH_LONG).show();
						}
					}
					break;
				default:break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
