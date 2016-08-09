package cn.crepusculo.subway_ticket_android.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.request.RefundOrderRequest;
import com.subwayticket.model.result.RefundOrderResult;
import com.subwayticket.model.result.Result;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.util.CalendarUtils;
import cn.crepusculo.subway_ticket_android.util.GsonUtils;
import cn.crepusculo.subway_ticket_android.util.NetworkUtils;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Display ticket message
 * <p/>
 * Created by airfr on 2016/8/9.
 */

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener {
    TicketOrder ticketOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        ticketOrder = new Gson().fromJson(bundle.getString(PayActivity.KEY_WORD), TicketOrder.class);
        Log.e("Display", ticketOrder.getStartStation().getSubwayStationName());
        Log.e("Display", ticketOrder.getEndStation().getSubwayStationName());
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
                        Gravity.CENTER, 2
                ))
                .addGroup("购买时间")
                .addItem(getCustom(CalendarUtils.formatCurrentTimeMills(
                        ticketOrder.getTicketOrderTime().getTime()
                        ), R.drawable.ic_time_black_24dp)
                )
                .addGroup("单价")
                .addItem(getCustom(String.valueOf(ticketOrder.getTicketPrice()), R.drawable.ic_cash_black_24dp))
                .addGroup("购买张数")
                .addItem(getCustom(String.valueOf(ticketOrder.getAmount()), R.drawable.ic_num_0_black_24dp))
                .addGroup("共计")
                .addItem(getCustom(String.valueOf(ticketOrder.getTicketPrice() * ticketOrder.getAmount()),
                        R.drawable.ic_cash_black_24dp))
                .addGroup("票务状态")
                .addItem(getCustom(
                        cn.crepusculo.subway_ticket_android.content.TicketOrder.translationCode(
                                this,
                                ticketOrder.getStatus()
                        )))
                .addItem(new Element().setTitle("退票").setIcon(R.drawable.btn_default).setOnClickListener(this))
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

    @Override
    public void onClick(final View view) {
        switch (ticketOrder.getStatus()) {
            case TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET:
                // Refund
                NetworkUtils.ticketOrderRefund(
                        new RefundOrderRequest(ticketOrder.getTicketOrderId()),
                        Info.getInstance().getToken(),
                        new Response.Listener<RefundOrderResult>() {
                            @Override
                            public void onResponse(RefundOrderResult response) {
                                Toast.makeText(
                                        getBaseContext(), response.getResultDescription() + "返回主界面", Toast.LENGTH_LONG)
                                        .show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1000);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                try {
                                    GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                                    Toast.makeText(
                                            getBaseContext(), r.result_description, Toast.LENGTH_LONG)
                                            .show();
                                    Log.e("SubmitFragment", "Error" + r.result_description);
                                } catch (NullPointerException e) {
                                    if (error != null) {
                                        Toast.makeText(
                                                getBaseContext(), error.getMessage(), Toast.LENGTH_LONG)
                                                .show();

                                    } else {
                                        Toast.makeText(
                                                getBaseContext(), "网络访问超时", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }

                            }
                        });
                break;
            case TicketOrder.ORDER_STATUS_NOT_PAY:
                // Cancel
                NetworkUtils.ticketOrderCancelById(
                        ticketOrder.getTicketOrderId(),
                        Info.getInstance().getToken(),
                        new Response.Listener<Result>() {
                            @Override
                            public void onResponse(Result response) {
                                Toast.makeText(
                                        getBaseContext(), response.getResultDescription() + "　返回主界面", Toast.LENGTH_LONG)
                                        .show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1000);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                try {
                                    GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                                    Toast.makeText(
                                            getBaseContext(), r.result_description, Toast.LENGTH_LONG)
                                            .show();
                                    Log.e("SubmitFragment", "Error" + r.result_description);
                                } catch (NullPointerException e) {
                                    if (error != null) {
                                        Toast.makeText(
                                                getBaseContext(), error.getMessage(), Toast.LENGTH_LONG)
                                                .show();

                                    } else {
                                        Toast.makeText(
                                                getBaseContext(), "网络访问超时", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }

                            }
                        });
                break;
            case TicketOrder.ORDER_STATUS_REFUNDED:
            case TicketOrder.ORDER_STATUS_FINISHED:
                // Keep empty
                break;
        }
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
