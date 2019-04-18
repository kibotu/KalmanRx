package net.kibotu.kalmanrx.app.misc


import android.hardware.SensorManager.*
import androidx.annotation.IntDef

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

@IntDef(SENSOR_DELAY_FASTEST, SENSOR_DELAY_GAME, SENSOR_DELAY_UI, SENSOR_DELAY_NORMAL)
@Retention(AnnotationRetention.SOURCE)
annotation class SensorDelay