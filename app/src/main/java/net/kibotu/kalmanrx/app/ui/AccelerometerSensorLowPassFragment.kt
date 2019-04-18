package net.kibotu.kalmanrx.app.ui

import android.hardware.Sensor
import android.hardware.SensorManager
import net.kibotu.kalmanrx.KalmanRx
import net.kibotu.kalmanrx.app.misc.createSensorEventObservable

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class AccelerometerSensorLowPassFragment : GraphViewSensorFragment() {

    override val sensorDelay = SensorManager.SENSOR_DELAY_UI

    override val sensorType = Sensor.TYPE_ACCELEROMETER

    override fun createSensorSubscription() = KalmanRx.createLowPassFilter(createSensorEventObservable(sensorType, sensorDelay)
            .map { it.values })
            .subscribe({ process(it) }, { it.printStackTrace() })
}
