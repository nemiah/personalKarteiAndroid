package it.furtmeier.personalKartei;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

// The purpose of this service is to keep the application running. 
public class ForegroundService extends Service {
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startForeground(42, new Notification());
		Log.v(PersonalKartei.class.getSimpleName(), "ForegroundService Startetd");
		return (START_NOT_STICKY);
	}

	@Override
	public void onDestroy() {
		Log.v(PersonalKartei.class.getSimpleName(), "ForegroundService Stopped");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static void start(Context context) {
		Intent i = new Intent(context, ForegroundService.class);
		context.startService(i);
	}
}
