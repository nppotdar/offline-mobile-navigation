package com.main;

import android.os.Handler;
import android.os.Message;

import com.graph.XYChartBuilder;

public class MyHandler extends Handler 
{ 
	XYChartBuilder xyActivity;
    int i =0;
	public MyHandler(XYChartBuilder xyActivity)
	{	
        super();
        this.xyActivity=xyActivity;
    }
    
    public void handleMessage(Message msg) 
    { 
    	if(i<50)
    	{	
    	xyActivity.dynamicUpdate(i);
    	i++;
    	}	
    }
} 