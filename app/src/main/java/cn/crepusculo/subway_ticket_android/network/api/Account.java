package cn.crepusculo.subway_ticket_android.network.api;

/**
 * The Account class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/7/13 14:16
 */
public class Account {

    private static final String PREFIX = "account/";

    public static final String REGISTER = "register";
    public static final String GET_CAPTCHA = "phone_captcha";
    public static final String LOGIN = "login";
    public static final String RESET_PASSWORD = "reset_password";
    public static final String MODIFY_PASSWORD = "modify_password";
    public static final String LOGOUT = "logout";

    public static String getApiFullName(String apiName) {
        return PREFIX + apiName;
    }
}
