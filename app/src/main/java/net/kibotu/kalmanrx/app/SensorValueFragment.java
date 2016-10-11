package net.kibotu.kalmanrx.app;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.android.utils.interfaces.LayoutProvider;
import com.common.android.utils.interfaces.LogTag;
import com.jjoe64.graphview.GraphView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static net.kibotu.android.deviceinfo.library.services.SystemService.getSensorManager;
import static net.kibotu.kalmanrx.app.R.layout.sensor;

/**
 * Created by jan.rabe on 11/10/16.
 */

public abstract class SensorValueFragment extends Fragment implements LayoutProvider, LogTag {

    @NonNull
    @BindView(R.id.x)
    TextView xLabel;
    @NonNull
    @BindView(R.id.y)
    TextView yLabel;
    @NonNull
    @BindView(R.id.z)
    TextView zLabel;

    @NonNull
    @BindView(R.id.graph)
    GraphView graphView;

    protected SensorManager sensorManager;
    protected SensorEventListener sensorEventListener;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public int getLayout() {
        return sensor;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sensorEventListener = createSensorEventListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerSensor();
    }

    @Override
    public void onPause() {
        unregisterSensor();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected void registerSensor() {
        sensorManager = getSensorManager();
        // SENSOR_DELAY_NORMAL, SENSOR_DELAY_UI, SENSOR_DELAY_GAME, or SENSOR_DELAY_FASTEST
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(sensorType()), sensorDelay());
    }

    @SensorDelay
    public abstract int sensorDelay();

    protected void unregisterSensor() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    protected abstract SensorEventListener createSensorEventListener();

    protected abstract int sensorType();

    @NonNull
    @Override
    public String tag() {
        return getClass().getSimpleName();
    }
}