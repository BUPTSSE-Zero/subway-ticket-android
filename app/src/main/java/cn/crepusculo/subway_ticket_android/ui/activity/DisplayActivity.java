package cn.crepusculo.subway_ticket_android.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.request.PayOrderRequest;
import com.subwayticket.model.request.RefundOrderRequest;
import com.subwayticket.model.result.OrderInfoResult;
import com.subwayticket.model.result.PayOrderResult;
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

public class DisplayActivity extends AppCompatActivity {
    public static final String BUNDLE_KEY_ORDER_ID = "OrderId";
    public static final String BUNDLE_KEY_ORDER = "Order";

    private String orderId;
    private TicketOrder ticketOrder;

    private void refreshOrderInfo(){
        NetworkUtils.ticketOrderGetOrderInfoById(orderId, Info.getInstance().getToken(),
                new Response.Listener<OrderInfoResult>() {
                    @Override
                    public void onResponse(OrderInfoResult response) {
                        ticketOrder = response.getTicketOrder();
                        orderId = ticketOrder.getTicketOrderId();
                        refreshView();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });
    }

    private void refreshView(){
        AboutPage aboutPage = new AboutPage(this)
                .isRTL(false);

        if(ticketOrder.getStatus() == TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET &&
                ticketOrder.getExtractCode() != null){
            aboutPage.setDescription(ticketOrder.getExtractCode());
        }else{
            aboutPage.setImage(R.drawable.ic_ticket_black_96dp);
            aboutPage.setDescription(" ");
        }


        aboutPage.addItem(getCustom(
                        ticketOrder.getStartStation().getDisplayName()
                                + " — "
                                + ticketOrder.getEndStation().getDisplayName()
                        ,
                        Gravity.CENTER
                ));

        switch (ticketOrder.getStatus()){
            case TicketOrder.ORDER_STATUS_NOT_PAY:
                aboutPage.addItem(new Element().setTitle(getString(R.string.pay_bill)).setIcon(R.drawable.btn_default).
                        setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle b = new Bundle();
                                b.putString(PayActivity.BUNDLE_KEY_ORDER_ID, orderId);
                                Intent intent = new Intent(DisplayActivity.this, PayActivity.class);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        }));
                aboutPage.addItem(new Element().setTitle(getString(R.string.cancel_bill)).setIcon(R.drawable.btn_default).
                        setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cancelOrder();
                            }
                        }));
                break;
            case TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET:
                aboutPage.addItem(new Element().setTitle(getString(R.string.refund)).setIcon(R.drawable.btn_default).
                        setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                refundOrder();
                            }
                        }));
                break;
        }

        aboutPage.addGroup(getString(R.string.order_id))
                .addItem(getCustom(ticketOrder.getTicketOrderId()))
                .addGroup(getString(R.string.time_pay))
                .addItem(getCustomWithIcon(CalendarUtils.formatCurrentTimeMills(
                        ticketOrder.getTicketOrderTime().getTime()
                        ), R.drawable.ic_time_black_24dp)
                )
                .addGroup(getString(R.string.ticket_pay))
                .addItem(getCustomWithIcon(String.valueOf(ticketOrder.getTicketPrice()), R.drawable.ic_cash_black_24dp))
                .addGroup(getString(R.string.ticket_count))
                .addItem(getCustomWithIcon(String.valueOf(ticketOrder.getAmount()), R.drawable.ic_num_0_black_24dp))
                .addGroup(getString(R.string.ticket_all_pay))
                .addItem(getCustomWithIcon(String.valueOf(ticketOrder.getTicketPrice() * ticketOrder.getAmount()),
                        R.drawable.ic_cash_black_24dp))
                .addGroup(getString(R.string.order_status))
                .addItem(getCustom(
                        cn.crepusculo.subway_ticket_android.content.TicketOrder.translationCode(
                                this, ticketOrder.getStatus()
                        )));


        /**
         * Build View Here
         */
        View v = aboutPage.create();
        if(ticketOrder.getStatus() == TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET &&
                ticketOrder.getExtractCode() != null){
            ((ImageView)v.findViewById(R.id.image)).setImageBitmap(getOrderQRCode());
        }
        setContentView(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Get action bar and set display HomeAsUp button
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString(BUNDLE_KEY_ORDER, null) != null) {
            ticketOrder = new Gson().fromJson(bundle.getString(BUNDLE_KEY_ORDER), TicketOrder.class);
            orderId = ticketOrder.getTicketOrderId();
            refreshView();
        }else{
            orderId = bundle.getString(BUNDLE_KEY_ORDER_ID);
            refreshOrderInfo();
        }
    }

    private Element getCustomWithIcon(String str, int res) {
        Element element = new Element();
        element.setTitle(str);
        element.setIcon(res);
        element.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        element.setGravity(Gravity.START);
        return element;
    }

    private Element getCustom(String str) {
        Element element = new Element();
        element.setTitle(str);
        element.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        element.setGravity(Gravity.START);
        return element;
    }

    private Element getCustom(String str, int gravity) {
        Element element = new Element();
        element.setTitle(str);
        element.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        element.setGravity(gravity);
        return element;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    private Bitmap getOrderQRCode(){
        try {
            BitMatrix matrix = new QRCodeWriter().encode(ticketOrder.getExtractCode(), BarcodeFormat.QR_CODE, 512, 512);
            int[][] pixels = new int[512][512];
            Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
            for(int y = 0; y < 512; y++){
                for(int x = 0; x < 512; x++){
                    if (matrix.get(x, y)) {
                        bitmap.setPixel(x, y, Color.BLACK);
                    } else {
                        bitmap.setPixel(x, y, Color.WHITE);
                    }
                }
            }
            return bitmap;
        }catch (WriterException we){
            we.printStackTrace();
        }
        return null;
    }

    private void cancelOrder(){
        showConfirmDialog(getString(R.string.query_cancel_bill), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NetworkUtils.ticketOrderCancelById(
                        ticketOrder.getTicketOrderId(),
                        Info.getInstance().getToken(),
                        new Response.Listener<Result>() {
                            @Override
                            public void onResponse(Result response) {
                                Toast.makeText(
                                        getBaseContext(), response.getResultDescription(), Toast.LENGTH_LONG)
                                        .show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                try {
                                    GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                                    Toast.makeText(getBaseContext(), r.result_description, Toast.LENGTH_LONG).show();
                                } catch (NullPointerException e) {}
                            }
                        });
                dialogInterface.dismiss();
            }
        });
    }

    private void refundOrder(){
        showConfirmDialog(getString(R.string.query_refund), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NetworkUtils.ticketOrderRefund(
                        new RefundOrderRequest(ticketOrder.getTicketOrderId()),
                        Info.getInstance().getToken(),
                        new Response.Listener<RefundOrderResult>() {
                            @Override
                            public void onResponse(RefundOrderResult response) {
                                Toast.makeText(DisplayActivity.this, response.getResultDescription(), Toast.LENGTH_LONG)
                                        .show();
                                refreshOrderInfo();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                try {
                                    GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                                    Toast.makeText(DisplayActivity.this, r.result_description, Toast.LENGTH_LONG)
                                            .show();
                                } catch (NullPointerException e) {}
                                refreshOrderInfo();
                            }
                        });
                dialogInterface.dismiss();
            }
        });
    }

    private void showConfirmDialog(String msg, DialogInterface.OnClickListener okListener){
        new AlertDialog.Builder(this).setMessage(msg).setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, okListener)
                .create().show();
    }

}

