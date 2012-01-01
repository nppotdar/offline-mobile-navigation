package com.sensor;

import android.os.Handler;
import android.os.Message;

class MyHandler extends Handler 
{ 
	CPSActivity cpsActivity;
    
	public MyHandler(CPSActivity cpsActivity)
	{	
        super();
        this.cpsActivity=cpsActivity;
    }
    
    public void handleMessage(Message msg) 
    { 
    	 cpsActivity.execute(); 
    }
} 