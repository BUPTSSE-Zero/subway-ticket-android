package cn.crepusculo.subway_ticket_android.util;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.subwayticket.model.request.AddPreferRouteRequest;
import com.subwayticket.model.request.AddPreferStationRequest;
import com.subwayticket.model.request.LoginRequest;
import com.subwayticket.model.request.ModifyPasswordRequest;
import com.subwayticket.model.request.PayOrderRequest;
import com.subwayticket.model.request.PhoneCaptchaRequest;
import com.subwayticket.model.request.RefundOrderRequest;
import com.subwayticket.model.request.RegisterRequest;
import com.subwayticket.model.request.ResetPasswordRequest;
import com.subwayticket.model.request.SubmitOrderRequest;
import com.subwayticket.model.result.CityListResult;
import com.subwayticket.model.result.HistoryRouteListResult;
import com.subwayticket.model.result.MobileLoginResult;
import com.subwayticket.model.result.OrderInfoResult;
import com.subwayticket.model.result.OrderListResult;
import com.subwayticket.model.result.PayOrderResult;
import com.subwayticket.model.result.PreferRouteListResult;
import com.subwayticket.model.result.PreferStationListResult;
import com.subwayticket.model.result.RefundOrderResult;
import com.subwayticket.model.result.Result;
import com.subwayticket.model.result.SubmitOrderResult;
import com.subwayticket.model.result.SubwayLineListResult;
import com.subwayticket.model.result.SubwayStationListResult;
import com.subwayticket.model.result.TicketPriceResult;

import java.util.HashMap;
import java.util.Map;

import cn.crepusculo.subway_ticket_android.network.NetworkMethodWrapper;
import cn.crepusculo.subway_ticket_android.network.Url;
import cn.crepusculo.subway_ticket_android.network.api.Account;
import cn.crepusculo.subway_ticket_android.network.api.Preference;
import cn.crepusculo.subway_ticket_android.network.api.Subway;
import cn.crepusculo.subway_ticket_android.network.api.TicketOrder;

/**
 * The NetworkUtils class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/7/13 21:36
 */
public class NetworkUtils {

    public static Request accountRegister(RegisterRequest requestModel,
                                          Response.Listener<Result> listener,
                                          Response.ErrorListener errorListener) {

        String url = Url.getUrl(Account.getApiFullName(Account.REGISTER));
        return NetworkMethodWrapper.post(url, Result.class, requestModel, listener, errorListener);
    }


    public static Request accountGetCaptcha(PhoneCaptchaRequest requestModel,
                                            Response.Listener<Result> listener,
                                            Response.ErrorListener errorListener) {

        String url = Url.getUrl(Account.getApiFullName(Account.GET_CAPTCHA));
        return NetworkMethodWrapper.put(url, Result.class, requestModel, listener, errorListener);
    }

    public static Request accountCheckLogin(String authToken,
                                       Response.Listener<Result> listener,
                                       Response.ErrorListener errorListener) {
        String url = Url.getUrl(Account.getApiFullName(Account.CHECK_LOGIN));
        Map<String, String> header = generateHeaderByAuthToken(authToken);
        return NetworkMethodWrapper.getObject(url, Result.class, header, listener, errorListener);
    }

    public static Request accountLogin(LoginRequest request,
                                       Response.Listener<MobileLoginResult> listener,
                                       Response.ErrorListener errorListener) {
        String url = Url.getUrl(Account.getApiFullName(Account.LOGIN));
        return NetworkMethodWrapper.put(url, MobileLoginResult.class, request, listener, errorListener);
    }


    public static Request accountResetPassword(ResetPasswordRequest requestModel,
                                               Response.Listener<Result> listener,
                                               Response.ErrorListener errorListener) {
        String url = Url.getUrl(Account.getApiFullName(Account.RESET_PASSWORD));
        return NetworkMethodWrapper.put(url, Result.class, requestModel, listener, errorListener);
    }


    public static Request accountModifyPassword(ModifyPasswordRequest requestModel,
                                                String authToken,
                                                Response.Listener<Result> listener,
                                                Response.ErrorListener errorListener) {
        String url = Url.getUrl(Account.getApiFullName(Account.MODIFY_PASSWORD));
        Map<String, String> header = generateHeaderByAuthToken(authToken);

        return NetworkMethodWrapper.put(url, Result.class, requestModel, header, listener, errorListener);
    }


    public static Request accountLogout(String authToken,
                                        Response.Listener<Result> listener,
                                        Response.ErrorListener errorListener) {
        String url = Url.getUrl(Account.getApiFullName(Account.LOGOUT));
        Map<String, String> header = generateHeaderByAuthToken(authToken);

        return NetworkMethodWrapper.put(url, Result.class, null, header, listener, errorListener);
    }

