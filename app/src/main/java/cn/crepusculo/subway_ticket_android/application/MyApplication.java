package cn.crepusculo.subway_ticket_android.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * The MyApplication class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/4/15 08:16
 */

public class MyApplication extends Application {
    private static Context context;
    private static Resources resources;

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static Resources getResource() {
        return MyApplication.resources;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        MyApplication.resources = getResources();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
