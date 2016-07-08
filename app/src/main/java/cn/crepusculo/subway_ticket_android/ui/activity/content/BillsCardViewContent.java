package cn.crepusculo.subway_ticket_android.ui.activity.content;

import java.util.Calendar;
import java.util.Date;

public class BillsCardViewContent {
    public String title_collapse;
    public String subtitle_collapse;
    public String status_collapse;
    public String date_collapse;

    public String title_expand_1;
    public String title_expand_2;
    public String sub_title_expand;
    public String status_expand;
    public String date_expand;

    public BillsCardViewContent(){
        title_collapse = null;
        subtitle_collapse =null;
        status_collapse = null;
        date_collapse = null;

        title_expand_1 = null;
        title_expand_2 = null;
        sub_title_expand = null;
        status_expand = null;
        date_expand = null;
    }

    public void ForTest(){
        title_collapse = "从 菊花村 到 太阳监狱";
        subtitle_collapse ="单程票 6元";
        status_collapse = "已发车";
        date_collapse = "12分钟前";

        title_expand_1 = "从 菊花村";
        title_expand_2 = "到 太阳监狱";
        sub_title_expand = "单程票 6元";
        status_expand = "已发车";
        date_expand = "12分钟前, 截止午夜有效";
    }
}
