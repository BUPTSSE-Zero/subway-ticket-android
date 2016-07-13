package cn.crepusculo.subway_ticket_android.network;

/**
 * The Url class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/7/13 12:13
 */
public class Url {

    private static final String HOST_NAME = "101.200.144.204";
    private static final int PORT = 16080;

    private static final String MOBILE_API_PREFIX = "/subway-ticket-web/mobileapi/";
    private static final String API_VERSION = "v1/";

    private String getUrlPrefix() {
        return "http://" + HOST_NAME + ":" + String.valueOf(PORT) +
                MOBILE_API_PREFIX + API_VERSION;
    }

    /**
     * Get URL without parameters
     *
     * @param apiFullName The FULL NAME of API, which contains its category and its name.
     * @return The url
     * @throws IllegalArgumentException When the apiName is not a full name.
     */
    public String getUrl(String apiFullName) throws IllegalArgumentException {
        if (!apiFullName.contains("/")) {
            throw new IllegalArgumentException(apiFullName + "is not a full api name!\n" +
                    "It must contain its category.");
        }

        return getUrlPrefix() + apiFullName;
    }


    /**
     * Get URL with parameters.
     * It eventually looks like ../api_name/param1/param2
     *
     * @param apiFullName The FULL NAME of API, which contains its category and its name.
     * @param params      The string of params
     * @return The url
     */
    public String getUrl(String apiFullName, String... params) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String param
                : params) {
            stringBuilder
                    .append("/")
                    .append(param);
        }

        return getUrl(apiFullName) + stringBuilder.toString();
    }
}
