package cn.crepusculo.subway_ticket_android.content;

import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.database.model.StationMessage;
import com.subwayticket.database.model.SubwayLine;
import com.subwayticket.database.model.SubwayStation;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;

/**
 * The Station class
 * It is client class match to SubwayStation
 */

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
        String[] ss = {"五棵松", "安定门", "什刹海", "五道口", "安贞门"};
        String[] sid = {"111", "121", "181", "1131", "1101"};
        clientLine = i % 23;
        serverLine = SubwayLineUtil.ToServerTypeId(clientLine);
        id = Integer.parseInt(sid[i % 5]);
        available = true;
        stationMessage = null;
        subwayLine = null;

        name = ss[i % 5];

    }

    /**
     * Convert server std data container to client mode
     *
     * @param subwayStation Server result
     * @return Client mode result
     */
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

    /**
     * Convert server std data container from PreferRoute class to client mode
     *
     * @param preferRoute A class contain two SubwayStation
     * @return ArrayList\<Station\>
     *              size = 2
     *              ArrayList.get(0) start Station
     *              ArrayList.get(1) end Station
     */
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

    /**
     * Accessor of Station class
     *
     */

    public StationMessage getStationMessage() {
        return stationMessage;
    }

    public Station setStationMessage(StationMessage stationMessage) {
        this.stationMessage = stationMessage;
        return this;
    }

    public int getServerLine() {
        return serverLine;
    }

    public Station setServerLine(int serverLine) {
        this.serverLine = serverLine;
        return this;
    }

    public boolean isAvailable() {
        return available;
    }

    public Station setAvailable(boolean available) {
        this.available = available;
        return this;
    }

    public int getId() {
        return id;
    }

    public Station setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * getLine auto return clientLine
     */
    public int getLine() {
        return clientLine;
    }

    public Station setClientLine(int line) {
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
