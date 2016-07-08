package cn.crepusculo.subway_ticket_android.ui.adapter;


/**
 * Created by airfr on 2016/7/8.
 * Deal with position and id interface do not correspond
 */
public class PositionTranslationAdapter {
    private PositionTranslationAdapter(){};
    public static int DrawerIdtoTablayoutId(int drawerId){
        return drawerId - 1;
    }
}
