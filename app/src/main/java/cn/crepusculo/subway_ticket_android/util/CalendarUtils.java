package cn.crepusculo.subway_ticket_android.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalendarUtils {
    static public String format(Calendar c) {
        String result;
        result = c.get(Calendar.YEAR) + " / ";
        result += (c.get(Calendar.MONTH) + 1) + " / ";
        result += c.get(Calendar.DATE) + "   -   ";
        result += c.get(Calendar.HOUR) + " : ";
        result += c.get(Calendar.MINUTE);
        return result;
    }

    static public String format_limit(Calendar c) {
        String result;
        result = c.get(Calendar.YEAR) + " / ";
        result += (c.get(Calendar.MONTH) + 1) + " / ";
        result += c.get(Calendar.DATE) + "   -   ";
        c.set(Calendar.HOUR, 24);
//        result += c.get(Calendar.HOUR) + " : ";
        result += "24" + " : ";
        c.set(Calendar.MINUTE, 0);
//        result += c.get(Calendar.MINUTE);
        result += "00";
        result += "  前";

        return result;
    }

    static public String formatTimeMills(long mills) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//将毫秒级long值转换成日期格式
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(mills);
        return dateformat.format(gc.getTime());
    }

}
