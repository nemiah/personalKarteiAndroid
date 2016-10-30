package it.furtmeier.personalKartei;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

public class PersonalKartei extends Activity {
	public static boolean running;
	public static PersonalKartei instance;
	public static PKWebView webView;
	File directory;
	final String TAG = "States";
	SharedPreferences erasePref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CheckRunningReceiver.enable(this);
		ForegroundService.start(this);
		running = true;
		instance = this;
		Options.load(this);
		setFullScreen();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		webView = new PKWebView(this);
		setContentView(webView);
		//setMenuButtonVisibility(true);
		setRequestedOrientation(Options.orientation);
		loadWebView();
		createDirectory();

		erasePref = getSharedPreferences("Photoeraser",MODE_PRIVATE);
		try {
			Log.d(TAG, "Photo lifetime: " + erasePref.getInt("Photoeraser",3) + "-days");
			deleteFilesOlderThanNdays(erasePref.getInt("Photoeraser",3),directory.getPath());  
		} finally {
			
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new it.furtmeier.personalKartei.Menu(this).show();
		return false;
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Resources resources = getResources();
		final String[] menus = resources.getStringArray(R.array.Options);
		it.furtmeier.personalKartei.Menu menu = new it.furtmeier.personalKartei.Menu(this);

		switch (item.getItemId()) {
			case R.id.refreshPage:
				loadWebView();
				return true;

			case R.id.setURL:
				menu.editUrl(resources, menus[it.furtmeier.personalKartei.Menu.EDIT_URL], Options.url);
				return true;

			case R.id.orientation:
				Log.i("personalKartei", "HI!");
				menu.editOrientation(resources, menus[it.furtmeier.personalKartei.Menu.EDIT_ORIENTATION], Options.orientation);
				return true;

			default:
				return true;
				//return super.onOptionsItemSelected(item);
		}
	}

	public void loadWebView() {
		webView.clearCache(true);
		webView.resetError();
		webView.loadUrl(Options.url);
	}

	public void setFullScreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/*public void setMenuButtonVisibility(boolean visible) {
		try {
			int flag = WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null);
			getWindow().setFlags(visible ? flag : 0, flag);
		} catch (Exception e) {
		}
	}*/

	public String getVersionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}

	public void setBrightness(float value) {
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.screenBrightness = value;
		window.setAttributes(lp);
	}


	private void createDirectory() {
		File path = Environment.getExternalStorageDirectory();
		if(android.os.Build.DEVICE.contains("Samsung") || android.os.Build.MANUFACTURER.contains("Samsung")){
			directory = new File(path.getAbsolutePath()+"/external_sd/","PersonalKartei");
		} else {
			directory = new File(path,"PersonalKartei");
		}
		if (!directory.exists()) {
			directory.mkdirs();
			Log.d(TAG, "Directory created");
		}
	}

	public void deleteFilesOlderThanNdays(int daysBack, String dirWay) {  
		Log.d(TAG, "Start .deleteFilesOlderThanNdays method");
		File directory = new File(dirWay);  
		if(directory.exists())  {     
			File[] listFiles = directory.listFiles();  
			long purgeTime = System.currentTimeMillis() - ((long)daysBack * 24 * 60 * 60 * 1000);  
			for(File listFile : listFiles)  {  
				if(listFile.lastModified() > purgeTime)  {  
					if (listFile.isFile())  {  
						System.out.println("This is a file: " + listFile);  
						System.out.println("listFile.lastModified()" + listFile.lastModified());
						double  purgeTimeDifference = (listFile.lastModified()-purgeTime)/60000;
						System.out.println("Storage time left: " + purgeTimeDifference + " minutes");
					} else {  
						System.out.println("This is a directory: " + listFile);  
						System.out.println("Last modified: " + listFile.lastModified() + " milliseconds");  
						deleteFilesOlderThanNdays(daysBack, listFile.getAbsolutePath());  
					}  
				} else {
					System.out.println("This is a file to delete: " + listFile);
					long purgeTimeDifference = (purgeTime-listFile.lastModified());
					System.out.println("File overtime: " + purgeTimeDifference + " minutes");
					listFile.delete();
				}
			}  
		}
		
		else Log.d(TAG, "Files were not deleted, directory " + dirWay + " doesn't exist!");

		Log.d(TAG, "End .deleteFilesOlderThanNdays method");
	}  
}

