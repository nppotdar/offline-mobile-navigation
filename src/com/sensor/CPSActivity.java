package com.sensor;


import com.cps.R;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.TextView;

public class CPSActivity extends Activity implements SensorEventListener {

	private SensorManager sensorManager;
	
	private MotionState prevState;
	private MotionState currentState;
	private float deltaT = 1;
	
	private float d = 0;
	private float cumulativeDisp[];

	private TextView orientationView;
	private TextView gravityView;
	private TextView devLinAccView;
	private TextView nullView;
	private TextView earthAccView;
	private TextView devTotAccView;
	private EditText distance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initDisplay();
		initSensorListeners();
		
		cumulativeDisp = new float[3];
		for(int i = 0; i<2; i++)
			cumulativeDisp[i] = 0;
		
		prevState = new MotionState();
		currentState = new MotionState();

		MyHandler myHandler = new MyHandler(this);

		MyThread mThread = new MyThread(myHandler);
		mThread.start();
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	public void execute() {

		display();
		
		currentState.calculateWorldCoordinates();
		currentState.calculateVelocity(prevState, deltaT); // ? unnecessary
		currentState.calculateDisplacement(prevState, deltaT);
		
		for(int i=0; i<3; i++)
			cumulativeDisp[i] += currentState.earthDisplacement[i];
	}

	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_LINEAR_ACCELERATION:
				currentState.setDevAcceleration(event.values[0],
						event.values[1], event.values[2]);
				break;

			case Sensor.TYPE_ORIENTATION:
				currentState.setDevOrientation(event.values[0],
						event.values[1], event.values[2]);
				/*
				 * azimuth = event.values[0]; pitch = event.values[1]; roll =
				 * event.values[2];
				 */
				break;

			case Sensor.TYPE_GRAVITY:
				currentState.setDevGravity(event.values[0], event.values[1],
						event.values[2]);
				break;
				
			case Sensor.TYPE_MAGNETIC_FIELD:
				currentState.setDevMagneticField(event.values[0], event.values[1],
						event.values[2]);
				break;
			}

		}
	}

	private void initSensorListeners() {
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager
				.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
						SensorManager.SENSOR_DELAY_GAME);
		sensorManager
		.registerListener(this, sensorManager
				.getDefaultSensor(Sensor.TYPE_GRAVITY),
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager
		.registerListener(this, sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);
	}
	
	private void initDisplay()
	{
		orientationView = (TextView) findViewById(R.id.pitch);
		devLinAccView = (TextView) findViewById(R.id.azimuth);
		gravityView = (TextView) findViewById(R.id.roll);
		nullView = (TextView) findViewById(R.id.ax);
		earthAccView = (TextView) findViewById(R.id.ay);
		
		devTotAccView = (TextView) findViewById(R.id.az);
		distance = (EditText) findViewById(R.id.dist);
	}	
	public void display()
	{
		orientationView.setText("Orientation: \n" + currentState.devOrientation[0] + "  ||  "
				+ currentState.devOrientation[1] + "  ||  "
				+ currentState.devOrientation[2] + "\n");
		
		gravityView.setText("Gravity:\n" + currentState.devGravity[0] + "  ||  "
				+ currentState.devGravity[1] + "  ||  "
				+ currentState.devGravity[2] + "\n");

		devLinAccView.setText("Device Linear acceleration:\n" + currentState.devAcceleration[0] + "  ||  "
				+ currentState.devAcceleration[1] + "  ||  "
				+ currentState.devAcceleration[2] + "\n");

		nullView.setText(".........");
		
		earthAccView.setText("Earth Acceleration:\n" + currentState.earthAcceleration[0] + "  ||  "
				+ currentState.earthAcceleration[1] + "  ||  "
				+ currentState.earthAcceleration[2] + "\n");
		
		devTotAccView.setText("Earth Displacement:\n" + currentState.earthDisplacement[0] + "  ||  "
				+ currentState.earthDisplacement[1] + "  ||  "
				+ currentState.earthDisplacement[2] + "\n");
		
		distance.setText("Total Displacement:\n" + cumulativeDisp[0] + "  ||  "
				+ cumulativeDisp[1] + "  ||  "
				+ cumulativeDisp[2] + "\n");
		
		// String s = new String();
		// s = "";		
		// for(int i=0; i<4 ; i++)
		// {
		//	 for(int j=0; j<4; j++)
		//	 	 s = s + "  |  "  + currentState.R[4*i+j];
		// 	 s = s + "\n";
		// }		
		// devTotAccView.setText("Matrix R:\n" + s);
		
		// devTotAccView.setText("Total Acceleration:\n" + tx + "  ||  "
		//		+ ty + "  ||  "
		//		+ tz + "\n");	
	}
}
