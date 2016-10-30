package it.furtmeier.personalKartei;

import it.furtmeier.personalKartei.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JavaScriptHook {
	private PersonalKartei personalKartei;
	SharedPreferences cameraPref;
	private MediaPlayer voice = new MediaPlayer();
	private MediaPlayer beep;
	
	public JavaScriptHook(PersonalKartei personalKartei) {
		this.personalKartei = personalKartei;
	}
	
	@JavascriptInterface
	public void louder(int vol) {
		AudioManager manager = (AudioManager) personalKartei.getSystemService(Context.AUDIO_SERVICE);

		if(vol > manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC))
			vol = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		Toast.makeText(personalKartei.getApplicationContext(), "Volume "+(vol / manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 100)+"%", Toast.LENGTH_SHORT).show();
		manager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
	}

	@JavascriptInterface
	public void beep(){
		if(beep == null)
			beep = MediaPlayer.create(personalKartei, R.raw.beep);

		try {
			//beep.stop();
			//beep.prepare();
			beep.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	@JavascriptInterface
	public void sing(String url, final int volumeStart, final int volumeEnd){
		Toast.makeText(personalKartei.getApplicationContext(), R.string.startPlayback, Toast.LENGTH_SHORT).show();

		//Log.d("Fading", "Volume end: "+(volumeEnd));

		try {
			voice.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					class pkFadeIn extends AsyncTask<MediaPlayer, Integer, Long> {

						@Override
						protected Long doInBackground(MediaPlayer... params) {
							float currentVolume = (float) volumeStart;
							while(currentVolume <= volumeEnd / 100){
								//Log.d("Fading", ""+currentVolume);
								currentVolume += 0.02;
								params[0].setVolume((float) currentVolume, (float)currentVolume);
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							return null;
						}

					}

					pkFadeIn fade = new pkFadeIn();
					fade.execute(mp);
				}
			});

			voice.setDataSource(url);
			voice.prepare();
			voice.setVolume((float) volumeStart / 100, (float) volumeStart / 100);
			voice.start();
		} catch (Exception e) {
			// e.printStackTrace();
		} 
	}
	
	@JavascriptInterface
	public boolean singing(){
		return voice.isPlaying();
	}
	
	@JavascriptInterface
	public void beQuiet(){
		voice.stop();
		voice.release();
		voice = new MediaPlayer();
	}
	
	@JavascriptInterface
	public void takePhoto(){
		cameraPref = personalKartei.getSharedPreferences("Camera",personalKartei.MODE_PRIVATE);
		if (cameraPref.getInt("Camera", 0)==1) {
			Intent intent = new Intent(personalKartei.getApplicationContext(), CameraActivity.class);
			personalKartei.startActivity(intent);
		}
	}
}
