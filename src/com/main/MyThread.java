package com.main;

import android.os.Handler;


public class MyThread extends Thread
{
	public Handler mHandler;
	
	public MyThread(MyHandler myHandler)
	{
		this.mHandler=myHandler;
	}
   
	
	public void run()
	{
    	do
    	{
         mHandler.sendEmptyMessage(0);
           try 
           {
			Thread.sleep(5000);
		   }
           catch (InterruptedException e) 
           {
			e.printStackTrace();
		   }
        }while(true);
   }
	
}