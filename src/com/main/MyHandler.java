package com.main;

import com.graph.PlotGraph;

import android.os.Handler;
import android.os.Message;

class MyHandler extends Handler 
{ 
	PlotGraph plotActivity;
    
	public MyHandler(PlotGraph cpsActivity)
	{	
        super();
        this.plotActivity = plotActivity;
    }
    
    public void handleMessage(Message msg) 
    { 
    	 plotActivity.execute(); 
    }
} 