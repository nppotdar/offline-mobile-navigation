package com.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.graph.IDemoChart;
import com.graph.PlotGraph;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private IDemoChart mCharts = new PlotGraph();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = mCharts.execute(this);
        startActivity(intent);
        
        MyHandler myHandler=new MyHandler(intent);    
        
        MyThread mThread=new MyThread(myHandler);
        mThread.start();   
        
    }
}