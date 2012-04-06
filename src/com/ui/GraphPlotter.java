/*
 * Guide Me!
 * By Nanga Don B)
 */
package com.ui;

import java.io.File;
import java.io.FileOutputStream;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

//import com.gps.GPSService;
import com.main.MainActivity;
import com.main.R;

import com.sensor.MotionState;
import com.sensor.MovementService;

public class GraphPlotter extends Activity {
	public static final String TYPE = "type";
	public static final String TAG = "GraphPlotter";

	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private XYSeries mCurrentSeries;
	private XYSeriesRenderer mCurrentRenderer;
	private String mDateFormat;
	private GraphicalView mChartView;

	UpdateDelayHandler displayDelayHandler;
	GraphUpdateDelayThread dispDelayThread;

	private int index = 0;
	private int displayRefreshDelay = 500;

	// data transfer from service
	MotionState currentState = new MotionState();
	private float[] totDisp;
	private int pointNumber = -1;

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		mDataset = (XYMultipleSeriesDataset) savedState
				.getSerializable("dataset");
		mRenderer = (XYMultipleSeriesRenderer) savedState
				.getSerializable("renderer");
		mCurrentSeries = (XYSeries) savedState
				.getSerializable("current_series");
		mCurrentRenderer = (XYSeriesRenderer) savedState
				.getSerializable("current_renderer");
		mDateFormat = savedState.getString("date_format");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("dataset", mDataset);
		outState.putSerializable("renderer", mRenderer);
		outState.putSerializable("current_series", mCurrentSeries);
		outState.putSerializable("current_renderer", mCurrentRenderer);
		outState.putString("date_format", mDateFormat);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		displayDelayHandler = new UpdateDelayHandler(this);
		dispDelayThread = new GraphUpdateDelayThread(displayDelayHandler,
				displayRefreshDelay);

		setContentView(R.layout.xy_chart);
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
		mRenderer.setAxisTitleTextSize(16);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setLegendTextSize(15);
		mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setPointSize(5);
		mRenderer.setXLabels(5);
		mRenderer.setYLabels(5);

		// code for setting start and goal points
		{
			String seriesTitle = "Recommended Path";
			XYSeries series = new XYSeries(seriesTitle);
			mDataset.addSeries(series);
			mCurrentSeries = series;
			XYSeriesRenderer renderer = new XYSeriesRenderer();
			mRenderer.addSeriesRenderer(renderer);
			renderer.setPointStyle(PointStyle.DIAMOND);
			renderer.setColor(Color.CYAN);
			renderer.setFillPoints(true);
			mCurrentRenderer = renderer;

			// Sachin's Goal Coordinates
			double xs = 1;
			double ys = 1;
			double xg = 35;
			double yg = 25;
			// end of Sachin's Goal Coordinates

			mCurrentSeries.add(xs, ys);
			mCurrentSeries.add(xg, yg);
			mRenderer.setXAxisMin(xs - 5);
			mRenderer.setXAxisMax(xg + 5);
			mRenderer.setYAxisMin(ys - 5);
			mRenderer.setYAxisMax(yg + 5);
			mRenderer.setShowGrid(true);
		}
		// code for the path the user traverses
		{
			String seriesTitle = "Your Path";
			XYSeries series = new XYSeries(seriesTitle);
			mDataset.addSeries(series);
			mCurrentSeries = series;
			XYSeriesRenderer renderer = new XYSeriesRenderer();
			mRenderer.addSeriesRenderer(renderer);
			renderer.setPointStyle(PointStyle.TRIANGLE);
			renderer.setColor(Color.GREEN);
			renderer.setFillPoints(true);
			mCurrentRenderer = renderer;

			mCurrentSeries.add(0, 0);
		}

		dispDelayThread.start();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getLineChartView(this, mDataset,
					mRenderer);
			mRenderer.setClickEnabled(true);
			mRenderer.setSelectableBuffer(100);
			mChartView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					double[] xy = mChartView.toRealPoint(0);
					if (seriesSelection == null) {
						Toast.makeText(GraphPlotter.this,
								"No chart element was clicked",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(
								GraphPlotter.this,
								"Chart element in series index "
										+ seriesSelection.getSeriesIndex()
										+ " data point index "
										+ seriesSelection.getPointIndex()
										+ " was clicked"
										+ " closest point value X="
										+ seriesSelection.getXValue() + ", Y="
										+ seriesSelection.getValue()
										+ " clicked point value X="
										+ (float) xy[0] + ", Y="
										+ (float) xy[1], Toast.LENGTH_SHORT)
								.show();
					}
				}
			});

			mChartView.setOnLongClickListener(new View.OnLongClickListener() {
				public boolean onLongClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						Toast.makeText(GraphPlotter.this,
								"No chart element was long pressed",
								Toast.LENGTH_SHORT);
						return false; // no chart element was long pressed, so
										// let something
						// else handle the event
					} else {
						Toast.makeText(GraphPlotter.this,
								"Chart element in series index "
										+ seriesSelection.getSeriesIndex()
										+ " data point index "
										+ seriesSelection.getPointIndex()
										+ " was long pressed",
								Toast.LENGTH_SHORT);
						return true; // the element was long pressed - the event
										// has been
						// handled
					}
				}
			});
			mChartView.addZoomListener(new ZoomListener() {
				public void zoomApplied(ZoomEvent e) {
					String type = "out";
					if (e.isZoomIn()) {
						type = "in";
					}
					System.out.println("Zoom " + type + " rate "
							+ e.getZoomRate());
				}

				public void zoomReset() {
					System.out.println("Reset");
				}
			}, true, true);
			mChartView.addPanListener(new PanListener() {
				public void panApplied() {
					System.out.println("New X range=["
							+ mRenderer.getXAxisMin() + ", "
							+ mRenderer.getXAxisMax() + "], Y range=["
							+ mRenderer.getYAxisMax() + ", "
							+ mRenderer.getYAxisMax() + "]");
				}
			});
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		} else {
			mChartView.repaint();
		}
		// dispDelayThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.guimenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.startserv:
			Log.d(TAG, "onClick: Starting service");
			startService(new Intent(this, MovementService.class));
			break;
		case R.id.stopserv:
			Log.d(TAG, "onClick: Stopping service");
			stopService(new Intent(this, MovementService.class));
			break;
		case R.id.textui:
			Log.d(TAG, "onClick: Switching to textui");
			Intent x = new Intent(getApplicationContext(), MainActivity.class);
			startActivityIfNeeded(x, Intent.FLAG_ACTIVITY_SINGLE_TOP);
			break;
		}
		return true;
	}

	public void dynamicUpdate(int i) {

		// Dipak and Bhumil's part to be added here.
		currentState = MovementService.outState;
		totDisp = MovementService.totDisp;
		// End of Sensor Part

		// Increment point number (index starting at 0)
		pointNumber += 1;
		mCurrentSeries.add(currentState.earthDisplacement[0],
				currentState.earthDisplacement[1]);

		if (mChartView != null)
			mChartView.repaint();

		// Previous wrong code:
		// XYSeries temp = mDataset.getSeriesAt(seriesIndex);
		// XYSeries series = new XYSeries("Edge: " + (seriesIndex + 1));
		// series.add(i,i);
		// series.add(totDisp[0], totDisp[1]);

		// mDataset.removeSeries(1);
		// mDataset.addSeries(1, series);
	}

	public void saveAsImage() {
		Bitmap bitmap = mChartView.toBitmap();
		try {
			File file = new File(Environment.getExternalStorageDirectory(),
					"test" + index++ + ".png");
			FileOutputStream output = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
