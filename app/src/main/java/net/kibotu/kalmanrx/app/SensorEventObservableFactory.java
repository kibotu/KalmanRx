package net.kibotu.kalmanrx.app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.android.MainThreadSubscription;

/**
 * Allows to treat sensor events as Observable
 *
 * Liberated: https://github.com/ArkadyGamza/ShakeDetector/tree/master/app/src/main/java/com/arkadygamza/shakedetector
 */
public class SensorEventObservableFactory {

    public static Observable<SensorEvent> createSensorEventObservable(@NonNull Sensor sensor, @NonNull SensorManager sensorManager, @SensorDelay int sensorDelay) {
        return Observable.create(subscriber -> {
            MainThreadSubscription.verifyMainThread();

            SensorEventListener listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (subscriber.isUnsubscribed()) {
                        return;
                    }

                    subscriber.onNext(event);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // NO-OP
                }
            };

            sensorManager.registerListener(listener, sensor, sensorDelay);

            // unregister listener in main thread when being unsubscribed
            subscriber.add(new MainThreadSubscription() {
                @Override
                protected void onUnsubscribe() {
                    sensorManager.unregisterListener(listener);
                }
            });
        });
    }
}