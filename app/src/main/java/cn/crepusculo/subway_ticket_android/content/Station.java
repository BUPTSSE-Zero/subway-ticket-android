package cn.crepusculo.subway_ticket_android.content;

import com.subwayticket.database.model.StationMessage;
import com.subwayticket.database.model.SubwayLine;
import com.subwayticket.database.model.SubwayStation;

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

    public Station(String name, int line) {
        this.name = name;
        this.clientLine = line;
    }

    public Station(int i) {
        String[] ss = {"111五棵松", "121安定门", "181什刹海", "1131五道口", "1101安贞门"};
        String[] ds = {"地下城", "美容中心", "洗脚城", "宏福", "五棵松中心"};
        String[] sid = {"111","121","181","1131","1101"};
        clientLine = i % 23;
        serverLine = SubwayLineUtil.ToServerTypeId(clientLine);
        id = Integer.parseInt(sid[i%5]);
        available = true;
        stationMessage = null;
        subwayLine = null;

        name = ss[i % 5];

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
}
