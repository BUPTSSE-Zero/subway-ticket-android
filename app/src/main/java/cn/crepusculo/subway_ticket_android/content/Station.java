package cn.crepusculo.subway_ticket_android.content;

import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.database.model.StationMessage;
import com.subwayticket.database.model.SubwayLine;
import com.subwayticket.database.model.SubwayStation;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;

public class Station {
    private int clientLine = 0;
    private int serverLine = 0;
    private SubwayLine subwayLine;
    private int id;
    private String name;
    private boolean available;
    private StationMessage stationMessage;
    public Station() {
    }
    public Station(String name, int line, int id) {
        this.available = true;
        this.name = name;
        this.clientLine = line;
        this.id = id;
    }

    public Station(int i) {
        String[] ss = {"111五棵松", "121安定门", "181什刹海", "1131五道口", "1101安贞门"};
        String[] sid = {"111", "121", "181", "1131", "1101"};
        clientLine = i % 23;
        serverLine = SubwayLineUtil.ToServerTypeId(clientLine);
        id = Integer.parseInt(sid[i % 5]);
        available = true;
        stationMessage = null;
        subwayLine = null;

        name = ss[i % 5];

    }

    public static Station SubwayStationAdapter(SubwayStation subwayStation) {
        Station s = new Station();
        s.clientLine = SubwayLineUtil.ToClientTypeId(subwayStation.getSubwayLine().getSubwayLineId());
        s.serverLine = subwayStation.getSubwayLine().getSubwayLineId();
        s.subwayLine = subwayStation.getSubwayLine();
        s.name = subwayStation.getSubwayStationName();
        s.stationMessage = subwayStation.getStationMessage();
        s.available = subwayStation.isAvailable();
        s.id = subwayStation.getSubwayStationId();
        return s;
    }

    public static List<Station> PreferRouteAdapter(PreferRoute preferRoute) {
        List<Station> result = new ArrayList<>();
        Station start = new Station(
                preferRoute.getStartStation().getSubwayStationName(),
                preferRoute.getStartStation().getSubwayLine().getSubwayLineId(),
                preferRoute.getStartStationId());
        Station end = new Station(
                preferRoute.getEndStation().getSubwayStationName(),
                preferRoute.getEndStation().getSubwayLine().getSubwayLineId(),
                preferRoute.getEndStationId());
        result.add(start);
        result.add(end);
        return result;
    }

    public int getId() {
        return id;
    }

    public Station setId(int id) {
        this.id = id;
        return this;
    }

    public int getLine() {
        return clientLine;
    }

    public Station setLine(int line) {
        this.clientLine = line;
        return this;
    }

    public String getName() {
        return name;
    }

    public Station setName(String name) {
        this.name = name;
        return this;
    }
}
