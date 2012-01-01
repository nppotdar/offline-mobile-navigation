package com.sensor;

import android.os.Handler;

class MyThread extends Thread
{
	public Handler mHandler;
	
	public MyThread(MyHandler myHandler)
	{
		mHandler=myHandler;
	}
   
	
	public void run()
	{
    	do
    	{
         mHandler.sendEmptyMessage(0);
           try 
           {
			Thread.sleep(1000);
		   }
           catch (InterruptedException e) 
           {
			e.printStackTrace();
		   }
        }while(true);
   }
	
}