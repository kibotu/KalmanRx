package net.kibotu.kalmanrx.app.ui

import net.kibotu.kalmanrx.KalmanRx
import net.kibotu.kalmanrx.app.misc.createSensorEventObservable
import rx.Subscription

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class AccelerationSensorLowPassFragment : AccelerationSensorFragment() {

    override fun createSensorSubscription(): Subscription {
        return KalmanRx.createLowPassFilter(createSensorEventObservable(sensorType, sensorDelay)
                .map { it.values })
                .subscribe({ process(it) }, { it.printStackTrace() })
    }
}
