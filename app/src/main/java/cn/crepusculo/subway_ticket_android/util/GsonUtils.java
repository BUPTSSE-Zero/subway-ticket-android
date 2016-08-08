package cn.crepusculo.subway_ticket_android.util;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public class GsonUtils {
    public static final String PROTOCOL_CHARSET = "utf-8";

    GsonUtils() {
    }

    /**
     * @param error Volley error
     * @return String result_description if error.networkResponse.date exist
     * OR there will be a NullPointerException
     */
    static public Response resolveErrorResponse(VolleyError error) {
        Response response;
        try {
            String json = new String(error.networkResponse.data, PROTOCOL_CHARSET);
            Log.e("Register", json);
            Gson gson = new Gson();
            response = gson.fromJson(json, Response.class);
        } catch (UnsupportedEncodingException e) {
            Log.e("Register", "Exception" + e);
            response = null;
        }
        return response;
    }

    public class Response {
        public long result_code;
        public String result_description;

        public Response() {
        }
    }
}
