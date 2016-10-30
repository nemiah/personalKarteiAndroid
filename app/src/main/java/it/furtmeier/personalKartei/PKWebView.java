package it.furtmeier.personalKartei;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint({ "SetJavaScriptEnabled", "ViewConstructor" })
public class PKWebView extends WebView {
	private PKWebViewClient client;
	
	
	public PKWebView(PersonalKartei personalKartei) {
		super(personalKartei);
		client = new PKWebViewClient();
		
		setKeepScreenOn(true);
		setWebViewClient(client);
		WebSettings webSettings = getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setDatabasePath("/data/data/"+getContext().getPackageName()+"/databases/"); //will have no effect as of API 19
		
		webSettings.setUserAgentString(webSettings.getUserAgentString() + " personalKartei Android/" + personalKartei.getVersionName());

		addJavascriptInterface(new JavaScriptHook(personalKartei), "alex");
		
		if(android.os.Build.VERSION.SDK_INT <= 16){
			final PKWebView view = this;
			final PersonalKartei activity = personalKartei;
			
		   /*setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
				public void onSystemUiVisibilityChange(int visibility){
					if(visibility == 0)
						activity.setMenuButtonVisibility(true);
					
					if (visibility != 0)
						return;
				
					Runnable rehideRunnable = new Runnable() {
						public void run() {
							activity.setMenuButtonVisibility(false);
							view.setSystemUiVisibility(SYSTEM_UI_FLAG_LOW_PROFILE);
						}
					};
					Handler rehideHandler = new Handler();
					rehideHandler.postDelayed(rehideRunnable, 2000);
				}
			});*/
		    
			setSystemUiVisibility(SYSTEM_UI_FLAG_LOW_PROFILE);
		}
	}
	
	public void resetError(){
		client.cancelNext();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	
	/*public void changeUrl(String url){
		client.cancelNext = true;
		loadUrl(url);
	}*/
	
	/*@Override
	public void loadUrl(String url) {
		// TODO Auto-generated method stub
		super.loadUrl(url);
	}*/
}