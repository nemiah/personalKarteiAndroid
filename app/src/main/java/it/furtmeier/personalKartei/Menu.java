package it.furtmeier.personalKartei;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Menu {
	final String TAG = "States";
	private PersonalKartei personalKartei;
	SharedPreferences cameraPref;
	SharedPreferences cameraModePref;
	static Dialog eraseDialogue;
	static int cameraMode = 0;

	public final static int REFRESH_VIEW = 0;
	public final static int EDIT_DEVICE_MODE = 1;
	public final static int EDIT_URL  = 1;
	public final static int EDIT_ORIENTATION = 2;
	public final static int EDIT_BRIGHTNESS = 4;

	public Menu(PersonalKartei personalKartei) {
		this.personalKartei = personalKartei;
	}

	/*public void show() {
		Builder builder = new AlertDialog.Builder(personalKartei);
		final Resources resources = personalKartei.getResources();
		final String[] menus = resources.getStringArray(R.array.Options);
		builder.setItems(menus, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0)
					personalKartei.loadWebView();

				if (which == 1)
					editUrl(resources, menus[which], Options.url);

				if (which == 2)
					editOrientation(resources, menus[which],
							Options.orientation);

				//if (which == 3)
				//	editCameraDialogue();

				//if (which == 4)
				//	editOnlineCheck();
			}
		});
		builder.show();
	}*/

	/*private void editCamera(Resources resources, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(personalKartei);
		final String[] menus = resources.getStringArray(R.array.Options);
		builder.setTitle(title).setItems(R.array.Camera,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							// editCameraMode();
						}
						if (which == 1) {
							// editCameraDialogue();
						}
					}
				});
		builder.show();
	}

	private void editOnlineCheck() {
		
	}

	private void editCameraDialogue() {

		final Dialog eraseDialogue = new Dialog(personalKartei);

		eraseDialogue.setTitle(R.string.cameraSettings);
		eraseDialogue.setContentView(R.layout.number_dialogue);
		Button btnSet = (Button) eraseDialogue.findViewById(R.id.btnSet);
		Button btnCancel = (Button) eraseDialogue.findViewById(R.id.btnCancel);
		final RadioGroup cameraModeGroup = (RadioGroup) eraseDialogue
				.findViewById(R.id.cameraModeGroup);
		final RadioButton btnEnableCam = (RadioButton) eraseDialogue
				.findViewById(R.id.cameraEnable);
		final RadioButton btnDisableCam = (RadioButton) eraseDialogue
				.findViewById(R.id.cameraDisable);

		final NumberPicker np = (NumberPicker) eraseDialogue
				.findViewById(R.id.numberPicker);
		np.setMaxValue(120);
		np.setMinValue(1);
		np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		np.setWrapSelectorWheel(false);

		cameraPref = personalKartei.getSharedPreferences("Photoeraser",
				personalKartei.MODE_PRIVATE);
		cameraModePref = personalKartei.getSharedPreferences("Camera",
				personalKartei.MODE_PRIVATE);

		Log.d(TAG, "•Camera mode default value : " + cameraMode);
		cameraMode = cameraModePref.getInt("Camera", 0);
		cameraModeGroup.check(cameraModePref.getInt("Camera",
				btnDisableCam.getId()));
		if (cameraMode == 0)
			cameraModeGroup.check(btnDisableCam.getId());
		else
			cameraModeGroup.check(btnEnableCam.getId());

		np.setValue(cameraPref.getInt("Photoeraser", 3));
		np.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				// do something here
			}
		});
		btnSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cameraPref = personalKartei.getSharedPreferences("Photoeraser",
						personalKartei.MODE_PRIVATE);
				cameraModePref = personalKartei.getSharedPreferences("Camera",
						personalKartei.MODE_PRIVATE);
				SharedPreferences.Editor edErase = cameraPref.edit();
				SharedPreferences.Editor edMode = cameraModePref.edit();
				edErase.putInt("Photoeraser", np.getValue());
				edMode.putInt("Camera", cameraMode);
				edErase.commit();
				edMode.commit();
				eraseDialogue.dismiss();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				eraseDialogue.dismiss();
			}
		});
		cameraModeGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup,
							int btnChecked) {
						if (btnChecked == btnEnableCam.getId())
							cameraMode = 1;
						else
							cameraMode = 0;
						Log.d(TAG, "•Camera mode is set to : " + cameraMode);

					}
				});
		eraseDialogue.show();
	}

	private void editCameraMode() {
		AlertDialog.Builder builder = new AlertDialog.Builder(personalKartei);
		builder.setTitle(R.string.CameraMode);
		cameraModePref = personalKartei.getSharedPreferences("Camera",
				personalKartei.MODE_PRIVATE);
		final Resources resources = personalKartei.getResources();
		final String[] items = resources
				.getStringArray(R.array.CameraEnableDisable);
		builder.setSingleChoiceItems(items, cameraModePref.getInt("Camera", 0),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						cameraModePref = personalKartei.getSharedPreferences(
								"Camera", personalKartei.MODE_PRIVATE);
						SharedPreferences.Editor edMode = cameraModePref.edit();
						// cameraPref.getInt("Camera",0);

						switch (item) {
						case 0:
							edMode.putInt("Camera", 0);
							edMode.commit();
							break;
						case 1:
							edMode.putInt("Camera", 1);
							edMode.commit();
							break;
						}
					}
				});
		builder.show();
	}*/

	public void editOrientation(Resources resources, String title, int orientation) {
		AlertDialog.Builder builder = new AlertDialog.Builder(personalKartei);
		builder.setTitle(title)
				.setItems(R.array.Orientations, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						int newValue = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
						if(which == 0)
							newValue = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
						if(which == 1)
							newValue = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
						if(which == 2)
							newValue = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
						if(which == 3)
							newValue = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;

						Options.orientation = newValue;
						Options.save(personalKartei);
						personalKartei.setRequestedOrientation(newValue);
					}
				});
		builder.show();
	}

	public void editUrl(final Resources resources, final String title,
			final String url) {
		Builder builder = new AlertDialog.Builder(personalKartei);
		builder.setTitle(title);
		final EditText edit = new EditText(personalKartei);
		edit.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_URI);
		edit.setText(url);
		builder.setView(edit);
		builder.setPositiveButton(resources.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						final String newUri = edit.getText().toString();
						try {
							URI.create(newUri);
							if (!newUri.toLowerCase(Locale.US).startsWith(
									"http://")
									&& !newUri.toLowerCase(Locale.US)
											.startsWith("https://"))
								throw new MalformedURLException();
							Options.url = newUri;
							Options.save(personalKartei);
							personalKartei.loadWebView();
						} catch (Exception e) {
							showError(resources.getString(R.string.madUri),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											editUrl(resources, title, newUri);
										}
									});
						}
					}
				});
		builder.setNegativeButton(resources.getString(R.string.cancel), null);
		builder.show();
	}

	public void showError(String text, DialogInterface.OnClickListener onClick) {
		final Resources resources = personalKartei.getResources();
		Builder builder = new AlertDialog.Builder(personalKartei);
		builder.setTitle(resources.getString(R.string.error));
		builder.setMessage(text);
		builder.setPositiveButton(resources.getString(R.string.ok), onClick);
		builder.show();
	}
}
