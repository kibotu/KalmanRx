package net.kibotu.kalmanrx.app.ui;

import net.kibotu.kalmanrx.KalmanRx;
import net.kibotu.kalmanrx.app.misc.SensorEventObservableFactory;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jan.rabe on 11/10/16.
 */

public class AccelerationSensorKalmanFragment extends AccelerationSensorFragment {

    @Override
    protected Subscription createSensorSubscription() {

        // 1) float stream
        Observable<float[]> floatStream = SensorEventObservableFactory
                .createSensorEventObservable(sensorType(), sensorDelay())
                .map(e -> e.values);

        // 2) apply kalman filter
        Observable<float[]> kalmanFilterStream = KalmanRx.createFrom3D(floatStream);

        // (optional) apply low pass filter
        Observable<float[]> lowPassFilter = KalmanRx.createLowPassFilter(kalmanFilterStream);

        return lowPassFilter.subscribe(this::process, Throwable::printStackTrace);
    }

    @Override
    protected void process(float x, float y, float z) {
        addToGraph(x, y, z);
    }
}