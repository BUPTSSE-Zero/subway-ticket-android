package cn.crepusculo.subway_ticket_android.network.api;

public class Preference {
    private static final String PREFIX = "preference/";

    public static final String HISTORY_ROUTE = "history_route";
    public static final String PREFER_STATION = "prefer_station";
    public static final String PREFER_ROUTE = "modify_password";
    public static final String ADD = "add";
    public static final String REMOVE = "remove";

    public static String getApiFullName(String apiName) {
        return PREFIX + apiName;
    }
}
