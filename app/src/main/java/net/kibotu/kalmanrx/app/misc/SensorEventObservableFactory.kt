package net.kibotu.kalmanrx.app.misc

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import net.kibotu.android.deviceinfo.library.services.SystemService.getSensorManager
import rx.Observable
import rx.android.MainThreadSubscription


/**
 * Allows to treat sensor events as Observable
 *
 * Liberated: https://github.com/ArkadyGamza/ShakeDetector/tree/master/app/src/main/java/com/arkadygamza/shakedetector
 */

fun createSensorEventObservable(sensorType: Int, @SensorDelay sensorDelay: Int): Observable<SensorEvent> = Observable.create { subscriber ->

    MainThreadSubscription.verifyMainThread()

    val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (subscriber.isUnsubscribed) {
                return
            }

            subscriber.onNext(event)
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // NO-OP
        }
    }

    val sensorManager = getSensorManager()
    sensorManager.registerListener(listener, sensorManager.getDefaultSensor(sensorType), sensorDelay)

    // unregister listener in main thread when being unsubscribed
    subscriber.add(object : MainThreadSubscription() {
        override fun onUnsubscribe() {
            sensorManager.unregisterListener(listener)
        }
    })
}