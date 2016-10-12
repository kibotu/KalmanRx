package net.kibotu.kalmanrx.app.ui;

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

import net.kibotu.kalmanrx.app.R;
import net.kibotu.kalmanrx.app.misc.SensorDelay;
import net.kibotu.kalmanrx.app.misc.SensorEventObservableFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;

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

    private Unbinder unbinder;

    Subscription sensorSubscription;

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

    }

    @Override
    public void onResume() {
        super.onResume();
        subscribeSensor();
    }

    @Override
    public void onPause() {
        super.onPause();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (sensorSubscription != null && sensorSubscription.isUnsubscribed())
            sensorSubscription.unsubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected void subscribeSensor() {
        unsubscribe();
        sensorSubscription = createSensorSubscription();
    }

    protected Subscription createSensorSubscription() {
        return SensorEventObservableFactory
                .createSensorEventObservable(sensorType(), sensorDelay())
                .subscribe(sensorEvent -> process(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]), Throwable::printStackTrace);
    }

    protected void process(float[] values) {
        process(values[0], values[1], values[2]);
    }

    protected abstract void process(float x, float y, float z);

    @SensorDelay
    public abstract int sensorDelay();

    protected abstract int sensorType();

    @NonNull
    @Override
    public String tag() {
        return getClass().getSimpleName();
    }
}