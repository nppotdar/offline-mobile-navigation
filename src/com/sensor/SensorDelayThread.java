package com.sensor;

import android.os.Handler;

class SensorDelayThread extends Thread {

	private Boolean threadAlive = true;
	public Handler mHandler;
	public int delayTime = 1000;

	public SensorDelayThread(SensorDelayHandler myHandler, int delayTime) {
		this.mHandler = myHandler;
		this.delayTime = delayTime;
	}

	public void stopThread() {
		synchronized (this) {
			threadAlive = false;
		}
	}

	public void run() {
		do {
			mHandler.sendEmptyMessage(0);
			try {
				Thread.sleep(delayTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (threadAlive);
	}
}