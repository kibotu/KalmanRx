package net.kibotu.kalmanrx.app.ui

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import com.exozet.android.core.extensions.resColor
import com.exozet.android.core.utils.MathExtensions.radiansToDegrees
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.sensor.*
import net.kibotu.kalmanrx.app.R


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 *
 * [Using the Accelerometer](http://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-accel)
 */

open class AccelerationSensorFragment : SensorValueFragment() {

    override val sensorDelay: Int = SensorManager.SENSOR_DELAY_UI

    override val sensorType: Int = Sensor.TYPE_ACCELEROMETER

    lateinit var xSeries: LineGraphSeries<DataPoint>

    lateinit var ySeries: LineGraphSeries<DataPoint>

    lateinit var zSeries: LineGraphSeries<DataPoint>

    var graph2LastXValue: Double = 0.0

    private val maxDataPoints = 1000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        xSeries = LineGraphSeries()
        xSeries.color = R.color.red.resColor
        ySeries = LineGraphSeries()
        ySeries.color = R.color.green.resColor
        zSeries = LineGraphSeries()
        zSeries.color = R.color.blue.resColor
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(maxDataPoints.toDouble())
        graph.addSeries(xSeries)
        graph.addSeries(ySeries)
        graph.addSeries(zSeries)
    }

    override fun process(x: Float, y: Float, z: Float) {
        addToGraph(x * radiansToDegrees, y * radiansToDegrees, z * radiansToDegrees)
    }

    private fun addToGraph(x: Float, y: Float, z: Float) {
        xLabel.text = String.format("%.2f", x)
        yLabel.text = String.format("%.2f", y)
        zLabel.text = String.format("%.2f", z)
        graph2LastXValue += 1
        xSeries.appendData(DataPoint(graph2LastXValue, x.toDouble()), true, maxDataPoints)
        ySeries.appendData(DataPoint(graph2LastXValue, y.toDouble()), true, maxDataPoints)
        zSeries.appendData(DataPoint(graph2LastXValue, z.toDouble()), true, maxDataPoints)
    }
}