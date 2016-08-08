package cn.crepusculo.subway_ticket_android.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.google.gson.Gson;
import com.subwayticket.database.model.TicketOrder;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.util.CalendarUtils;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by airfr on 2016/8/9.
 */

public class DisplayActivity extends AppCompatActivity {
    TicketOrder ticketOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        ticketOrder = new Gson().fromJson(bundle.getString(PayActivity.KEY_WORD), TicketOrder.class);

        View settingsView = new AboutPage(this)
                .isRTL(false)
                .setDescription(ticketOrder.getExtractCode() + '\n'
                        + ticketOrder.getTicketOrderId())
                .setImage(R.drawable.ic_qr)
                .addItem(getCustom(
                        ticketOrder.getStartStation().getSubwayStationName()
                                + "　　 <-> 　　"
                                + ticketOrder.getEndStation().getSubwayStationName()
                        ,
                        Gravity.CENTER
                ))
                .addGroup("购买时间")
                .addItem(getCustom(CalendarUtils.formatCurrentTimeMills(ticketOrder.getTicketOrderTime().getTime())))
                .addGroup("单价")
                .addItem(getCustom(String.valueOf(ticketOrder.getTicketPrice())))
                .addGroup("购买张数")
                .addItem(getCustom(String.valueOf(ticketOrder.getAmount()), R.drawable.ic_num_0_black_24dp))
                .addGroup("共计")
                .addItem(getCustom(String.valueOf(ticketOrder.getTicketPrice() * ticketOrder.getAmount())))
                .addGroup("票务状态")
                .addItem(getCustom(
                        cn.crepusculo.subway_ticket_android.content.TicketOrder.translationCode(
                                this,
                                ticketOrder.getStatus()
                        )))
                .create();
        /**
         * Build View Here
         */
        setContentView(settingsView);
        /**
         * Get action bar and set display HomeAsUp button
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    Element getCustom(String str, int res) {
        Element element = new Element();
        element.setTitle(str);
        element.setIcon(res);
        element.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        element.setGravity(Gravity.START);
        return element;
    }

    Element getCustom(String str) {
        Element element = new Element();
        element.setTitle(str);
        element.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        element.setGravity(Gravity.START);
        return element;
    }

    Element getCustom(String str, int gra, int useless) {
        Element element = new Element();
        element.setTitle(str);
        element.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        element.setGravity(gra);
        return element;
    }
}

//public class DisplayActivity extends BaseActivity {
//    private TicketOrder ticketOrder;
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_display;
//    }
//
//    @Override
//    protected void initView() {
//        bindLayout();
//        initActionbar();
//        initTicketOrder();
//    }
//
//    private void bindLayout(){
//
//    }
//
//    private void initActionbar() {
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(true);
//        }
//    }
//
//    private void initTicketOrder() {
//        Bundle b = getBundle();
//        if (!b.isEmpty())
//            ticketOrder = new Gson().fromJson(b.getString(PayActivity.KEY_WORD), TicketOrder.class);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return true;
//    }
//
//}
