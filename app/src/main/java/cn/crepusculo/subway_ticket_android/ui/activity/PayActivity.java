package cn.crepusculo.subway_ticket_android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.request.PayOrderRequest;
import com.subwayticket.model.result.PayOrderResult;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.activity.settings.ResetPassword;
import cn.crepusculo.subway_ticket_android.util.NetworkUtils;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class PayActivity extends AppCompatActivity {
    public static final String KEY_WORD = "key";
    private TicketOrder order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View settingsView = new AboutPage(this)
                .isRTL(false)
                .setDescription(" ")
                .setImage(R.color.primary)
                .addItem(getAlipay())
                .addItem(getWechat())
                .create();
        /**
         * Build View Here
         */
        setContentView(settingsView);
        /**
         * Get action bar and set display HomeAsUp button
         */
        Bundle b = getIntent().getExtras();

        if (!b.isEmpty()) {
            order = new Gson().fromJson(b.getString(KEY_WORD), TicketOrder.class);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    Element getAlipay() {
        Element element = new Element();
        element.setTitle("支付宝");
        element.setIcon(R.mipmap.images);
        element.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        element.setGravity(Gravity.START);
        element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return element;
    }

    Element getWechat() {
        Element element = new Element();
        element.setTitle("微信支付");
        element.setIcon(R.drawable.ic_wechat_24dp);
        element.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        element.setGravity(Gravity.START);
        element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayActivity.this, ResetPassword.class);
                startActivity(intent);
                finish();
            }
        });
        return element;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void payTicket() {


        NetworkUtils.ticketOrderPay(
                new PayOrderRequest(order.getTicketOrderId()),
                Info.getInstance().getToken(),
                new Response.Listener<PayOrderResult>() {
                    @Override
                    public void onResponse(PayOrderResult response) {
                        order.setExtractCode(response.getExtractCode());
                        Toast.makeText(getBaseContext(), response.getResultDescription(), Toast.LENGTH_SHORT).show();

                        Bundle b = new Bundle();
                        b.putString(KEY_WORD, new Gson().toJson(order));
                        Intent intent = new Intent(PayActivity.this, DisplayActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }
}
