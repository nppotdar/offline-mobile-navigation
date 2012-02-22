package com.main;

import android.os.Handler;


public class GraphUpdateThread extends Thread
{
	public Handler mHandler;
	
	public GraphUpdateThread(UpdateHandler myHandler)
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