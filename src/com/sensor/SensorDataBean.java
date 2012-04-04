package com.sensor;
/* Copyrighted(ghanta :P ): 
 * By Dipak Pawar & Bhumil Haria 
 */
public class SensorDataBean {
	// The data processed continuously from the sensors necessary for navigation

	double x = 0;
	double y = 0;
	double z = 0;
	SensorDataBean previous = null;
	
	SensorDataBean(){
		
	}
	
	public SensorDataBean update(){
		return this;
	}
	
	public int[] plotDataPoint(){
		///returns the points(whether (x,y,z) or (x,y)) to be plotted on the graph.
		return null;
	}
}
