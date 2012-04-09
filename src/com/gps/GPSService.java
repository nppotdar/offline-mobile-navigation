/**
 * 
 */
package com.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Bhumil Haria
 * 
 */
public class GPSService extends Service {

	private static final String TAG = "GPSService";
	private int sensUpdateDelay = 1000 * 60 * 10;

	GPSDelayHandler gpsDelayHandler = new GPSDelayHandler();
	GPSDelayThread gpsDelayThread = new GPSDelayThread(gpsDelayHandler,
			sensUpdateDelay);

	LocationManager lm;
	LocationListener ll;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, TAG + " Created", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "onCreate");

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		ll = new MyLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

	}

	public void gpsDoSomething() {
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		ll = new MyLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		
		String s = TAG + ": \n";
		s = s ;
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, TAG + " Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, TAG + " Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
	}

	private class MyLocationListener implements LocationListener {

		public double latitude;
		public double longitude;

		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();

				Log.d("LOCATION CHANGED", latitude + "");
				Log.d("LOCATION CHANGED", longitude + "");

				Toast.makeText(GPSService.this, latitude + " : " + longitude,
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}
