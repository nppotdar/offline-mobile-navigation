/**
 * 
 */
package com.sensor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;

/**
 * @author Bhumil Haria
 *
 */
public final class SensorThresholds {
	
	// Note: If you wish to disable thresholds, just set values to 0
	public static float[] NetAcceleration = new float[4];
	public static float Acceleration[] = new float[4];
	public static float MagneticField[] = new float[4];
	public static float Orientation[] = new float[4];
	public static float Gravity[] = new float[4];
	public static FileInputStream fis;
	public static FileOutputStream fos;
	public static InputStreamReader isr;
	public static OutputStreamWriter osr;
	public static Context context;
	private static String data;
	public static void setContext(Context c){
		context = c;
	}
	
	public static void initThresholds()
	{
		try{
		 fis = context.openFileInput("");
		 isr = new InputStreamReader(fis);		 

		 char[] inputBuffer = new char[fis.available()];
		 isr.read(inputBuffer);
		 data = new String(inputBuffer);
		 isr.close();
		 fis.close();
		}catch(Exception e){
			Log.d("lol sensorthreshold", "File read error");
		}
		
		//TODO: Bhumlya: Read the values from data & intitialize here
		for (int i = 0; i < 3; i++) {
			NetAcceleration[i] = (float) 0.2 ;
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
		
		/*calculation bhumil*/
		try{
			 fos = context.openFileOutput("thresh.txt", Context.MODE_PRIVATE);
			 osr = new OutputStreamWriter(fos);		
			 
			 for(int i = 0; i < 4; i++)
				 data+=(NetAcceleration[i]+"|");
			 data+="$";
			 for(int i = 0; i < 4; i++)
				 data+=(Acceleration[i]+"|");
			 data+="$";
			 for(int i = 0; i < 4; i++)
				 data+=(MagneticField[i]+"|");
			 data+="$";
			 for(int i = 0; i < 4; i++)
				 data+=(Orientation[i]+"|");
			 data+="$";
			 for(int i = 0; i < 4; i++)
				 data+=(Gravity[i]+"|");
			 data+="\n";
			 
			 osr.write(data);
			 isr.close();
			 fis.close();
			}catch(Exception e){
				Log.d("lol sensorthreshold", "File read error");
			}
		
		
		

	}
}
