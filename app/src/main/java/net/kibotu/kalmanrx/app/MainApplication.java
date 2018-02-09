package net.kibotu.kalmanrx.app;

import android.support.multidex.MultiDexApplication;

import com.common.android.utils.ContextHelper;
import com.common.android.utils.logging.Logger;

import net.kibotu.android.deviceinfo.library.Device;

/**
 * Created by jan.rabe on 11/10/16.
 */

public class MainApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Device.with(this);
        ContextHelper.with(this);
        Logger.setLogLevel(BuildConfig.DEBUG ? Logger.Level.VERBOSE : Logger.Level.SILENT);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Device.onTerminate();
        ContextHelper.onTerminate();
    }
}
