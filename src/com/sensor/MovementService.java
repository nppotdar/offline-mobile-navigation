/**
 * 
 */
package com.sensor;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Bhumil Haria
 * 
 */
public class MovementService extends Service implements SensorEventListener {

	private static final String TAG = "MovementService";

	private SensorManager sensorManager;
	private MotionState prevState;
	private MotionState currentState;

	private int calcUpdateDelay = 250;
	private int sensUpdateDelay = 250;

	private SensorDelayHandler calcDelayHandler = new SensorDelayHandler(this,
			SensorDelayHandler.DelayType.CALCULATE);
	private SensorDelayThread calcDelayThread = new SensorDelayThread(
			calcDelayHandler, calcUpdateDelay);

	private SensorDelayHandler sensDelayHandler = new SensorDelayHandler(this,
			SensorDelayHandler.DelayType.SENSOR_UPDATE);
	private SensorDelayThread sensDelayThread = new SensorDelayThread(
			sensDelayHandler, sensUpdateDelay);

	// For communication only
	private float cumulativeDisplacement[];
	public static MotionState outState = new MotionState();
	public static float[] totDisp = new float[3];
	private static File outfile;

	/* Overriden or implemented */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, TAG + " Created", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, TAG + " Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");

		registerSensorListeners();
		SensorThresholds.initThresholds();
		
		cumulativeDisplacement = new float[3];
		for (int i = 0; i < 3; i++)
			cumulativeDisplacement[i] = 0;

		prevState = new MotionState();
		currentState = new MotionState();



		initFileHandling();

		sensDelayThread.start();
		calcDelayThread.start();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, TAG + " Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");

		// TODO: call function deregisterSensorListeners; close threads;
		unregisterSensorListeners();
		sensDelayThread.stopThread();
		calcDelayThread.stopThread();

		MovementService.outState.reset();
	}

	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			switch (event.sensor.getType()) {

			case Sensor.TYPE_ACCELEROMETER:
				currentState.setNetDevAcceleration(event.values);
				break;

			case Sensor.TYPE_LINEAR_ACCELERATION:
				currentState.setDevLinearAcceleration(event.values);
				break;

			case Sensor.TYPE_ORIENTATION:
				currentState.setDevOrientation(event.values);
				/*
				 * azimuth = event.values[0]; pitch = event.values[1]; roll =
				 * event.values[2];
				 */
				break;

			case Sensor.TYPE_GRAVITY:
				currentState.setDevGravity(event.values);
				break;

			case Sensor.TYPE_MAGNETIC_FIELD:
				currentState.setDevMagneticField(event.values);
				break;
			}

		}
	}

	/* Asynchronous calls by threads */
	public void executeCalculation() {
		// display();

		// Method 2: Independent calc of dist and dirn
		// currentState.calculateDistance();
		// currentState.findMotionDirection();

		// Methood 1: Not being use
		currentState.calculateWorldCoordinates();
		currentState.calculateVelocity(prevState, calcUpdateDelay);
		currentState.calculateDisplacement(prevState, calcUpdateDelay);

		for (int i = 0; i < 3; i++)
			cumulativeDisplacement[i] += currentState.earthDisplacement[i];

		prevState.updatePrevious(currentState);
		saveToFile();
		
		MovementService.outState = currentState;
		MovementService.totDisp = cumulativeDisplacement;

	}

	public void enableSensorUpdate() {
		MotionState.SensorSemaphore.unlockAll();
	}

	/* Private methods */

	private void registerSensorListeners() {
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager
				.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
						SensorManager.SENSOR_DELAY_GAME);

		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);

		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
				SensorManager.SENSOR_DELAY_GAME);

		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);
	}

	private void unregisterSensorListeners() {
		// TODO: Code for unregister
		sensorManager.unregisterListener(this);
	}

	private boolean initFileHandling() {
		// TODO: Only initialise file variables - Nagesh
		return true;
	}
	
	private boolean saveToFile(){
		// TODO: Code to save to file here - Nagesh
		// what to save: currentState.earthDisplacement
		return true;
	}
}
