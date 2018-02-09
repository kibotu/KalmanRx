package net.kibotu.kalmanrx.app.ui;

import net.kibotu.kalmanrx.KalmanRx;
import net.kibotu.kalmanrx.app.misc.SensorEventObservableFactory;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jan.rabe on 11/10/16.
 */

public class AccelerationSensorLowPassFragment extends AccelerationSensorFragment {

    @Override
    protected Subscription createSensorSubscription() {
        return KalmanRx.createLowPassFilter(SensorEventObservableFactory
                .createSensorEventObservable(sensorType(), sensorDelay())
                .map(e -> e.values))
                .subscribe(this::process, Throwable::printStackTrace);
    }

    @Override
    protected void process(float x, float y, float z) {
        addToGraph(x, y, z);
    }

}
