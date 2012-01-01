/**
 * 
 */
package com.sensor;

import android.hardware.SensorManager;
import android.opengl.Matrix;

/**
 * @author Bhumil Haria
 * 
 */
public class MotionState {
	/**
	 * Class that represents, for a given instant of time:
	 * 
	 * All (relevant) sensor values (that is, w.r.t. device
	 * coordinates/reference frame) AND Other values derived from them (Earth
	 * reference frame)
	 * 
	 * Each value is an array of floats: Indices (0,1,2,3) stand for (x, y, z, 0) or
	 * (East, North, Up, 0) in order
	 */

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
	 * Constructor.
	 * Defines members, initialises them to zeroes.
	 */
	public MotionState() {
		devAcceleration = new float[4];
		devMagneticField = new float[4];
		devOrientation = new float[4];
		devGravity = new float[4];

		earthAcceleration = new float[4];
		earthVelocity = new float[4];
		earthDisplacement = new float[4];

		for (int i = 0; i < 4; i++){
			earthVelocity[i] = 0;
			earthAcceleration[i] = 0;
			earthDisplacement[i] = 0;
		}
	
		// Details about the various Sensor values and their meanings:
		// 	> devAccelerometer: the values of acceleration ALONG the 3 axes of the phone, which are
		//  	ax = axis along LENGTH of phone
		//  	ay = axis along BREADTH of phone
		//  	az = axis perpendicular to surface of phone
		// 
		//	> devOrientation: ANGLE values
		//  	pitch 	= angle in XY plane (about Z axis)
		//  	roll  	= angle in YZ plane (about X axis)
		//  	azimuth = angle in ZX plane (about Y axis)
		//
	}

	public void setDevAcceleration(float ax, float ay, float az) {
		// if (ax < 0.5)
		devAcceleration[0] = ax;
		// if (ay < 0.5)
		devAcceleration[1] = ay;
		// if (az < 0.5)
		devAcceleration[2] = az;
	}
	public void setDevMagneticField(float gx, float gy, float gz) {
		this.devMagneticField[0] = gx;
		this.devMagneticField[1] = gy;
		this.devMagneticField[2] = gz;
	}
	public void setDevOrientation(float azimuth, float pitch, float roll) {
		this.devOrientation[0] = azimuth;
		this.devOrientation[1] = pitch;
		this.devOrientation[2] = roll;
	}
	public void setDevGravity(float gx, float gy, float gz) {
		this.devGravity[0] = gx;
		this.devGravity[1] = gy;
		this.devGravity[2] = gz;
	}

	public void calculateWorldCoordinates() {
		 earthAcceleration[0] =(float) (devAcceleration[0]*(Math.cos(devOrientation[2])*Math.cos(devOrientation[0])+Math.sin(devOrientation[2])*Math.sin(devOrientation[1])*Math.sin(devOrientation[0])) + devAcceleration[1]*(Math.cos(devOrientation[1])*Math.sin(devOrientation[0])) + devAcceleration[2]*(-Math.sin(devOrientation[2])*Math.cos(devOrientation[0])+Math.cos(devOrientation[2])*Math.sin(devOrientation[1])*Math.sin(devOrientation[0])));
         earthAcceleration[1] = (float) (devAcceleration[0]*(-Math.cos(devOrientation[2])*Math.sin(devOrientation[0])+Math.sin(devOrientation[2])*Math.sin(devOrientation[1])*Math.cos(devOrientation[0])) + devAcceleration[1]*(Math.cos(devOrientation[1])*Math.cos(devOrientation[0])) + devAcceleration[2]*(Math.sin(devOrientation[2])*Math.sin(devOrientation[0])+ Math.cos(devOrientation[2])*Math.sin(devOrientation[1])*Math.cos(devOrientation[0])));
         earthAcceleration[2] = (float) (devAcceleration[0]*(Math.sin(devOrientation[2])*Math.cos(devOrientation[1])) + devAcceleration[1]*(-Math.sin(devOrientation[1])) + devAcceleration[2]*(Math.cos(devOrientation[2])*Math.cos(devOrientation[1])));
	}

	public void calculateVelocity(MotionState prevState, float time) {
		for (int i = 0; i < 3; i++) {
			this.earthVelocity[i] = prevState.earthVelocity[i]
					+ this.devAcceleration[i] * time;
		}
	}
	public float[] calculateDisplacement(MotionState prevState, float time) {
		for (int i = 0; i < 3; i++) {
			this.earthDisplacement[i] = prevState.earthVelocity[i]*time
					+ this.devAcceleration[i] * time*time/2;
		}
		return earthDisplacement;
	}
	
}
