package net.kibotu.kalmanrx.app;

import android.hardware.SensorEvent;

import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by jan.rabe on 11/10/16.
 */

public class AccelerationSensorRawFragment extends AccelerationSensorFragment {

    @Override
    protected void set(SensorEvent event) {
        set(event.values[0], event.values[1], event.values[2]);
    }

    protected void set(float x, float y, float z) {

        xLabel.setText(String.valueOf(x));
        yLabel.setText(String.valueOf(y));
        zLabel.setText(String.valueOf(z));

        graph2LastXValue += 1d;
        xSeries.appendData(new DataPoint(graph2LastXValue, x), true, maxDataPoints);
        ySeries.appendData(new DataPoint(graph2LastXValue, y), true, maxDataPoints);
        zSeries.appendData(new DataPoint(graph2LastXValue, z), true, maxDataPoints);
    }
}
