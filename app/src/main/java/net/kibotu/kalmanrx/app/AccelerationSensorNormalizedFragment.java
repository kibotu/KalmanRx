package net.kibotu.kalmanrx.app;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.jjoe64.graphview.series.DataPoint;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Created by jan.rabe on 11/10/16.
 */

public class AccelerationSensorNormalizedFragment extends AccelerationSensorFragment {

    // Create a constant to convert nanoseconds to seconds.
    protected static final float NS2S = 1.0f / 1000000000.0f;
    protected final float[] deltaRotationVector = new float[4];
    protected float timestamp;
    protected static final float EPSILON = 0.000000001f;

    @Override
    protected void set(SensorEvent event) {
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Calculate the angular speed of the sample
            float omegaMagnitude = (float) sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

            // Normalize the rotation vector if it's big enough to get the axis
            // (that is, EPSILON should represent your maximum allowable margin of error)
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = (float) sin(thetaOverTwo);
            float cosThetaOverTwo = (float) cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        // rotationCurrent = rotationCurrent * deltaRotationMatrix;


        graph2LastXValue += 1d;
        xSeries.appendData(new DataPoint(graph2LastXValue, deltaRotationVector[0]), true, maxDataPoints);
        ySeries.appendData(new DataPoint(graph2LastXValue, deltaRotationVector[1]), true, maxDataPoints);
        zSeries.appendData(new DataPoint(graph2LastXValue, deltaRotationVector[2]), true, maxDataPoints);
    }
}
