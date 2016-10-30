package it.furtmeier.personalKartei;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;


public class CameraActivity extends Activity implements SurfaceHolder.Callback {

	final String TAG = "States";
	static File directory;
	SharedPreferences passPrefL;
	Camera camera = null;
	CameraInfo camInfo = new CameraInfo();
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	RelativeLayout Slayout;
	IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	final Handler tasktobackHandler = new Handler();
	LayoutInflater controlInsflater = null;
	Locale local = new Locale("ru","RU");
	Date currentDate = new Date();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camerasurface);

		//Initialise buttons and text fields
		Log.d(TAG, "CameraActivity: OnCreate");
		Slayout = (RelativeLayout) findViewById (R.id.Slayout);
		AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.01f);
		alpha.setDuration(10); 
		alpha.setFillAfter(true); 
		Slayout.startAnimation(alpha);
		Log.d(TAG, "In Method screen_on:  CameraActivity is set invisible");
		//Set screen orientation
		setRequestedOrientation(Options.orientation);
		getWindow().setFormat(PixelFormat.UNKNOWN);
		//Create preview for camera
		surfaceView = (SurfaceView)findViewById(R.id.camerapreviewed);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		Log.d(TAG, "CameraActivity: surfaceView&Holder created");

		createDirectory();

		try {
			tasktobackHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (camera != null) {
						camera.takePicture(null,myPictureCallback_RAW, myPictureCallback_JPG);
						Log.d(TAG, "Taking Picture");
					} else {
						Log.d(TAG, "No front camera. Finish the activity");
						finish();
					}
				}
			}, 200);

		} catch (RuntimeException e) {
			Log.d(TAG, "In method onCreate: Something went wrong");
		} finally {}
	}

	ShutterCallback myShutterCallback = new ShutterCallback(){

		@Override
		public void onShutter() {

		}};

		PictureCallback myPictureCallback_RAW = new PictureCallback(){

			@Override
			public void onPictureTaken(byte[] arg0, Camera arg1) {
				// TODO Auto-generated method stub

			}};

			PictureCallback myPictureCallback_JPG = new PictureCallback(){

				@Override
				public void onPictureTaken(byte[] paramArrayOfByte, Camera paramCamera)
				{
					Log.d(TAG, "onPictureTaken starts"); 
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
					currentDate = new Date();
					String timeIdentifier = sdf.format(currentDate).toString().replace(".", "").replace(" ", "_").replace(":", "")+"_"+String.format(""+System.currentTimeMillis());
					try
					{
						Log.d(TAG, "Foto might be saved here: " + String.format(directory.getPath() + "/"+"photo_"+ timeIdentifier + ".jpg"));
						FileOutputStream outStream = new FileOutputStream(String.format(directory.getPath() + "/"+"photo_"+ timeIdentifier + ".jpg"));
						Log.d(TAG, "Saving a photo");
						outStream.write(paramArrayOfByte);
						outStream.close();
					}
					catch (Exception e)
					{
						Log.d(TAG, "Photo is not saved");	
					}
					paramCamera.startPreview();
	
					//Move task to back and reveal activity
					try {
						tasktobackHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								Log.d(TAG, "In Method onPictureTaken:  finish Activity");
								finish();
							}	
						}, 200);
					} catch (RuntimeException e) {
						e.printStackTrace();
						Log.d(TAG, "In method onPictureTaken: can't reveal activity");	
					}
				}
			};


			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				Log.d(TAG, "Surface changed");
				if(previewing){
					Log.d(TAG, "Status previewing, stop preview");
					camera.stopPreview();
					previewing = false;
				}
				if (camera != null){
					try {
						Log.d(TAG, "Camera not a NULL, start preview");
						camera.setPreviewDisplay(surfaceHolder);
						camera.startPreview();
						previewing = true;

					} catch (IOException e) {
						Log.d(TAG, "Camera IS a NULL");
						e.printStackTrace();
					}
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				Log.d(TAG, "Surface created");
				int camNo = 1;
				//Camera.getCameraInfo(camNo, camInfo);
				try {
					try {
					     camera = Camera.open(camNo);
					     Log.d(TAG, "Camera.open(index 1) is successful");
					     Camera.getCameraInfo(camNo, camInfo);
				         Log.d(TAG, "Camera.getCameraInfo(index 1) is successful");
					    } catch(RuntimeException e) {
					     Log.d(TAG, "There is no camera with index = 1");
					     camNo = 0;
					     Log.d(TAG, "Setting camera index to 0");
					     camera = Camera.open(camNo);
					     Log.d(TAG, "Camera.open(index 0) is successful");
					     Camera.getCameraInfo(camNo, camInfo);
				         Log.d(TAG, "Camera.getCameraInfo(index 0) is successful");
					    } finally { 
					    }
			
					if (Camera.CameraInfo.CAMERA_FACING_FRONT!=camInfo.facing){
						 Log.d(TAG, "Camera facing isn't front");
						 camNo = 0;
						 Log.d(TAG, "Setting camera index to 0");
						 camera = Camera.open(camNo);
						 Log.d(TAG, "Camera.open(index 0) is successful");
						 Camera.getCameraInfo(camNo, camInfo);
						 Log.d(TAG, "Camera.getCameraInfo(index 0) is successful");
						} 
				
					//Set camera orientation
					int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
					int degrees = 0;
					switch (rotation) {
						case Surface.ROTATION_0: degrees = 0; break;
						case Surface.ROTATION_90: degrees = 90; break;
						case Surface.ROTATION_180: degrees = 180; break;
						case Surface.ROTATION_270: degrees = 270; break;
					}
					Camera.Parameters parameters = camera.getParameters();
					int rotate = (degrees + 270) % 360;
					parameters.setRotation(rotate);
					camera.setParameters(parameters);
					Log.d(TAG, "degrees=" + degrees + ". Camera orientation is set");
					Log.d(TAG, "Camera opens in SurfaceCreate");

				} catch (RuntimeException e) {
					Log.d(TAG, "Camera can't be open in SurfaceCreate");
				} finally {
				}
				Log.d(TAG, "Camera's current facing index is " + camInfo.facing);
				Log.d(TAG, "Camera's facing index front is " + Camera.CameraInfo.CAMERA_FACING_FRONT);
				Log.d(TAG, "Camera's facing index back is " + Camera.CameraInfo.CAMERA_FACING_BACK);
				if (camera == null) {
					Log.d(TAG, "No front camera");
					finish();
				}
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				Log.d(TAG, "Surface begins destroying");
				if (camera!=null){
					camera.stopPreview();
					camera.release();
					camera = null;
					previewing = false;
				}

				Log.d(TAG, "Surface destroyed");
			}

			private void createDirectory() {
				File path = Environment.getExternalStorageDirectory();
				if(android.os.Build.DEVICE.contains("Samsung") || android.os.Build.MANUFACTURER.contains("Samsung")){
					directory = new File(path.getAbsolutePath()+"/external_sd/","PersonalKartei");
				} else {
					directory = new File(path,"PersonalKartei");
				}
				if (!directory.exists())
				{
					directory.mkdirs();
					Log.d(TAG, "Directory created");
				}
			}


			@Override
			protected void onRestart() {
				super.onRestart();
				Log.d(TAG, "CameraActivity: onRestart()");
				surfaceView.setVisibility(View.VISIBLE);

			}

			@Override
			protected void onStart() {
				super.onStart();
				Log.d(TAG, "CameraActivity: onStart()");
			}

			@Override
			protected void onResume() {
				super.onResume();

				Log.d(TAG, "CameraActivity: onResume()");
			}

			@Override
			protected void onPause() {
				super.onPause();
				Log.d(TAG, "CameraActivity: onPause()");
			}

			@Override
			protected void onStop() {
				super.onStop();
				Log.d(TAG, "CameraActivity: onStop()");

			}

			@Override
			protected void onDestroy() {
				super.onDestroy();
				Log.d(TAG, "CameraActivity: onDestroy()");

			}
}