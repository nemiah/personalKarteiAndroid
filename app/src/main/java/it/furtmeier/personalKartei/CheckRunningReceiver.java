package it.furtmeier.personalKartei;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class CheckRunningReceiver extends BroadcastReceiver  {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (!PersonalKartei.running) {
			Intent i = new Intent(context, PersonalKartei.class);  
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}
	
	public static void enable(Context context) {
		AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	    PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(context, CheckRunningReceiver.class), 0);
	    mgr.cancel(pi);
	    mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10000, 10000, pi);
	}
}
