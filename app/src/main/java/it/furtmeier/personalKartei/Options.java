package it.furtmeier.personalKartei;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;

public class Options {
	public static String url = "https://cloud.furtmeier.it/ubiquitous/CustomerPage/?CC=TimeTerminal&cloud=Furtmeier&terminalID=1";
	public static int orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	
	public static void load(PersonalKartei personalKartei) {
		SharedPreferences pref = personalKartei.getPreferences(Activity.MODE_PRIVATE);
		url = pref.getString("url", url);
		orientation = pref.getInt("orientation", orientation);
	}

	public static void save(PersonalKartei personalKartei) {
		SharedPreferences pref = personalKartei.getPreferences(Activity.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString("url", url);
		editor.putInt("orientation", orientation);
		editor.commit();
	}
}
