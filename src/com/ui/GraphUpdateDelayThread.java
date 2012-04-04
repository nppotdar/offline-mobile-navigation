package com.ui;

public class GraphUpdateDelayThread extends Thread {
	
	public UpdateDelayHandler mHandler;
	private Boolean threadAlive = true;
	public int delayTime = 1000;
	
	public GraphUpdateDelayThread(UpdateDelayHandler myHandler, int delayTime) {
		this.delayTime = delayTime;
		this.mHandler = myHandler;
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