    // FIXME: 16/7/15 跟后端商量,返回结果是 JSON 数组还是对象
    public static Request subwayGetCityList(Response.Listener<CityListResult> listener,
                                            Response.ErrorListener errorListener) {
        String url = Url.getUrl(Subway.getApiFullName(Subway.GET_CITY_LIST));
        return NetworkMethodWrapper.getObject(url, CityListResult.class, listener, errorListener);
    }


    // FIXME: 16/7/15 同上一个 FIXME
    public static Request subwayGetLineListByCity(String cityId,
                                                  Response.Listener<SubwayLineListResult> listener,
                                                  Response.ErrorListener errorListener) {
        String url = Url.getUrl(Subway.getApiFullName(Subway.GET_LINE), cityId);
        return NetworkMethodWrapper.getObject(url, SubwayLineListResult.class, listener, errorListener);
    }


    // FIXME: 16/7/15 同上一个 FIXME
    public static Request subwayGetStationByLine(String subwayLineId,
                                                 Response.Listener<SubwayStationListResult> listener,
                                                 Response.ErrorListener errorListener) {
        String url = Url.getUrl(Subway.getApiFullName(Subway.GET_STATION), subwayLineId);
        return NetworkMethodWrapper.getObject(url, SubwayStationListResult.class, listener, errorListener);
    }


    public static Request subwayGetTicketPriceByStartStationAndEndStation(
            String startStationId, String endStationId,
            Response.Listener<TicketPriceResult> listener, Response.ErrorListener errorListener
    ) {
        String url = Url.getUrl(
                Subway.getApiFullName(Subway.GET_TICKET_PRICE),
                startStationId, endStationId);

        return NetworkMethodWrapper.getObject(url, TicketPriceResult.class, listener, errorListener);
    }


    public static Request ticketOrderSubmit(SubmitOrderRequest requestModel,
                                            String authToken,
                                            Response.Listener<SubmitOrderResult> listener,
                                            Response.ErrorListener errorListener) {
        String url = Url.getUrl(TicketOrder.getApiFullName(TicketOrder.SUBMIT));
        Map<String, String> header = generateHeaderByAuthToken(authToken);

        return NetworkMethodWrapper.post(url, SubmitOrderResult.class,
                requestModel, header,
                listener, errorListener);
    }


    // FIXME: 16/7/15 与后端商量 DELETE 请求 Body 是否需要有内容
    public static Request ticketOrderCancelById(String orderId,
                                                String authToken,
                                                Response.Listener<Result> listener,
                                                Response.ErrorListener errorListener) {
        String url = Url.getUrl(TicketOrder.getApiFullName(TicketOrder.CANCEL), orderId);

        Map<String, String> header = generateHeaderByAuthToken(authToken);

        return NetworkMethodWrapper.delete(url, Result.class, null, header, listener, errorListener);
    }


    public static Request ticketOrderPay(PayOrderRequest requestModel,
                                         String authToken,
                                         Response.Listener<PayOrderResult> listener,
                                         Response.ErrorListener errorListener) {
        String url = Url.getUrl(TicketOrder.getApiFullName(TicketOrder.PAY));
        Map<String, String> header = generateHeaderByAuthToken(authToken);

        return NetworkMethodWrapper.put(url, PayOrderResult.class, requestModel, header,
                listener, errorListener);
    }


    public static Request ticketOrderRefund(RefundOrderRequest requestModel,
                                            String authToken,
                                            Response.Listener<RefundOrderResult> listener,
                                            Response.ErrorListener errorListener) {
        String url = Url.getUrl(TicketOrder.getApiFullName(TicketOrder.REFUND));
        Map<String, String> header = generateHeaderByAuthToken(authToken);

        return NetworkMethodWrapper.put(url, RefundOrderResult.class, requestModel, header,
                listener, errorListener);
    }


    public static Request ticketOrderGetOrderInfoById(String orderId,
                                                      String authToken,
                                                      Response.Listener<OrderInfoResult> listener,
                                                      Response.ErrorListener errorListener) {
        String url = Url.getUrl(TicketOrder.getApiFullName(TicketOrder.GET_ORDER_INFO), orderId);
        Map<String, String> header = generateHeaderByAuthToken(authToken);

        return NetworkMethodWrapper.getObject(url, OrderInfoResult.class, header, listener, errorListener);
    }


