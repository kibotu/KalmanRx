package net.kibotu.kalmanrx.app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import rx.subjects.PublishSubject;

import static br.com.zbra.androidlinq.Linq.stream;
import static net.kibotu.android.deviceinfo.library.services.SystemService.getSensorManager;

/**
 * Created by <a href="https://about.me/janrabe">Jan Rabe</a>.
 */

public enum SensorRx implements SensorEventListener {

    /**
     * Singleton.
     */
    instance;

    private final PublishSubject<float[]> subject = PublishSubject.create();

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        subject.onNext(sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // unused
    }

    // region sensor listener

    public static SensorRx registerSensorListener(int sensorType, @SensorDelay int delay) {
        SensorManager sensorManager = getSensorManager();
        Sensor sensor = stream(sensorManager.getSensorList(sensorType)).firstOrNull();
        if (sensor != null)
            sensorManager.registerListener(instance, sensor, delay);
        return instance;
    }

    public static SensorRx unregisterSensorListener() {
        getSensorManager().unregisterListener(instance);
        return instance;
    }

    // endregion

    public static PublishSubject<float[]> getObservable() {
        return instance.subject;
    }
}