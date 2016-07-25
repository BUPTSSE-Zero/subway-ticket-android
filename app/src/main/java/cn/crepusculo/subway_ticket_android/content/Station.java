package cn.crepusculo.subway_ticket_android.content;

public class Station {

    private String name;
    private int line = 0;

    public Station() {}

    public Station(String name, int line) {
        this.name = name;
        this.line = line;
    }

    public Station(int i) {
        String[] ss = {"李家庄", "宋家庄", "惠新西街南口", "百度全家桶", "东村"};
        String[] ds = {"地下城", "美容中心", "洗脚城", "宏福", "五棵松中心"};
        name = ss[i%5];
        line = i%23;
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