    // FIXME: 16/7/15 需要和后端确认传输的 JSON 是数组还是对象
    public static Request ticketOrderGetOrderListByStatusAndStartTimeAndEndTime(
            String status, String startTimeStamp, String endTimeStamp, String authToken,
            Response.Listener<OrderListResult> listener,
            Response.ErrorListener errorListener
    ) {
        String url = Url.getUrl(TicketOrder.getApiFullName(TicketOrder.GET_ORDER_LIST),
                status, startTimeStamp, endTimeStamp);
        Map<String, String> header = generateHeaderByAuthToken(authToken);
        return NetworkMethodWrapper.getObject(url, OrderListResult.class, header, listener, errorListener);
    }


    // FIXME: 16/7/15 同上一条 FIXME
    public static Request ticketOrderGetOrderListByStartTimeAndEndTime(
            String startTimeStamp, String endTimeStamp, String authToken,
            Response.Listener<OrderListResult> listener,
            Response.ErrorListener errorListener
    ) {
        String url = Url.getUrl(TicketOrder.getApiFullName(TicketOrder.GET_ORDER_LIST),
                startTimeStamp, endTimeStamp);

        Map<String, String> header = generateHeaderByAuthToken(authToken);

        return NetworkMethodWrapper.getObject(url, OrderListResult.class, header, listener, errorListener);
    }


    private static Map<String, String> generateHeaderByAuthToken(String authToken) {
        Map<String, String> header = new HashMap<>();
        header.put("AuthToken", authToken);

        return header;
    }


    public static Request preferenceHistory(
            String authToken,
            Response.Listener<HistoryRouteListResult> listener,
            Response.ErrorListener errorListener) {
        String url = Url.getUrl(Preference.getApiFullName(Preference.HISTORY_ROUTE));
        Map<String, String> header = generateHeaderByAuthToken(authToken);
        return NetworkMethodWrapper.getObject(url, HistoryRouteListResult.class, header, listener, errorListener);
    }

    public static Request preferencePreferStation(
            String authToken,
            Response.Listener<PreferStationListResult> listener,
            Response.ErrorListener errorListener) {
        String url = Url.getUrl(Preference.getApiFullName(Preference.PREFER_STATION));
        Map<String, String> header = generateHeaderByAuthToken(authToken);
        return NetworkMethodWrapper.getObject(url, PreferStationListResult.class, header, listener, errorListener);
    }

    public static Request preferenceAddPreferStation(
            AddPreferStationRequest addPreferStationRequest,
            String authToken,
            Response.Listener<Result> listener,
            Response.ErrorListener errorListener) {
        String url = Url.getUrl(Preference.getApiFullName(Preference.PREFER_STATION), Preference.ADD);
        Map<String, String> header = generateHeaderByAuthToken(authToken);
        return NetworkMethodWrapper.post(url, Result.class, addPreferStationRequest, header, listener, errorListener);
    }

    public static Request preferenceRemovePreferStation(
            int stationID,
            String authToken,
            Response.Listener<Result> listener,
            Response.ErrorListener errorListener) {
        String url = Url.getUrl(Preference.getApiFullName(Preference.PREFER_STATION), Preference.REMOVE, String.valueOf(stationID));
        Map<String, String> header = generateHeaderByAuthToken(authToken);
        return NetworkMethodWrapper.delete(url, Result.class, null, header, listener, errorListener);
    }

    public static Request preferencePrefeRoute(
            String authToken,
            Response.Listener<PreferRouteListResult> listener,
            Response.ErrorListener errorListener) {
        String url = Url.getUrl(Preference.getApiFullName(Preference.PREFER_ROUTE));
        Map<String, String> header = generateHeaderByAuthToken(authToken);
        return NetworkMethodWrapper.getObject(url, PreferRouteListResult.class, header, listener, errorListener);
    }

    public static Request preferenceAddPreferRoute(
            AddPreferRouteRequest addPreferRouteRequest,
            String authToken,
            Response.Listener<Result> listener,
            Response.ErrorListener errorListener) {
        String url = Url.getUrl(Preference.getApiFullName(Preference.PREFER_ROUTE), Preference.ADD);
        Map<String, String> header = generateHeaderByAuthToken(authToken);
        return NetworkMethodWrapper.post(url, Result.class, addPreferRouteRequest, header, listener, errorListener);
    }

    public static Request preferenceRemovePreferRoute(
            int startStationId, int endStationId,
            String authToken,
            Response.Listener<Result> listener,
            Response.ErrorListener errorListener) {
        String url = Url.getUrl(Preference.getApiFullName(Preference.PREFER_ROUTE), Preference.REMOVE,
                String.valueOf(startStationId), String.valueOf(endStationId));
        Map<String, String> header = generateHeaderByAuthToken(authToken);
        return NetworkMethodWrapper.delete(url, Result.class, null, header, listener, errorListener);
    }
}
