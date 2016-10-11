package net.kibotu.kalmanrx.app;

import android.hardware.SensorEvent;

import com.jjoe64.graphview.series.DataPoint;

import net.kibotu.kalmanrx.Kalman;

/**
 * Created by jan.rabe on 11/10/16.
 */

public class AccelerationSensorKalmanFragment extends AccelerationSensorFragment {

    Kalman kalmanX = new Kalman().SetDefaults();
    Kalman kalmanY = new Kalman().SetDefaults();

    @Override
    protected void set(SensorEvent event) {

        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        double kX = kalmanX.getAngle(x, event.accuracy, event.timestamp);
        double kY = kalmanY.getAngle(y, event.accuracy, event.timestamp);

        xLabel.setText(String.valueOf(kX));
        yLabel.setText(String.valueOf(kY));
        // zLabel.setText(String.valueOf(z));

        graph2LastXValue += 1d;
        xSeries.appendData(new DataPoint(graph2LastXValue, kX), true, maxDataPoints);
        ySeries.appendData(new DataPoint(graph2LastXValue, kY), true, maxDataPoints);
        // zSeries.appendData(new DataPoint(graph2LastXValue, z), true, maxDataPoints);
    }
}
