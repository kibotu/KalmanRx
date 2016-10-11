package net.kibotu.kalmanrx.app;

import android.hardware.SensorEvent;

import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by jan.rabe on 11/10/16.
 */

public class AccelerationSensorLowPassFragment extends AccelerationSensorFragment {

    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];

    @Override
    protected void set(SensorEvent event) {
        set(event.values[0], event.values[1], event.values[2]);
    }

    protected void set(final float x, final float y, final float z) {

        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        final float alpha = 0.8f;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
        gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
        gravity[2] = alpha * gravity[2] + (1 - alpha) * z;

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = x - gravity[0];
        linear_acceleration[1] = y - gravity[1];
        linear_acceleration[2] = z - gravity[2];

        xLabel.setText(String.valueOf(linear_acceleration[0]));
        yLabel.setText(String.valueOf(linear_acceleration[1]));
        zLabel.setText(String.valueOf(linear_acceleration[2]));

        graph2LastXValue += 1d;
        xSeries.appendData(new DataPoint(graph2LastXValue, linear_acceleration[0]), true, maxDataPoints);
        ySeries.appendData(new DataPoint(graph2LastXValue, linear_acceleration[1]), true, maxDataPoints);
        zSeries.appendData(new DataPoint(graph2LastXValue, linear_acceleration[2]), true, maxDataPoints);
    }
}
