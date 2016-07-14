package cn.crepusculo.subway_ticket_android.network;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

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
     * @param url           The request url
     * @param classType     The response class
     * @param postObject    The object ready to be post
     * @param listener      The onSuccess listener
     * @param errorListener The onError listener
     * @param <T>           Type of the <b>RESPONSE</b> object
     * @return The volley request of this method
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


    /**
     * Make a POST request with custom header
     * @see NetworkMethodWrapper#post(String, Class, Object, Response.Listener, Response.ErrorListener)
     * @param header The custom header
     */
    public static <T> Request<T> post(String url,
                                      Class<T> classType,
                                      Object postObject,
                                      Map<String, String> header,
                                      Response.Listener<T> listener,
                                      Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(
                url, classType, postObject, header, listener, errorListener);

        singleton.addToRequestQueue(request);

        return request;
    }


    /**
     * Make a GET request, get JSONObject from http, and parsing the json
     * to the specific model object using {@link com.google.gson.Gson}
     *
     * Use the anonymous class to implement the listener
     *
     * @param url The request url
     * @param classType The response class
     * @param listener The listener for {@link Response#success(Object, Cache.Entry)}
     * @param errorListener The listener for {@link Response#error(VolleyError)}
     * @param <T> The type of <b>RESPONSE</b>class
     * @return The volley request of this method
     */
    public static <T> Request<T> getObject(String url,
                                           Class<T> classType,
                                           Response.Listener<T> listener,
                                           Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(url, classType, null, listener, errorListener);

        singleton.addToRequestQueue(request);

        return request;
    }


    /**
     * Make a GET request with the custom header,
     * @see NetworkMethodWrapper#getObject(String, Class, Response.Listener, Response.ErrorListener)
     *
     * @param header The custom header
     */
    public static <T> Request<T> getObject(String url,
                                            Class<T> classType,
                                            Map<String, String> header,
                                            Response.Listener<T> listener,
                                            Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(
                url, classType, null, header, listener, errorListener);

        singleton.addToRequestQueue(request);

        return request;
    }


    /**
     * Make a GET request, get the JSONArray from http, and parsing the json to
     * the specific array using {@link com.google.gson.Gson}
     *
     * @param url The request url
     * @param classType The response class
     * @param arrayTypeToken The type token to describe the array
     * @param listener The listener for {@link Response#success(Object, Cache.Entry)}
     * @param errorListener The listener for {@link Response#error(VolleyError)}
     * @param <T> The type of response class
     * @return The volley request of this method
     */
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


    /**
     * Make a GET request to get JSONArray with a custom header
     * @see NetworkMethodWrapper#getArray(String, Class, TypeToken, Response.Listener, Response.ErrorListener)
     *
     * @param header The custom header
     */
    public static <T> Request<T> getArray(String url,
                                              Class<T> classType,
                                              TypeToken<T> arrayTypeToken,
                                              Map<String, String> header,
                                              Response.Listener<T> listener,
                                              Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(
                url, classType, arrayTypeToken, header, listener, errorListener);

        singleton.addToRequestQueue(request);

        return request;
    }


    /**
     * Make a PUT request, put a json object with the http body,
     * parsed by the specific object using {@link com.google.gson.Gson}
     *
     * @param url The request url
     * @param classType The response class
     * @param putObject The object to put
     * @param listener The listener for {@link Response#success(Object, Cache.Entry)}
     * @param errorListener The listener for {@link Response#error(VolleyError)}
     * @param <T> The type of response class
     * @return The volley request of this method
     */
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


    /**
     * Make a PUT request with the custom header
     *
     * @param header The specific header
     * @see NetworkMethodWrapper#put(String, Class, Object, Response.Listener, Response.ErrorListener)
     */
    public static <T> Request<T> put(String url,
                                     Class<T> classType,
                                     Object putObject,
                                     Map<String, String> header,
                                     Response.Listener<T> listener,
                                     Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(
                Request.Method.PUT, url, classType, null, putObject, header, listener, errorListener);

        singleton.addToRequestQueue(request);

        return request;
    }


    /**
     * Make a DELETE request, delete a object by http body,
     * parsed using {@link com.google.gson.Gson}
     *
     * @param url The request url
     * @param classType The response class
     * @param deleteRequestObject The object to be deleted
     * @param listener The listener for {@link Response#success(Object, Cache.Entry)}
     * @param errorListener The listener for {@link Response#error(VolleyError)}
     * @param <T> The type of response class
     * @return The request of this method
     */
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

    /**
     * Make a DELETE request with the custom header
     *
     * @param header The specific header
     * @see NetworkMethodWrapper#put(String, Class, Object, Response.Listener, Response.ErrorListener)
     */
    public static <T> Request<T> delete(String url,
                                        Class<T> classType,
                                        Object deleteRequestObject,
                                        Map<String, String> header,
                                        Response.Listener<T> listener,
                                        Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(
                Request.Method.DELETE, url, classType, null, deleteRequestObject, header,
                listener, errorListener);

        singleton.addToRequestQueue(request);

        return request;
    }

}
