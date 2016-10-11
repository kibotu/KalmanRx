package net.kibotu.kalmanrx.app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.common.android.utils.logging.Logger;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static com.common.android.utils.extensions.ResourceExtensions.color;
import static java.text.MessageFormat.format;
import static net.kibotu.android.deviceinfo.library.ViewHelper.getAccuracyName;
import static net.kibotu.android.deviceinfo.library.ViewHelper.getSensorName;

/**
 * Created by Nyaruhodo on 18.03.2016.
 * <p>
 * <a href="http://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-accel">Using the Accelerometer</a>
 */
public abstract class AccelerationSensorFragment extends SensorValueFragment {

    protected LineGraphSeries<DataPoint> xSeries;
    protected LineGraphSeries<DataPoint> ySeries;
    protected LineGraphSeries<DataPoint> zSeries;
    protected double graph2LastXValue;


    protected int maxDataPoints = 1000;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
        // graphView.addSeries(ySeries);
        // graphView.addSeries(zSeries);
    }

    @Override
    protected int sensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }

    protected SensorEventListener createSensorEventListener() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Logger.v(tag(), format("[{0} | {1} | {2} | {3}]", event.timestamp, event.sensor, event.accuracy, event.values));

                set(event);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Logger.v(tag(), "[onAccuracyChanged] " + getSensorName(sensor) + " to " + getAccuracyName(accuracy));
            }
        };
    }

    protected abstract void set(SensorEvent event);

    @Override
    public int sensorDelay() {
        return SensorManager.SENSOR_DELAY_UI;
    }
}