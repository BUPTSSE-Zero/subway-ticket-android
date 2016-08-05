package cn.crepusculo.subway_ticket_android.content;

import android.content.Context;
import android.widget.ImageView;

import java.util.Calendar;

import cn.crepusculo.subway_ticket_android.utils.CalendarUtils;

public class BillsCardViewContent {

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
    public Station start;
    public Station end;
    public String date;
    public Float price;
    public int City;
    public int status;
    public BillsCardViewContent(){
        start = new Station();
        end = new Station();
        date = null;
        price = 0.0f;
        status = TICKET_UNPAID;
    }

    public static void setTagColor(Context context, ImageView s, int c1, ImageView d, int c2){
        s.setColorFilter(context.getResources().getColor(c1));
        d.setColorFilter(context.getResources().getColor(c2));
    }

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

    public void ForTest(int i){
        String[] ss = {"李家庄","宋家庄","惠新西街南口","百度全家桶","东村"};
        String[] ds = {"地下城","美容中心","洗脚城","宏福","五棵松中心"};

        start.setName(ss[i%5]);
        start.setLine(i%30);
        end.setName(ss[i%3]);
        end.setLine(i%25);

        date = CalendarUtils.format(Calendar.getInstance());
        status = TICKET_UNBOUNCE;
    }
}
