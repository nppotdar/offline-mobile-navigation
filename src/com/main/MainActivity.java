package com.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.graph.GraphPlotter;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Intent x = new Intent(getApplicationContext(), GraphPlotter.class);
    	startActivity(x);
    }
}