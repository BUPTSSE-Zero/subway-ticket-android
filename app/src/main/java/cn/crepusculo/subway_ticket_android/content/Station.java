package cn.crepusculo.subway_ticket_android.content;

/**
 * Created by airfr on 2016/7/16.
 */
public class Station {

    private String name;
    private int line;

    public  Station(String name,int line){
        this.name = name;
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public Station setLine(int line) {
        this.line = line;
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
