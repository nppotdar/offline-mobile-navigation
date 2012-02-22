package com.main;

import android.os.Handler;
import android.os.Message;

import com.graph.GraphPlotter;

public class UpdateHandler extends Handler 
{ 
	GraphPlotter xyActivity;
    int i =0;
	public UpdateHandler(GraphPlotter xyActivity)
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