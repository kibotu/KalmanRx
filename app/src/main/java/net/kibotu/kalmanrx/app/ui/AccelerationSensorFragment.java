package net.kibotu.kalmanrx.app.ui;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import net.kibotu.kalmanrx.app.R;

import static com.common.android.utils.extensions.ResourceExtensions.color;

/**
 * Created by Nyaruhodo on 18.03.2016.
 * <p>
 * <a href="http://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-accel">Using the Accelerometer</a>
 */
public class AccelerationSensorFragment extends SensorValueFragment {

    protected LineGraphSeries<DataPoint> xSeries;
    protected LineGraphSeries<DataPoint> ySeries;
    protected LineGraphSeries<DataPoint> zSeries;
    protected double graph2LastXValue;

    protected int maxDataPoints = 1000;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init example series data

        xSeries = new LineGraphSeries<>();
        xSeries.setColor(color(R.color.red));
        ySeries = new LineGraphSeries<>();
        ySeries.setColor(color(R.color.green));
        zSeries = new LineGraphSeries<>();
        zSeries.setColor(color(R.color.blue));
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(maxDataPoints);
        graphView.addSeries(xSeries);
        graphView.addSeries(ySeries);
        graphView.addSeries(zSeries);
    }

    @Override
    protected void process(float x, float y, float z) {
        addToGraph(x, y, z);
    }

    protected void addToGraph(float x, float y, float z) {
        xLabel.setText(String.valueOf(x));
        yLabel.setText(String.valueOf(y));
        zLabel.setText(String.valueOf(z));

        graph2LastXValue += 1d;
        xSeries.appendData(new DataPoint(graph2LastXValue, x), true, maxDataPoints);
        ySeries.appendData(new DataPoint(graph2LastXValue, y), true, maxDataPoints);
        zSeries.appendData(new DataPoint(graph2LastXValue, z), true, maxDataPoints);
    }

    @Override
    protected int sensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }

    @Override
    public int sensorDelay() {
        return SensorManager.SENSOR_DELAY_UI;
    }
}