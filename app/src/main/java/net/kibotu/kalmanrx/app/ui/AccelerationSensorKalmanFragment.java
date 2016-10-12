package net.kibotu.kalmanrx.app.ui;

import net.kibotu.kalmanrx.KalmanRx;
import net.kibotu.kalmanrx.app.misc.SensorEventObservableFactory;

import rx.Subscription;

/**
 * Created by jan.rabe on 11/10/16.
 */

public class AccelerationSensorKalmanFragment extends AccelerationSensorFragment {

    @Override
    protected Subscription createSensorSubscription() {
        return KalmanRx.createFrom3D(SensorEventObservableFactory
                .createSensorEventObservable(sensorType(), sensorDelay())
                .map(e -> e.values))
                .subscribe(this::process, Throwable::printStackTrace);
    }

    protected void process(float x) {
        process(x, 0, 0);
    }

    protected void process(float x, float y, float z) {
        addToGraph(x, y, z);
    }
}