package cn.crepusculo.subway_ticket_android.ui.activity.content;

import java.util.Calendar;
import java.util.Date;

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

    public void ForTest(){
        start = "菊花村西北口";
        start_line = 1;
        destination = "太阳监狱和我";
        destination_line = 5;
        date = new Date();
        status = TICKET_UNBOUNCE;
    }
}
