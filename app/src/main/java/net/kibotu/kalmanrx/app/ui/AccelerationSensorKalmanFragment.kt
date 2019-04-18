package net.kibotu.kalmanrx.app.ui

import net.kibotu.kalmanrx.KalmanRx

import rx.Observable
import rx.Subscription

import net.kibotu.kalmanrx.app.misc.createSensorEventObservable
import rx.functions.Action1


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class AccelerationSensorKalmanFragment : AccelerationSensorFragment() {

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