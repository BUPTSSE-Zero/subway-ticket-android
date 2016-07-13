package cn.crepusculo.subway_ticket_android.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * The GsonRequest class.
 * <br/>
 * It is the subclass of {@link com.android.volley.Request}
 * and also generics.
 * <br/>
 * It use {@link com.google.gson.Gson} to parse and convert the object to json
 * and perform the HTTP request.
 *
 * @author wafer
 * @since 16/4/16 06:57
 */
public class GsonRequest<T> extends Request<T> {

    protected final String PROTOCOL_CHARSET = "utf-8";
    protected final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private static final String POST_PREFIX = "data=";

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private final Class<T> classType;
    private final TypeToken<T> typeToken;

    private final Response.Listener<T> listener;
    private final Object postObject;
    private Map<String, String> headers;


    private void checkNullPointer(Class<T> classType, TypeToken<T> typeToken) {
        if (classType == null && typeToken == null) {
            throw new NullPointerException("The classType and typeToken cannot be both null!!");
        }
    }


    /**
     * Make a GET request and return a parsed object
     * <br/>
     * from JSON by {@link com.google.gson.Gson}
     * <br/>
     *
     * @param url           The GET request url
     * @param classType     The classType of response object
     * @param typeToken     The specified type token, when the
     *                      response json is a <b>JSONArray</b>,
     *                      use it
     * @param listener      The response listener for
     *                      {@link com.android.volley.Response#success(Object, Cache.Entry)}
     *                      callback
     * @param errorListener The error listener for
     *                      {@link com.android.volley.Response#error(VolleyError)}
     *                      callback
     */
    public GsonRequest(@NonNull String url,
                       @Nullable Class<T> classType,
                       @Nullable TypeToken<T> typeToken,
                       @NonNull Response.Listener<T> listener,
                       @NonNull Response.ErrorListener errorListener) {

        super(Method.GET, url, errorListener);

        checkNullPointer(classType, typeToken);

        this.classType = classType;
        this.typeToken = typeToken;
        this.listener = listener;

        this.postObject = null;
        this.headers = null;
    }


    /**
     * Make a POST request and return a parsed object from JSON by
     * {@link com.google.gson.Gson}
     *
     * @param url           The request url
     * @param classType     The class type of the response object
     * @param postObject    The object ready to be post, <b>NONNULL</b>
     * @param listener      The response listener for
     *                      {@link com.android.volley.Response#success(Object, Cache.Entry)}
     *                      callback
     * @param errorListener The error listener for
     *                      {@link com.android.volley.Response#error(VolleyError)}
     *                      callback
     */
    public GsonRequest(@NonNull String url,
                       @NonNull Class<T> classType,
                       @NonNull Object postObject,
                       @NonNull Response.Listener<T> listener,
                       @NonNull Response.ErrorListener errorListener) {

        super(Method.POST, url, errorListener);

        this.classType = classType;
        this.typeToken = null;

        this.postObject = postObject;
        this.headers = null;

        this.listener = listener;
    }


    /**
     * The most customizable Request constructor
     *
     * @param method        The specified HTTP method
     * @param url           The request url
     * @param classType     The classType for response
     * @param postObject    The object ready to be post,
     *                      which will be encode into <b>HTTP body</b>
     * @param headers       The HTTP headers
     * @param listener      The response listener for
     *                      {@link com.android.volley.Response#success(Object, Cache.Entry)}
     *                      callback
     * @param errorListener The error listener for
     *                      {@link com.android.volley.Response#error(VolleyError)}
     *                      callback
     */
    public GsonRequest(int method,
                       @NonNull String url,
                       @Nullable Class<T> classType,
                       @Nullable TypeToken<T> typeToken,
                       @Nullable Object postObject,
                       @Nullable Map<String, String> headers,
                       @NonNull Response.Listener<T> listener,
                       @NonNull Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        checkNullPointer(classType, typeToken);

        this.classType = classType;
        this.typeToken = typeToken;

        this.postObject = postObject;
        this.headers = headers;

        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            String postJson = gson.toJson(postObject);
            return postObject == null ? null : postJson.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException e) {
            VolleyLog.wtf("Unsupported Encoding while trying get bytes of %s using %s",
                    postObject, PROTOCOL_CHARSET);

            return null;
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            if (this.typeToken == null) {
                // The response json is a object
                return Response.success(
                        gson.fromJson(json, classType),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                // The response json is a array
                return (Response<T>) Response.success(
                        gson.fromJson(json, this.typeToken.getType()),
                        HttpHeaderParser.parseCacheHeaders(response)
                );
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }


    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
