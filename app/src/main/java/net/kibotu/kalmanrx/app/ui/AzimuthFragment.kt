package net.kibotu.kalmanrx.app.ui

import android.hardware.Sensor
import android.hardware.SensorManager
import com.exozet.android.core.utils.MathExtensions

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 *
 * https://www.deviantdev.com/journal/android-compass-azimuth-calculating
 */
class AzimuthFragment : GraphViewSensorFragment() {

    override val sensorDelay = SensorManager.SENSOR_DELAY_UI

    override val sensorType = Sensor.TYPE_ROTATION_VECTOR

    var rotationMatrix = FloatArray(9)

    var orientation = FloatArray(3)

    override fun process(values: FloatArray) {

        // calculate th rotation matrix
        SensorManager.getRotationMatrixFromVector(rotationMatrix, values)

        // get the azimuth value (orientation[0]) in degree
        val azimuth = ((SensorManager.getOrientation(rotationMatrix, orientation)[0] * MathExtensions.radiansToDegrees) + 360) % 360

        addToGraph(azimuth, 0f, 0f)
    }
}