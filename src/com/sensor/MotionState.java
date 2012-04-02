package com.sensor;

import android.hardware.SensorManager;
import android.opengl.Matrix;

/**
 * @author Bhumil Haria
 * 
 */
public class MotionState {
	/*
	 * Class that represents, for a given instant of time:
	 * 
	 * All (relevant) sensor values (that is, w.r.t. device
	 * coordinates/reference frame) AND Other values derived from them (Earth
	 * reference frame)
	 * 
	 * Each value is an array of floats: Indices (0,1,2,3) stand for (x, y, z,
	 * 0) or (East, North, Up, 0) in order
	 */

	public static class SensorSemaphore {

		public static final short AccSensor = 0;
		public static final short OrientSensor = 1;
		public static final short MagneticSensor = 2;
		public static final short GravitySensor = 3;

		static boolean isLocked[] = new boolean[4];

		public static boolean lock(short sensor) {
			try {
				isLocked[sensor] = true;
				return true;
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}

		public static boolean unlock(short sensor) {
			try {
				isLocked[sensor] = false;
				return true;
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}

		public static void unlockAll() {
			for (short i = 0; i < 4; i++) {
				isLocked[i] = false;
			}
		}

	}

	public static int SensorReadPrecision = 1;
	public static int OutputPrecision = 1;
	
	// Values w.r.t. device coordinate
	public float devAcceleration[];
	public float devMagneticField[];
	public float devOrientation[];
	public float devGravity[];

	// Values w.r.t. Earth coordinate
	public float earthAcceleration[];
	public float earthVelocity[];
	public float earthDisplacement[];

	/**
	 * Constructor. Defines members, and initialises them to zeroes.
	 */
	public MotionState() {
		devAcceleration = new float[4];
		devMagneticField = new float[4];
		devOrientation = new float[4];
		devGravity = new float[4];

		earthAcceleration = new float[4];
		earthVelocity = new float[4];
		earthDisplacement = new float[4];

		// Reset or use: this.reset()
		for (int i = 0; i < 4; i++) {
			earthVelocity[i] = 0;
			earthAcceleration[i] = 0;
			earthDisplacement[i] = 0;
		}

		// Init SensorSemaphores
		for (short i = 0; i < 4; i++) {
			SensorSemaphore.lock(i);
		}

		// Details about the various Sensor values and their meanings:
		// > devAccelerometer: the values of acceleration ALONG the 3 axes of
		// the phone, which are
		// ax = axis along LENGTH of phone
		// ay = axis along BREADTH of phone
		// az = axis perpendicular to surface of phone
		//
		// > devOrientation: ANGLE values
		// pitch = angle in XY plane (about Z axis)
		// roll = angle in YZ plane (about X axis)
		// azimuth = angle in ZX plane (about Y axis)
		//
	}

	// ---------------- Setters. Getters unnecessary ----------------- //
	public void setDevAcceleration(float[] eValues) {

		if (!SensorSemaphore.isLocked[SensorSemaphore.AccSensor]) {

			for (int i = 0; i < 3; i++) {
				if (Math.abs(devAcceleration[i] - eValues[i]) > SensorThresholds.Acceleration[i])
					devAcceleration[i] = Round(eValues[i], MotionState.SensorReadPrecision);
			}

			SensorSemaphore.lock(SensorSemaphore.AccSensor);
		}
	}

	public void setDevMagneticField(float[] eValues) {
		if (!SensorSemaphore.isLocked[SensorSemaphore.MagneticSensor]) {

			for (int i = 0; i < 3; i++) {
				if (Math.abs(devMagneticField[i] - eValues[i]) > SensorThresholds.MagneticField[i])
					devMagneticField[i] = Round(eValues[i], MotionState.SensorReadPrecision);
			}

			SensorSemaphore.lock(SensorSemaphore.MagneticSensor);
		}
	}

	public void setDevOrientation(float[] eValues) {
		if (!SensorSemaphore.isLocked[SensorSemaphore.OrientSensor]) {

			for (int i = 0; i < 3; i++) {
				if (Math.abs(devOrientation[i] - eValues[i]) > SensorThresholds.Orientation[i])
					devOrientation[i] = Round(eValues[i], MotionState.SensorReadPrecision);
			}

			SensorSemaphore.lock(SensorSemaphore.OrientSensor);
		}
	}

	public void setDevGravity(float[] eValues) {
		if (!SensorSemaphore.isLocked[SensorSemaphore.GravitySensor]) {

			for (int i = 0; i < 3; i++) {
				if (Math.abs(devGravity[i] - eValues[i]) > SensorThresholds.Gravity[i])
					devGravity[i] = Round(eValues[i], MotionState.SensorReadPrecision);
			}

			SensorSemaphore.lock(SensorSemaphore.GravitySensor);
		}
	}

	/**
	 * Reset function. Resets all values to default (zero)
	 */
	public void reset() {
		for (int i = 0; i < 4; i++) {
			this.devAcceleration[i] = 0;
			this.devMagneticField[i] = 0;
			this.devOrientation[i] = 0;
			this.devGravity[i] = 0;
			this.earthVelocity[i] = 0;
			this.earthAcceleration[i] = 0;
			this.earthDisplacement[i] = 0;
		}
		// TODO: check if values of above arrays at index 3 are always zero.
		// If yes, then reduce loop to i<3, for efficiency
	}

	/**
	 * Copies all values of "current" object into calling object; resets
	 * "current" object variables
	 * 
	 * @param previous
	 *            MotionState object representing previous state
	 * @param current
	 *            MotionState object representing current state
	 */
	public void updatePrevious(MotionState current) {
		for (int i = 0; i < 4; i++) {
			// previous := current
			this.devAcceleration[i] = current.devAcceleration[i];
			this.devMagneticField[i] = current.devMagneticField[i];
			this.devOrientation[i] = current.devOrientation[i];
			this.devGravity[i] = current.devGravity[i];
			this.earthVelocity[i] = current.earthVelocity[i];
			this.earthAcceleration[i] = current.earthAcceleration[i];
			this.earthDisplacement[i] = current.earthDisplacement[i];
		}
		// NO current.reset(); because we need the previous values anyways while
		// initializing again
		// since sensor values are updated only above a certain threshold
	}

	// ---------------- Calculate functions ----------------- //
	/**
	 * Converts acceleration values from device coordinates to real-world
	 * coordinates, initializes the earthAcceleration[] array
	 */
	public void calculateWorldCoordinates() {
		
		if (true){
			float[] R = new float[16];
			float[] I = new float[16];		
			
			SensorManager.getRotationMatrix (R, I, devGravity, devMagneticField);		
			Matrix.multiplyMV(earthAcceleration, 0, R, 0, devAcceleration, 0);
		}
		else {
			earthAcceleration[0] = (float) (devAcceleration[0]
					* (Math.cos(devOrientation[2])
							* Math.cos(devOrientation[0]) + Math
							.sin(devOrientation[2])
							* Math.sin(devOrientation[1])
							* Math.sin(devOrientation[0]))
					+ devAcceleration[1]
					* (Math.cos(devOrientation[1]) * Math
							.sin(devOrientation[0])) + devAcceleration[2]
					* (-Math.sin(devOrientation[2])
							* Math.cos(devOrientation[0]) + Math
							.cos(devOrientation[2])
							* Math.sin(devOrientation[1])
							* Math.sin(devOrientation[0])));
			earthAcceleration[1] = (float) (devAcceleration[0]
					* (-Math.cos(devOrientation[2])
							* Math.sin(devOrientation[0]) + Math
							.sin(devOrientation[2])
							* Math.sin(devOrientation[1])
							* Math.cos(devOrientation[0]))
					+ devAcceleration[1]
					* (Math.cos(devOrientation[1]) * Math
							.cos(devOrientation[0])) + devAcceleration[2]
					* (Math.sin(devOrientation[2])
							* Math.sin(devOrientation[0]) + Math
							.cos(devOrientation[2])
							* Math.sin(devOrientation[1])
							* Math.cos(devOrientation[0])));
			earthAcceleration[2] = (float) (devAcceleration[0]
					* (Math.sin(devOrientation[2]) * Math
							.cos(devOrientation[1])) + devAcceleration[1]
					* (-Math.sin(devOrientation[1])) + devAcceleration[2]
					* (Math.cos(devOrientation[2]) * Math
							.cos(devOrientation[1])));
		}
		
		for(int i=0; i<3; i++)
			earthAcceleration[i] = Round(earthAcceleration[i], MotionState.OutputPrecision); //Rounding off
	}

	/**
	 * Calculates velocity in real-world coordinates from earthAccereation[]
	 * 
	 * @param prevState
	 *            Object representing the last state at which calculation
	 *            occurred
	 * @param time
	 *            Time since the last calculation occurred (in milliseconds)
	 */
	public void calculateVelocity(MotionState prevState, int dTime) {
		float time = dTime * (float) 0.001;
		for (int i = 0; i < 3; i++) {
			this.earthVelocity[i] = prevState.earthVelocity[i] + this.earthAcceleration[i] * time;
			//this.earthVelocity[i] = this.earthAcceleration[i] * time;
			
			this.earthVelocity[i] = Round(this.earthVelocity[i], 2); //Rounding off
		}
	}

	/**
	 * Calculates displacement in real-world coordinates
	 * 
	 * @param prevState
	 *            Object representing the last state at which calculation
	 *            occurred
	 * @param time
	 *            Time since the last calculation occurred (in milliseconds)
	 * @return Returns the displacement in earth coordinates in an array
	 */
	public float[] calculateDisplacement(MotionState prevState, int dTime) {
		float time = dTime * (float) 0.001;
		for (int i = 0; i < 3; i++) {
/*			this.earthDisplacement[i] = prevState.earthVelocity[i] * time
					+ this.earthAcceleration[i] * time * time / 2;*/
			this.earthDisplacement[i] = (prevState.earthVelocity[i] + this.earthVelocity[i]) * time/ (float)2 ;
			
			this.earthDisplacement[i] = Round (this.earthDisplacement[i], 2);
		}
		return earthDisplacement;
	}
	
	public static float Round(float d, int precision){
		if(precision == 0)
			return d;
		else if(precision < 1 || precision > 5)
			precision = 1;
		float p = (float) Math.pow(10, precision);
		
		return (float) Math.round(d*p)/p;
	}

}
