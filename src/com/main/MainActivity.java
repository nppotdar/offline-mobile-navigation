package com.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.sensor.MovementService;
import com.ui.GraphPlotter;

public class MainActivity extends Activity {

	public static final String TAG = "GraphPlotter";
	public static TextView view;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		view = (TextView) findViewById(R.id.view);
	}

	public static void display(int i) {
		view.setText(i + "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.startserv:
			Log.d(TAG, "onClick: Starting service");
			startService(new Intent(this, MovementService.class));
			break;
		case R.id.stopserv:
			Log.d(TAG, "onClick: Stopping service");
			stopService(new Intent(this, MovementService.class));
			break;
		case R.id.gui:
			Log.d(TAG, "onClick: Switching to textui");
			Intent x = new Intent(getApplicationContext(), GraphPlotter.class);
			startActivityIfNeeded(x, Intent.FLAG_ACTIVITY_SINGLE_TOP);
			break;
		}
		return true;
	}

}