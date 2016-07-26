package cn.crepusculo.subway_ticket_android.network.api;

/**
 * The Subway class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/7/13 14:29
 */
public class Subway {

    private static final String PREFIX = "subway/";

    public static final String GET_CITY_LIST = "city";
    public static final String GET_LINE= "line";
    public static final String GET_STATION= "station";
    public static final String GET_TICKET_PRICE = "ticket_price";

    public static String getApiFullName(String apiName) {
        return PREFIX + apiName;
    }
}
