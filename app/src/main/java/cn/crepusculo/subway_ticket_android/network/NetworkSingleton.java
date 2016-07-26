package cn.crepusculo.subway_ticket_android.network;

import android.content.Context;
import android.nfc.Tag;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * The NetworkSingleton class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/7/13 11:09
 */
public class NetworkSingleton {

    private static NetworkSingleton mInstance = null;
    private static Context mContext;

    private RequestQueue requestQueue;

    private NetworkSingleton(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();
    }


    public RequestQueue getRequestQueue() {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }

        return requestQueue;
    }

    public static NetworkSingleton getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkSingleton(context);
        }

        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public <T> void cancelRequestByTag(Object tag) {
        getRequestQueue().cancelAll(tag);
    }

    public <T> void cancelRequestByFilter(RequestQueue.RequestFilter filter) {
        getRequestQueue().cancelAll(filter);
    }
}
