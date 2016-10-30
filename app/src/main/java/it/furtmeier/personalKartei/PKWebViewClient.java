package it.furtmeier.personalKartei;

import android.net.http.SslError;
import android.os.Handler;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class PKWebViewClient extends WebViewClient {
	private PKRunnable next;
	
	public void cancelNext(){
		if(next == null)
			return;
		
		next.myCancel = true;
	}
	
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
    
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
    	handler.proceed();
    }
    
    
	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		//Log.e("WebView", description+" ("+errorCode+")");
		
		Handler reload = new Handler();
		next = new PKRunnable(view, failingUrl);
		
		reload.postDelayed(next, 60000);
	}
}

class PKRunnable implements Runnable {
	public boolean myCancel = false;
	private WebView myView;
	private String myUrl;
	
	public PKRunnable(WebView view, String url){
		myView = view;
		myUrl = url;
	}
	
	@Override
	public void run() {
		if(myCancel){
			//Log.i("WebView", "Aborting reload");
			return;
		}
		
		//Log.i("WebView", "reloading...");
		myView.loadUrl(myUrl);
	}
	
}