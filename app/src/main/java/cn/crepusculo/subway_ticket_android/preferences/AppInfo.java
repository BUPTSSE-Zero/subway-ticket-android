package cn.crepusculo.subway_ticket_android.preferences;

import android.content.SharedPreferences;

import cn.crepusculo.subway_ticket_android.Utils.SharedPreferencesUtils;

/**
 * The AppInfo class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/6/5 01:25
 */
public class AppInfo {
    private static AppInfo mInstance = null;
    private static final String filename = "app_info";
    private SharedPreferences preferences = SharedPreferencesUtils.getPreferences(filename);

    private boolean isAppFirstBoot;

    private AppInfo() {
        isAppFirstBoot = preferences.getBoolean(AppInfoKeys.IS_APP_FIRST_BOOT, true);
    }

    public static AppInfo getInstance() {
        if (mInstance == null) {
            mInstance = new AppInfo();
        }

        return mInstance;
    }

    public boolean isAppFirstBoot() {
        return isAppFirstBoot;
    }

    public void setAppFirstBoot(boolean appFirstBoot) {
        isAppFirstBoot = appFirstBoot;
        SharedPreferencesUtils.putBoolean(preferences, AppInfoKeys.IS_APP_FIRST_BOOT, appFirstBoot);
    }
}
