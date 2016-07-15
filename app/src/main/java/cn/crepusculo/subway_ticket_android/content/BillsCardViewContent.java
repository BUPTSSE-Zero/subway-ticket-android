package cn.crepusculo.subway_ticket_android.content;

import android.content.Context;
import android.widget.ImageView;

import java.util.Date;

public class BillsCardViewContent {

    public String start;
    public int start_line;
    public String destination;
    public int destination_line;

    public String date;
    public Double price;

    public int City;

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

    public static int CITY_BEIJING = 0;
    public static int CITY_SHANGHAI = 0;
    public static int CITY_GUANGZHOU = 0;
    public static int CITY_HANGZHOU = 0;
    public static int CITY_CHENGDU = 0;
    public static int CITY_XIAN = 0;

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
        price = 0.0;
        status = TICKET_UNPAID;
    }

    public void ForTest(int i){
        String[] ss = {"李家庄","宋家庄","惠新西街南口","百度全家桶","东村"};
        String[] ds = {"地下城","美容中心","洗脚城","宏福","五棵松中心"};
        start = ss[i%5];
        start_line = i%30;
        destination = ds[i*2%5];
        destination_line = i%30;

        date = new Date().toString();
        status = TICKET_UNBOUNCE;
    }

    public static void setTagColor(Context context, ImageView s, int c1, ImageView d, int c2){
        s.setColorFilter(context.getResources().getColor(c1));
        d.setColorFilter(context.getResources().getColor(c2));
    }
}
