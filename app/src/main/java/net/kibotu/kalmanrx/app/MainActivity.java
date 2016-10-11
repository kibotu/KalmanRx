package net.kibotu.kalmanrx.app;

import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.common.android.utils.logging.Logger;

import net.kibotu.android.deviceinfo.library.Device;

import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.common.android.utils.extensions.FragmentExtensions.replace;
import static com.common.android.utils.extensions.FragmentExtensions.setFragmentContainerId;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Sensor> sensorList = Device.getSensorList();
        for (Sensor sensor : sensorList)
            Logger.v(TAG, "Sensor: " + sensor.getName() + " [" + sensor.getType() + "]");

        Sensor accelerometer = stream(sensorList).firstOrNull(sensor
                -> sensor.getType() == android.hardware.Sensor.TYPE_ACCELEROMETER);

        if (accelerometer == null)
            return;

        replace(new AccelerationSensorRawFragment());

        setFragmentContainerId(R.id.fragment_container2);
        replace(new AccelerationSensorLowPassFragment());

        setFragmentContainerId(R.id.fragment_container3);
        replace(new AccelerationSensorKalmanFragment());
    }
}
