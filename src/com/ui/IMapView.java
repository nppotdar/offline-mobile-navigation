package com.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.main.R;

public class IMapView extends View {

	private static final int INVALID_POINTER_ID = -1;

	// TODO: Code to restrict movement beyond borders
	// TODO: Extract values from file
	// TODO: Maintain a set of points, join consecutive points

	private Drawable mImage;
	private float mPosX;
	private float mPosY;
	private Paint p = new Paint();
	private float mLastTouchX;
	private float mLastTouchY;
	private int mActivePointerId = INVALID_POINTER_ID;
	File f = null;
	FileInputStream fis = null;
	InputStreamReader isr = null;
	String data = "";

	public class Coordinate {
		public Coordinate(float x, float y) {
			this.x = x;
			this.y = y;
		}

		private float x;
		private float y;

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}
	}

	ArrayList<Coordinate> selfPathClist = new ArrayList<Coordinate>();
	ArrayList<Coordinate> searchPathClist = new ArrayList<Coordinate>();

	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;

	public IMapView(Context context) {
		this(context, null, 0);
		mImage = getResources().getDrawable(R.drawable.vj);

		mImage.setBounds(0, 0, mImage.getIntrinsicWidth(),
				mImage.getIntrinsicHeight());
	}

	public IMapView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public IMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Let the ScaleGestureDetector inspect all events.
		mScaleDetector.onTouchEvent(ev);

		final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			final float x = ev.getX();
			final float y = ev.getY();

			mLastTouchX = x;
			mLastTouchY = y;
			mActivePointerId = ev.getPointerId(0);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			final int pointerIndex = ev.findPointerIndex(mActivePointerId);
			final float x = ev.getX(pointerIndex);
			final float y = ev.getY(pointerIndex);

			// Only move if the ScaleGestureDetector isn't processing a gesture.
			if (!mScaleDetector.isInProgress()) {
				final float dx = x - mLastTouchX;
				final float dy = y - mLastTouchY;

				mPosX += dx;
				mPosY += dy;

				invalidate();
			}

			mLastTouchX = x;
			mLastTouchY = y;

			break;
		}

		case MotionEvent.ACTION_UP: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP: {
			final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			final int pointerId = ev.getPointerId(pointerIndex);
			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastTouchX = ev.getX(newPointerIndex);
				mLastTouchY = ev.getY(newPointerIndex);
				mActivePointerId = ev.getPointerId(newPointerIndex);
			}
			break;
		}
		}

		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		Log.d("DEBUG", "X: " + mPosX + " Y: " + mPosY);
		canvas.translate(mPosX, mPosY);
		canvas.scale(mScaleFactor, mScaleFactor);
		mImage.draw(canvas);

		// selfPath
		p.setStrokeWidth(3.0f);
		p.setColor(Color.BLUE);
		for (int i = 0; i < selfPathClist.size() - 1; i++) {
			canvas.drawLine(selfPathClist.get(i).getX(), selfPathClist.get(i)
					.getY(), selfPathClist.get(i + 1).getX(), selfPathClist
					.get(i + 1).getY(), p);
		}
		// searchPath
		p.setStrokeWidth(3.0f);
		p.setColor(Color.RED);
		for (int i = 0; i < searchPathClist.size() - 1; i++) {
			canvas.drawLine(searchPathClist.get(i).x, searchPathClist.get(i).y,
					searchPathClist.get(i + 1).x, searchPathClist.get(i + 1).y,
					p);
		}

		canvas.restore();
	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 5.0f));

			invalidate();
			return true;
		}
	}

	public void setPaintColor(int c) {
		p.setColor(c);
	}

	public void setPaint(Paint pi) {
		p = pi;
	}

	private void initFileHandling() {
		// open FILE objects
		 try{
			 fis = getContext().openFileInput("");
			 isr = new InputStreamReader(fis);		 
	     }catch(Exception e){
		 }
	}
	private void readFromFile() {
		// open FILE objects
		 String data = null;
		 try{
		 char[] inputBuffer = new char[fis.available()];
		 isr.read(inputBuffer);
		 data = new String(inputBuffer);
		 int x = 0;
		 String lines[] = data.split("\n");
		 for(String aline: lines)
		 {
			 String temp[] = aline.split("|");
			//TODO: Read the values to temp, now just plot
		 }
		 
		 isr.close();
		 fis.close();
		 }catch(IOException e){
			 Log.d("lol", "IOEException read error");
		 }
	}

	public void populateSearchPathList() {
		//read searchpath file
	}

	public void populateSelfPathList() {
		//read selfpath file		
	}

	public void dynamicUpdate(int i) {
		File f = new File("");
	}

	public void setPoints(float x, float y) {
		selfPathClist.add(new Coordinate(x, y));
		Log.d("lol", "lol:" + x + ";" + y);
		invalidate();
	}
}
