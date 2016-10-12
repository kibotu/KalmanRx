package net.kibotu.kalmanrx.app.ui;

/**
 * Created by jan.rabe on 11/10/16.
 */

public class AccelerationSensorLowPassFragment extends AccelerationSensorFragment {

    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];

    @Override
    protected void process(float x, float y, float z) {

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

        addToGraph(linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]);
    }
}
