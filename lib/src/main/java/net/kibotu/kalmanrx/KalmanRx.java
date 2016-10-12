package net.kibotu.kalmanrx;

import net.kibotu.kalmanrx.jama.Matrix;
import net.kibotu.kalmanrx.jkalman.JKalman;

import rx.Observable;

/**
 * Created by jan.rabe on 11/10/16.
 * <p>
 */

public class KalmanRx {

    /**
     * Smoothens float value stream using kalman filter.
     *
     * @param stream Float Stream.
     * @return Observable.
     */
    public static Observable<Float> createFrom1D(Observable<Float> stream) {

        final JKalman kalman = new JKalman(2, 1);

        // measurement [x]
        final Matrix m = new Matrix(1, 1);

        // transitions for x, dx
        double[][] tr = {{1, 0},
                {0, 1}};
        kalman.setTransition_matrix(new Matrix(tr));

        // 1s somewhere?
        kalman.setError_cov_post(kalman.getError_cov_post().identity());

        return Observable.create(subscriber -> {

            if (subscriber.isUnsubscribed()) {
                return;
            }

            stream.subscribe(value -> {

                m.set(0, 0, value);

                // state [x, dx]
                Matrix s = kalman.Predict();

                // corrected state [x, dx]
                Matrix c = kalman.Correct(m);

                subscriber.onNext((float) c.get(0, 0));
            });
        });
    }

    /**
     * Smoothens (float,float) value stream using kalman filter.
     *
     * @param stream Float Stream.
     * @return Observable.
     */
    public static Observable<float[]> createFrom2D(Observable<float[]> stream) {

        final JKalman kalman = new JKalman(4, 2);

        // measurement [x]
        final Matrix m = new Matrix(4, 1);

        // transitions for x, y, dx, dy
        double[][] tr = {{1, 0, 1, 0},
                {0, 1, 0, 1},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
        kalman.setTransition_matrix(new Matrix(tr));

        // 1s somewhere?
        kalman.setError_cov_post(kalman.getError_cov_post().identity());

        final float[] buffer = new float[2];

        return Observable.create(subscriber -> {

            if (subscriber.isUnsubscribed()) {
                return;
            }

            stream.subscribe(values -> {

                m.set(0, 0, values[0]);
                m.set(1, 0, values[1]);

                // state [x, dx]
                Matrix s = kalman.Predict();

                // corrected state [x, dx]
                Matrix c = kalman.Correct(m);

                buffer[0] = (float) c.get(0, 0);
                buffer[1] = (float) c.get(1, 0);

                subscriber.onNext(buffer);
            });
        });
    }

    /**
     * Smoothens (float,float,float) value stream using kalman filter.
     *
     * @param stream Float Stream.
     * @return Observable.
     */
    public static Observable<float[]> createFrom3D(Observable<float[]> stream) {

        final JKalman kalman = new JKalman(6, 3);

        // measurement [x, y, z]
        Matrix m = new Matrix(3, 1);

        // transitions for x, y, z, dx, dy, dz (velocity transitions)
        double[][] tr = {{1, 0, 0, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 0, 1, 0, 0, 1},
                {0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 1}};
        kalman.setTransition_matrix(new Matrix(tr));

        // 1s somewhere?
        kalman.setError_cov_post(kalman.getError_cov_post().identity());

        final float[] buffer = new float[3];

        return Observable.create(subscriber -> {

            if (subscriber.isUnsubscribed()) {
                return;
            }

            stream.subscribe(values -> {

                m.set(0, 0, values[0]);
                m.set(1, 0, values[1]);
                m.set(2, 0, values[2]);

                // state [x, y, z, dx, dy, dz]
                Matrix s = kalman.Predict();

                // corrected state [x, y,z, dx, dy, dz, dxyz]
                Matrix c = kalman.Correct(m);

                buffer[0] = (float) c.get(0, 0);
                buffer[1] = (float) c.get(1, 0);
                buffer[2] = (float) c.get(2, 0);

                subscriber.onNext(buffer);
            });
        });
    }

    /**
     * Applies low pass filter for (float,float,float) value stream.
     *
     * @param stream Float Stream.
     * @return Observable.
     */
    public static Observable<float[]> createLowPassFilter(Observable<float[]> stream) {
        return createLowPassFilter(stream, 0.8f);
    }

    public static Observable<float[]> createLowPassFilter(Observable<float[]> stream, final float alpha) {

        final float[] output = new float[3];

        final float[] gravity = new float[3];

        return Observable.create(subscriber -> {

            if (subscriber.isUnsubscribed()) {
                return;
            }

            stream.subscribe(values -> {

                // skip invalid values
                if (values == null || values.length != 3)
                    return;

                // apply low pass filter
                applyLowPassFilter(values, output, gravity, alpha);

                // pass values
                subscriber.onNext(output);
            });
        });
    }

    /**
     * In this example, alpha is calculated as t / (t + dT),
     * where t is the low-pass filter's time-constant and
     * dT is the event delivery rate.
     */
    static void applyLowPassFilter(float[] input, float[] output, float[] gravity, float alpha) {

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * input[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * input[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * input[2];

        // Remove the gravity contribution with the high-pass filter.
        output[0] = input[0] - gravity[0];
        output[1] = input[1] - gravity[1];
        output[2] = input[2] - gravity[2];
    }
}
