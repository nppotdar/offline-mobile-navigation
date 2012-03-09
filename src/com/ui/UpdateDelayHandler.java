package com.ui;

import android.os.Handler;
import android.os.Message;

public class UpdateDelayHandler extends Handler {
	
	GraphPlotter xyActivity;
	int i = 0;

	public UpdateDelayHandler(GraphPlotter xyActivity) {
		super();
		this.xyActivity = xyActivity;
	}

	public void handleMessage(Message msg) {
		// if (i < 50) {
			xyActivity.dynamicUpdate(i);
			i++;
		// }
	}
}