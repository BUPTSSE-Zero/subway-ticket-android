package cn.crepusculo.subway_ticket_android.utils;

import com.subwayticket.database.model.Account;
import com.subwayticket.database.model.City;
import com.subwayticket.database.model.HistoryRoute;
import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.database.model.SubwayLine;
import com.subwayticket.database.model.SubwayStation;

import java.util.Date;

import cn.crepusculo.subway_ticket_android.content.TicketOrder;

/**
 * Created by airfr on 2016/8/6.
 */
public class TestUtils {
    static public int[] lids = {1, 2, 8, 10, 13};
    static public String[] lnames = {"一号线", "二号线", "八号线", "十号线", "十三号线"};
    static public String[] snames = {"五棵松", "安定门", "什刹海", "安贞门", "五道口"};
    static public int[] sids = {111, 121, 181, 1101, 1131};

    TestUtils() {
    }

    public static SubwayStation BuildSubwayStation(int i) {
        i = i % 5;
        SubwayStation result = new SubwayStation();

        result.setSubwayLine(BuildSubwayLine(i));

        result.setSubwayStationId(sids[i]);
        result.setSubwayStationName(snames[i]);
        result.setSubwayStationEnglishName(snames[i]);


        return result;
    }

    public static SubwayLine BuildSubwayLine(int i) {
        i = i % 5;

        SubwayLine result = new SubwayLine();
        result.setCity(new City(1));

        result.setSubwayLineId(lids[i]);
        result.setSubwayLineName(lnames[i]);


        return result;
    }

    public static PreferRoute BuildPreferRoute(int i, int j) {
        i = i % 5;
        j = j % 5;
        return new PreferRoute(
                "99999999999",
                BuildSubwayStation(i).getSubwayStationId(),
                BuildSubwayStation(j).getSubwayStationId());
    }

    public static HistoryRoute BuildHistoryRoute(int i, int j) {
        i = i % 5;
        j = j % 5;
        return new HistoryRoute(
                "999999999",
                BuildSubwayStation(i).getSubwayStationId(),
                BuildSubwayStation(j).getSubwayStationId());
    }

    public static TicketOrder BuildTicketOrder(int i, int j, char status) {
        i = i % 5;
        j = j % 5;
        TicketOrder result = new TicketOrder();
        result.setTicketOrderId("233");
        result.setTicketOrderTime(new Date(2016, 7, 24));
        result.setUser(new Account("99999999999"));
        result.setEndStation(BuildSubwayStation(j));
        result.setStartStation(BuildSubwayStation(i));
        result.setTicketPrice((i + j) / 2);
        result.setAmount(65);

        result.setStatus(status);
        return result;
    }
}
