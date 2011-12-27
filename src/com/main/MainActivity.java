package com.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.graph.XYChartBuilder;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Intent x = new Intent(getApplicationContext(), XYChartBuilder.class);
    	startActivity(x);
    }
}