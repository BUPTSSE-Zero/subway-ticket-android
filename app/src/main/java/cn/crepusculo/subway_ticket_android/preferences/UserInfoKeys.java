package cn.crepusculo.subway_ticket_android.preferences;

/**
 * The UserInfoKeys class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/5/29 00:52
 */
public abstract class UserInfoKeys {

    // Register Info
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";

    // Personal Info
    public static final String IS_FIRST_LOGIN = "is_first_login";

    public static final String TICKET_COUNT = "ticket_count";

    private UserInfoKeys() {
        // Required empty
    }
}
