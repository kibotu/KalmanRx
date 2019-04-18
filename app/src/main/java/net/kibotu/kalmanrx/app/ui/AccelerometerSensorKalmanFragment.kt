package net.kibotu.kalmanrx.app.ui

import android.hardware.Sensor
import android.hardware.SensorManager
import net.kibotu.kalmanrx.KalmanRx

import rx.Subscription

import net.kibotu.kalmanrx.app.misc.createSensorEventObservable


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class AccelerometerSensorKalmanFragment : GraphViewSensorFragment() {

    override val sensorDelay = SensorManager.SENSOR_DELAY_UI

    override val sensorType = Sensor.TYPE_ACCELEROMETER

    override fun createSensorSubscription(): Subscription {

        // 1) float stream
        val floatStream = createSensorEventObservable(sensorType, sensorDelay)
                .map { e -> e.values }

        // 2) apply kalman filter
        val kalmanFilterStream = KalmanRx.createFrom3D(floatStream)

        // (optional) apply low pass filter
        val lowPassFilter = KalmanRx.createLowPassFilter(kalmanFilterStream)

        return lowPassFilter.subscribe({  process(it) },  { it.printStackTrace() })
    }
}