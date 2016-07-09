package cn.crepusculo.subway_ticket_android.ui.activity.content;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class BillsCardViewContent {

    public String start;
    public int start_line;
    public String destination;
    public int destination_line;

    public Date date;
    public Double prize;

    public String getStatus() {
        if(status==TICKET_UNBOUNCE){
            return "UNBOUNCE";
        }
        else if(status==TICKET_FINISHED){
            return "FINISHED";
        }
        else if(status==TICKET_UNPAID){
            return "UNPAID";
        }
        else if(status==TICKET_INVALID){
            return "INVALID";
        }
        else if(status==TICKET_REFUND){
            return "REFUND";
        }
        return "ERROR";
    }

    public int status;

    public static int TICKET_UNPAID = 0;
    public static int TICKET_UNBOUNCE = 1;
    public static int TICKET_FINISHED = 2;

    public static int TICKET_INVALID = -1;
    public static int TICKET_REFUND = -2;

    public BillsCardViewContent(){
        start = null;
        start_line = 0;
        destination = null;
        destination_line = 0;
        date = null;
        prize = 0.0;
        status = TICKET_UNPAID;
    }

    public void ForTest(int i){
        String[] ss = {"菊花村","傻逼监狱","智障关爱中心","百度全家桶","东村"};
        String[] ds = {"地下城与高数","美容中心","洗脚城","宏福","五棵松上吊中心"};
        start = ss[i%5];
        start_line = i%30;
        destination = ds[i*2%5];
        destination_line = i%30;

        date = new Date();
        status = TICKET_UNBOUNCE;
    }
}
