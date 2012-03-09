package com.sensor;

import android.os.Handler;
import android.os.Message;

class DelayHandler extends Handler {
	MovementService movService = null;
	public int delayType = -1;

	public static final class DelayType {
		public static final int CALCULATE = 0;
		public static final int SENSOR_UPDATE = 1;
		public static final int DISPLAY_UPDATE = 2;
	}

	public DelayHandler(MovementService movService, int delayType) {
		super();
		this.delayType = delayType;
		this.movService = movService;
	}

	public void handleMessage(Message msg) {
		switch (this.delayType) {

		case DelayHandler.DelayType.CALCULATE:
			movService.executeCalculation();
			break;

		case DelayHandler.DelayType.SENSOR_UPDATE:
			movService.enableSensorUpdate();
			break;

		default:
		}

	}
}