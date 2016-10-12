package net.kibotu.kalmanrx.app.misc;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import rx.Observable;
import rx.android.MainThreadSubscription;

import static net.kibotu.android.deviceinfo.library.services.SystemService.getSensorManager;

/**
 * Allows to treat sensor events as Observable
 * <p>
 * Liberated: https://github.com/ArkadyGamza/ShakeDetector/tree/master/app/src/main/java/com/arkadygamza/shakedetector
 */
public class SensorEventObservableFactory {

    public static Observable<SensorEvent> createSensorEventObservable(int sensorType, @SensorDelay int sensorDelay) {
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

            final SensorManager sensorManager = getSensorManager();
            sensorManager.registerListener(listener, sensorManager.getDefaultSensor(sensorType), sensorDelay);

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