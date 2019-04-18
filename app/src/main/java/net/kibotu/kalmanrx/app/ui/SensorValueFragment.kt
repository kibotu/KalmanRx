package net.kibotu.kalmanrx.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import net.kibotu.kalmanrx.app.R
import net.kibotu.kalmanrx.app.misc.SensorDelay
import net.kibotu.kalmanrx.app.misc.createSensorEventObservable
import rx.Subscription


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

abstract class SensorValueFragment : Fragment() {

    @LayoutRes
    open val layout = R.layout.sensor

    var sensorSubscription: Subscription? = null

    @SensorDelay
    abstract val sensorDelay: Int

    abstract val sensorType: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeSensor()
    }

    private fun subscribeSensor() {
        unsubscribe()
        sensorSubscription = createSensorSubscription()
    }

    protected open fun createSensorSubscription(): Subscription = createSensorEventObservable(sensorType, sensorDelay)
            .subscribe({ sensorEvent -> process(sensorEvent.values) }, Throwable::printStackTrace)

    abstract fun process(x: Float, y: Float, z: Float)

    fun process(values: FloatArray) = process(values[0], values[1], values[2])

    private fun unsubscribe() {
        if (sensorSubscription?.isUnsubscribed == false)
            sensorSubscription?.unsubscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unsubscribe()
    }
}