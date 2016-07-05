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
    public static final String GENDER = "gender";
    public static final String NICKNAME = "nickname";
    public static final String SCHOOL = "school";

    // Personal Info
    public static final String IS_SINGLE = "is_single";
    public static final String LOVER = "lover";
    public static final String IS_FIRST_LOGIN = "is_first_login";

    private UserInfoKeys() {
        // Required empty
    }
}
