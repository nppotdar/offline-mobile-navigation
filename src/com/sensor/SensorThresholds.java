/**
 * 
 */
package com.sensor;

/**
 * @author Bhumil Haria
 *
 */
public final class SensorThresholds {
	// Note: If you wish to disable thresholds, just set values to 0
	public static float Acceleration[] = new float[4];
	public static float MagneticField[] = new float[4];
	public static float Orientation[] = new float[4];
	public static float Gravity[] = new float[4];
	
	public static void initThresholds()
	{
		for (int i = 0; i < 3; i++) {
			Acceleration[i] = (float) 0.2 ;
			MagneticField[i] = (float) 0.40001;
			Orientation[i] = (float) 0.5;
			Gravity[i] = (float) 0.05 ;
		}		
		MagneticField[2] = (float) 0.8;
		
		// TODO: Code to read file having calibrated Threshold levels
	}
	
	public static void calibrateThresholds(){

		// TODO: Code to calculate thresholds using stats
		// NOT to be run every time
		// Only once, while calibration

	}
}
