package cn.crepusculo.subway_ticket_android.Utils;

import android.net.Network;
import android.system.ErrnoException;

import com.android.volley.Request;
import com.android.volley.Response;
import com.subwayticket.model.request.LoginRequest;
import com.subwayticket.model.request.ModifyPasswordRequest;
import com.subwayticket.model.request.PhoneCaptchaRequest;
import com.subwayticket.model.request.RegisterRequest;
import com.subwayticket.model.request.ResetPasswordRequest;
import com.subwayticket.model.result.MobileLoginResult;
import com.subwayticket.model.result.Result;

import cn.crepusculo.subway_ticket_android.network.NetworkMethodWrapper;
import cn.crepusculo.subway_ticket_android.network.Url;
import cn.crepusculo.subway_ticket_android.network.api.Account;

/**
 * The NetworkUtils class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/7/13 21:36
 */
public class NetworkUtils {

    // TODO: 16/7/14 Need to fill api in.

    public static Request register(RegisterRequest requestModel,
                                   Response.Listener<Result> listener,
                                   Response.ErrorListener errorListener) {

        String url = Url.getUrl(Account.getApiFullName(Account.REGISTER));
        return NetworkMethodWrapper.post(url, Result.class, requestModel, listener, errorListener);
    }

    public static Request getCaptcha(PhoneCaptchaRequest requestModel,
                                     Response.Listener<Result> listener,
                                     Response.ErrorListener errorListener) {

        String url = Url.getUrl(Account.getApiFullName(Account.GET_CAPTCHA));
        return NetworkMethodWrapper.put(url, Result.class, requestModel, listener, errorListener);
    }

    public static Request login(LoginRequest request,
                                Response.Listener<MobileLoginResult> listener,
                                Response.ErrorListener errorListener) {
        String url = Url.getUrl(Account.getApiFullName(Account.LOGIN));
        return NetworkMethodWrapper.put(url, MobileLoginResult.class, request, listener, errorListener);
    }

    public static Request resetPassword(ResetPasswordRequest requestModel,
                                        Response.Listener<Result> listener,
                                        Response.ErrorListener errorListener) {
        String url = Url.getUrl(Account.getApiFullName(Account.RESET_PASSWORD));
        return NetworkMethodWrapper.put(url, Result.class, requestModel, listener, errorListener);
    }

    // TODO: 16/7/14 Need to add auth token
    public static Request modifyPassword(ModifyPasswordRequest requestModel,
                                         Response.Listener<Result> listener,
                                         Response.ErrorListener errorListener) {
        String url = Url.getUrl(Account.getApiFullName(Account.MODIFY_PASSWORD));
        return NetworkMethodWrapper.put(url, Result.class, requestModel, listener, errorListener);
    }
}
