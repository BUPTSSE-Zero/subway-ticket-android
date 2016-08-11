package cn.crepusculo.subway_ticket_android.network.api;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The TicketOrder class
 * Please put more info here.
 *
 * @author wafer
 * @since 16/7/13 14:58
 */
public class TicketOrder {

    private static final String PREFIX = "ticket_order/";

    public static final String SUBMIT= "submit";
    public static final String CANCEL = "cancel";
    public static final String PAY = "pay";
    public static final String REFUND = "refund";
    public static final String GET_ORDER_INFO = "order_info/by_orderid";
    public static final String GET_ORDER_LIST = "order_list";

    public static String getApiFullName(String apiName) {
        return PREFIX + apiName;
    }
}
