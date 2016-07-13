package cn.crepusculo.subway_ticket_android.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;

import cn.crepusculo.subway_ticket_android.application.MyApplication;

/**
 * The NetworkWrapper class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/7/13 11:40
 */
public class NetworkMethodWrapper {
    private static NetworkSingleton singleton = NetworkSingleton.getmInstance(MyApplication.getAppContext());

    private NetworkMethodWrapper() {
        // Required empty
    }

    /**
     * Make a POST request, post a json with the HTTP body, <br>
     * generated from the specified object using
     * {@link com.google.gson.Gson}
     * <br>
     * Use the anonymous class to implement the listener parameters
     * <br>
     * Notice that: the type of the generics is the <b>RESPONSE</b>
     * object
     *
     * @param url The request url
     * @param classType The response class
     * @param postObject The object ready to be post
     * @param listener The onSuccess listener
     * @param errorListener The onError listener
     * @param <T> Type of the <b>RESPONSE</b> object
     */
    public static <T> Request<T> post(String url,
                                          Class<T> classType,
                                          Object postObject,
                                          Response.Listener<T> listener,
                                          Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(
                url, classType, postObject, listener, errorListener
        );

        singleton.addToRequestQueue(request);

        return request;
    }

    public static <T> Request<T> getObject(String url,
                                     Class<T> classType,
                                     Response.Listener<T> listener,
                                     Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(url, classType, null,listener, errorListener);

        singleton.addToRequestQueue(request);

        return request;
    }

    public static <T> Request<T> getArray(String url,
                                          Class<T> classType,
                                          TypeToken<T> arrayTypeToken,
                                          Response.Listener<T> listener,
                                          Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(
                url, classType, arrayTypeToken, listener, errorListener);

        singleton.addToRequestQueue(request);

        return request;
    }

    public static <T> Request<T> put(String url,
                                     Class<T> classType,
                                     Object putObject,
                                     Response.Listener<T> listener,
                                     Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(
                Request.Method.PUT, url, classType, null, putObject, null, listener, errorListener);

        singleton.addToRequestQueue(request);

        return request;
    }

    public static <T> Request<T> delete(String url,
                                        Class<T> classType,
                                        Object deleteRequestObject,
                                        Response.Listener<T> listener,
                                        Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(
                Request.Method.DELETE, url, classType, null, deleteRequestObject, null,
                listener, errorListener);

        singleton.addToRequestQueue(request);

        return request;
    }

}
