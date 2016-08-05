package cn.crepusculo.subway_ticket_android.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.subwayticket.model.request.SubmitOrderRequest;
import com.subwayticket.model.result.SubmitOrderResult;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.content.TicketOrder;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.utils.CalendarUtils;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;
import cn.crepusculo.subway_ticket_android.utils.TestUtils;

public class PayActivity extends BaseActivity {
    private ImageButton startPic;
    private ImageButton destinationPic;

    private TextView startTitle;
    private TextView startText;
    private TextView destinaionTitle;
    private TextView endText;

    private TextView date;
    private TextView dateLimit;

    private EditText editCount;
    private EditText editBills;
    private EditText editPrice;

    private TicketOrder payRequest;

    private Activity activity;

    private Button checkButton;

    private Station start;
    private Station end;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initView() {
        activity = this;
        payRequest = TestUtils.BuildTicketOrder(0, 0, TicketOrder.ORDER_STATUS_NOT_PAY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        buildBills();
        loadCompacts();
        setCardInfo(payRequest);
    }

    private void loadCompacts() {
        startPic = (ImageButton) findViewById(R.id.start_pic);
        destinationPic = (ImageButton) findViewById(R.id.destination_pic);
        startTitle = (TextView) findViewById(R.id.start_title);
        startText = (TextView) findViewById(R.id.start);
        destinaionTitle = (TextView) findViewById(R.id.destination_title);
        endText = (TextView) findViewById(R.id.destination);
        date = (TextView) findViewById(R.id.date);
        dateLimit = (TextView) findViewById(R.id.date_limit);

        destinationPic.setOnClickListener(new ImageButtonOnClickListener());
        startPic.setOnClickListener(new ImageButtonOnClickListener());

        editCount = (EditText) findViewById(R.id.count);
        editPrice = (EditText) findViewById(R.id.price);
        editBills = (EditText) findViewById(R.id.show_money);

        editPrice.setText(String.valueOf(payRequest.getTicketPrice()));

        editCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String result = "";
                if (editCount.getText().toString().trim().length() != 0) {
                    Log.e("233", "num" + editCount.getText().toString().trim());
                    double count = payRequest.getTicketPrice() * Integer.parseInt(editCount.getText().toString().trim());
                    result += count;
                } else {
                    result = "0.0";
                }
                editBills.setText(result);
            }
        });

        checkButton = (Button) findViewById(R.id.check_button);
//        checkButton.setBackgroundColor(getResources().getColor(R.color.primary));
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int count = TextUtils.isEmpty(editCount.getText()) ?
                        0 : Integer.parseInt(editCount.getText().toString().trim());

                NetworkUtils.ticketOrderSubmit(
                        new SubmitOrderRequest(
//                                SubwayLineUtil.ToServerTypeId(start.getId()),
                                start.getId(),
                                end.getId(),
                                count
//                                SubwayLineUtil.ToServerTypeId(end.getId()),
                        ),
                        Info.getInstance().getToken(),
                        new Response.Listener<SubmitOrderResult>() {
                            @Override
                            public void onResponse(SubmitOrderResult response) {
                                payRequest = new TicketOrder(response.getTicketOrder());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Response r;
                                Log.e("Request", start.getId() + " " + end.getId() + " " + count);
                                // FIXME:: BUG WAITING FOR RESUBMIT
                                try {
                                    String json = new String(error.networkResponse.data, "utf-8");
                                    Log.e("Pay", json + Info.getInstance().getToken());
                                    Gson gson = new Gson();
                                    r = gson.fromJson(json, Response.class);
                                } catch (UnsupportedEncodingException e) {
                                    Log.e("Pay", "Exception" + e);
                                    r = null;
                                } catch (NullPointerException e) {
                                    Snackbar.make(findViewById(R.id.pay_layout), "Something happened", Snackbar.LENGTH_LONG).show();
                                }
//                                try {
//                                    GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
//                                    Snackbar.make(findViewById(R.id.pay_layout), r.result_description, Snackbar.LENGTH_LONG).show();
//                                } catch (NullPointerException e) {
//                                    Snackbar.make(findViewById(R.id.pay_layout), "网络访问超时", Snackbar.LENGTH_LONG).show();
//                                }
                            }
                        });

            }
        });
    }


    private void buildBills() {
        Bundle b = getBundle();
        if (b != null) {
            String start_str = b.getString("route_start");
            String end_str = b.getString("route_end");
            Log.e("PayActivity", start_str + end_str);
            start = new Gson().fromJson(start_str, Station.class);
            end = new Gson().fromJson(end_str, Station.class);
        } else {
            /* Failure to get tickle info */
            new MaterialDialog.Builder(this)
                    .title(R.string.error)
                    .titleColor(getResources().getColor(R.color.primary))
                    .content(R.string.error_failure_to_get_ticket_info)
                    .autoDismiss(true)
                    .show();
        }

    }

    private void setCardInfo(TicketOrder info) {
        Calendar c = Calendar.getInstance();
        startText.setText(start.getName());
        endText.setText(end.getName());
        date.setText(CalendarUtils.format(c));
        SubwayLineUtil.setColor(startPic,
                getResources().getColor(SubwayLineUtil.getColor(start.getLine()))
        );
        SubwayLineUtil.setColor(destinationPic,
                getResources().getColor(SubwayLineUtil.getColor(end.getLine()))
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_center, R.anim.fade_out_center);
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

    private class ImageButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Station swap = start;
            start = end;
            end = swap;
            startText.setText(start.getName());
            endText.setText(end.getName());

            SubwayLineUtil.setColor(startPic,
                    getResources().getColor(SubwayLineUtil.getColor(start.getLine()))
            );
            SubwayLineUtil.setColor(destinationPic,
                    getResources().getColor(SubwayLineUtil.getColor(end.getLine()))
            );
        }
    }
}
