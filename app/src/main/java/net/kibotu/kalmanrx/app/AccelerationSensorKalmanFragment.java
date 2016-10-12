package net.kibotu.kalmanrx.app;

import android.hardware.SensorEvent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jjoe64.graphview.series.DataPoint;

import net.kibotu.kalmanrx.jama.Matrix;
import net.kibotu.kalmanrx.jkalman.JKalman;

/**
 * Created by jan.rabe on 11/10/16.
 */

public class AccelerationSensorKalmanFragment extends AccelerationSensorFragment {

    JKalman kalman;

    /**
     * measurement [x]
     */
    private Matrix m;

    /**
     * state [x, y, dx, dy, dxy]
     */
    private Matrix s;

    /**
     * corrected state [x, y, dx, dy, dxy]
     */
    private Matrix c;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            initKalman();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initKalman() throws Exception {
        kalman = new JKalman(4, 2);

        // init
        s = new Matrix(4, 1);
        c = new Matrix(4, 1);

        // transitions for x, y, dx, dy
        double[][] tr = {{1, 0, 1, 0},
                {0, 1, 0, 1},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
        kalman.setTransition_matrix(new Matrix(tr));

        // 1s somewhere?
        kalman.setError_cov_post(kalman.getError_cov_post().identity());
    }

    @Override
    protected void set(SensorEvent event) {

        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        // init
        if (m == null) {
            m = new Matrix(2, 1); // measurement [x]
            m.set(0, 0, x);
            m.set(1, 0, y);
        }

        s = kalman.Predict();

        m.set(0, 0, x);
        m.set(1, 0, y);

        c = kalman.Correct(m);

        double dX = c.get(0, 0);
        double dY = c.get(1, 0);

        xLabel.setText(String.valueOf(dX));
        yLabel.setText(String.valueOf(dY));

        graph2LastXValue += 1d;
        xSeries.appendData(new DataPoint(graph2LastXValue, dX), true, maxDataPoints);
        ySeries.appendData(new DataPoint(graph2LastXValue, dY), true, maxDataPoints);
        // zSeries.appendData(new DataPoint(graph2LastXValue, kZ), true, maxDataPoints);
    }
}
