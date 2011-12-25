package com.graph;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * Your Path on the Graph
 */
public class PlotGraph extends AbstractDemoChart{
  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
    return "Recommended Path";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "Guide Me! Pro Edition";
  }

  /**
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context context) {
    String[] titles = new String[] { "Path"};
    List<double[]> x = new ArrayList<double[]>();
    for (int i = 0; i < titles.length; i++) {
      x.add(new double[] { 0, 12 });
    }
    List<double[]> values = new ArrayList<double[]>();
    values.add(new double[] { 0,12 });
    int[] colors = new int[] { Color.BLUE };
    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE};
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    setChartSettings(renderer, "GuideMe! Pro Edition", "X", "Y", 0.0, 20, 0, 20,
        Color.LTGRAY, Color.LTGRAY);
    renderer.setXLabels(12);
    renderer.setYLabels(10);
    renderer.setShowGrid(true);
    renderer.setXLabelsAlign(Align.RIGHT);
    renderer.setYLabelsAlign(Align.RIGHT);
    renderer.setZoomButtonsVisible(true);
    renderer.setPanLimits(new double[] { -100, 200, -100, 400 });
    renderer.setZoomLimits(new double[] { -100, 200, -100, 400 });

    Intent intent = ChartFactory.getLineChartIntent(context, buildDataset(titles, x, values),
        renderer, "Path Graph");
    return intent;
  }

}