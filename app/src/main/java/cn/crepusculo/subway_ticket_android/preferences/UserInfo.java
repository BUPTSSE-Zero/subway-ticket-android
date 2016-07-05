package cn.crepusculo.subway_ticket_android.preferences;

import android.content.SharedPreferences;


/**
 * The UserInfo class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/5/29 02:02
 */
public class UserInfo {

    private static UserInfo mInstance = null;
    private static final String filename = "user_info";
    private SharedPreferences preferences = SharedPreferencesUtils.getPreferences(filename);

    private String phoneNumber;
    private String password;

    private boolean isFirstLogin;

    private boolean holdTicket;

    private UserInfo() {
        phoneNumber = preferences.getString(UserInfoKeys.PHONE, null);
        password = preferences.getString(UserInfoKeys.PASSWORD, null);
        isFirstLogin = preferences.getBoolean(UserInfoKeys.IS_FIRST_LOGIN, true);

    }

    public static UserInfo getInstance() {
        if (mInstance == null) {
            mInstance = new UserInfo();
        }

        return mInstance;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        SharedPreferencesUtils.putString(preferences, UserInfoKeys.PHONE, phoneNumber);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        SharedPreferencesUtils.putString(preferences, UserInfoKeys.PASSWORD, password);
    }


    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
        SharedPreferencesUtils.putBoolean(preferences, UserInfoKeys.IS_FIRST_LOGIN, firstLogin);
    }


}
