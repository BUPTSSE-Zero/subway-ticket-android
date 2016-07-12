package cn.crepusculo.subway_ticket_android.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;

import cn.crepusculo.subway_ticket_android.R;


public class SubwayLineUtil {
    public final static int ONE = 1;
    public final static int TWO = 2;
    public final static int FOUR = 4;
    public final static int FIVE = 5;
    public final static int SIX = 6;
    public final static int SEVEN = 7;
    public final static int EIGHT = 8;
    public final static int NINE = 9;
    public final static int TEN = 10;
    public final static int THIRTEEN = 13;
    public final static int FOURTEEN = 14;
    public final static int FIFTEEN = 15;
    public final static int SIXTEEN = 16;
    public final static int BATONG = 17;
    public final static int CHANGPING = 18;
    public final static int DAXING = 19;
    public final static int FANGSHAN = 20;
    public final static int MENGOUTOU = 21;
    public final static int YANFANG = 22;
    public final static int YIZHUANG = 23;
    public final static int XIJIAO = 24;
    public final static int AIRPORT = 25;


    private SubwayLineUtil(){}

    public static int getColor(int line) {
        switch (line) {
            case ONE:
                return R.color.line_1;
            case TWO:
                return R.color.line_2;
            case FOUR:
                return R.color.line_4;
            case FIVE:
                return R.color.line_5;
            case SIX:
                return R.color.line_6;
            case SEVEN:
                return R.color.line_7;
            case EIGHT:
                return R.color.line_8;
            case NINE:
                return R.color.line_9;
            case TEN:
                return R.color.line_10;
            case THIRTEEN:
                return R.color.line_13;
            case FOURTEEN:
                return R.color.line_14;
            case FIFTEEN:
                return R.color.line_15;
            case SIXTEEN:
                return R.color.line_16;
            case BATONG:
                return R.color.line_ba;
            case CHANGPING:
                return R.color.line_chang;
            case DAXING:
                return R.color.line_da;
            case FANGSHAN:
                return R.color.line_fang;
            case MENGOUTOU:
                return R.color.line_men;
            case YANFANG:
                return R.color.line_yan;
            case YIZHUANG:
                return R.color.line_yi;
            case XIJIAO:
                return R.color.line_xi;
            case AIRPORT:
                return R.color.line_airport;
            default:
                return R.color.undefine;
        }
    }
}
