package net.kibotu.kalmanrx.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exozet.android.core.utils.FragmentExtensions.replace
import com.exozet.android.core.utils.FragmentExtensions.setFragmentContainerId
import net.kibotu.android.deviceinfo.library.Device
import net.kibotu.kalmanrx.app.ui.AccelerometerSensorKalmanFragment
import net.kibotu.kalmanrx.app.ui.AccelerometerSensorLowPassFragment
import net.kibotu.kalmanrx.app.ui.AzimuthFragment
import net.kibotu.logger.LogcatLogger
import net.kibotu.logger.Logger
import net.kibotu.logger.Logger.logv


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Logger.addLogger(LogcatLogger())

        val sensorList = Device.getSensorList()
        for (sensor in sensorList)
            logv("Sensor: ${sensor.name} [${sensor.type}]")

        // check if we have accelerometer
        val accelerometer = sensorList.firstOrNull { it.type == android.hardware.Sensor.TYPE_ACCELEROMETER } ?: return

        replace(AzimuthFragment())

        setFragmentContainerId(R.id.fragment_container2)
        replace(AccelerometerSensorLowPassFragment())

        setFragmentContainerId(R.id.fragment_container3)
        replace(AccelerometerSensorKalmanFragment())
    }
}
