package cn.crepusculo.subway_ticket_android.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.subwayticket.model.request.SubmitOrderRequest;
import com.subwayticket.model.result.Result;
import com.subwayticket.model.result.SubmitOrderResult;

import java.util.Calendar;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.content.TicketOrder;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.util.CalendarUtils;
import cn.crepusculo.subway_ticket_android.util.GsonUtils;
import cn.crepusculo.subway_ticket_android.util.NetworkUtils;
import cn.crepusculo.subway_ticket_android.util.SubwayLineUtil;
import cn.crepusculo.subway_ticket_android.util.TestUtils;

public class SubmitActivity extends BaseActivity {
    private String mode;
    /**
     * Compat
     */
    private ImageButton startPic;
    private ImageButton endPic;
    private TextView startTitle;
    private TextView startText;
    private TextView endTitle;
    private TextView endText;
    private TextView date;
    private TextView dateLimit;
    private EditText editCount;
    private EditText editAmount;
    private EditText editPrice;
    private TicketOrder payRequest;
    private ActionProcessButton checkButton;
    private Button backBtn;
    private Button cancelBtn;
    private Station start;
    private Station end;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_submit;
    }

    @Override
    protected void initView() {
        /**
         * Create payRequest with default value
         * Waiting for NetWork Response
         */
        payRequest = TestUtils.BuildTicketOrder(0, 0, TicketOrder.ORDER_STATUS_NOT_PAY);

        // init toolbar with homeAsUp button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // init functions
        buildBills();
        loadCompacts();
        setCardInfo(payRequest);

        // init mode
        mode = Mode.SUBMIT;
    }

    private void loadCompacts() {
        // start part
        startPic = (ImageButton) findViewById(R.id.start_pic);
        startTitle = (TextView) findViewById(R.id.start_title);
        startText = (TextView) findViewById(R.id.start);
        // end part
        endTitle = (TextView) findViewById(R.id.destination_title);
        endText = (TextView) findViewById(R.id.destination);
        endPic = (ImageButton) findViewById(R.id.destination_pic);
        // date part
        date = (TextView) findViewById(R.id.date);
        dateLimit = (TextView) findViewById(R.id.date_limit);
        // edit text
        editCount = (EditText) findViewById(R.id.count);
        editPrice = (EditText) findViewById(R.id.price);
        editAmount = (EditText) findViewById(R.id.amount);
        // check button
        checkButton = (ActionProcessButton) findViewById(R.id.check_button);
        checkButton.setMode(ActionProcessButton.Mode.ENDLESS);
        backBtn = (Button) findViewById(R.id.back_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        // bind listener
        endPic.setOnClickListener(new ImageButtonOnClickListener());
        startPic.setOnClickListener(new ImageButtonOnClickListener());

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * set checkButton Animation on
                 */
                checkButton.setProgress(32);
                /**
                 * Mode.Submit
                 */
                if (mode.equals(Mode.SUBMIT)) {
                    final int count = TextUtils.isEmpty(editCount.getText()) ?
                            0 : Integer.parseInt(editCount.getText().toString().trim());

                    NetworkUtils.ticketOrderSubmit(
                            new SubmitOrderRequest(
                                    start.getId(),
                                    end.getId(),
                                    count
                            ),
                            Info.getInstance().getToken(),
                            new Response.Listener<SubmitOrderResult>() {
                                @Override
                                public void onResponse(SubmitOrderResult response) {
                                    Log.e("Request", "Success!!");
                                    payRequest = new TicketOrder(response.getTicketOrder());
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            checkButton.setProgress(100);
                                            setSubmitTitle(Mode.SUCCESS);
                                        }
                                    }, 2000);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    checkButton.setProgress(-1);
                                    GsonUtils.Response r;
                                    Log.e("Request", start.getId() + " " + end.getId() + " " + count);
                                    try {
                                        r = GsonUtils.resolveErrorResponse(error);
                                        Snackbar.make(
                                                findViewById(R.id.pay_layout),
                                                r.result_description,
                                                Snackbar.LENGTH_LONG).show();
                                        Log.e("ERROR", error.getMessage() + "\n" + error.getLocalizedMessage());
                                    } catch (NullPointerException e) {
                                        Snackbar.make(
                                                findViewById(R.id.pay_layout),
                                                error.getMessage(),
                                                Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                } // if
                /**
                 * Mode.SUCCESS
                 * jump to PayActivity
                 */
                else {
                    if (payRequest != null) {
                        Bundle b = new Bundle();
                        b.putString(PayActivity.KEY_WORD, new Gson().toJson(payRequest));
                        jumpToActivity(PayActivity.class, b);
                        finish();
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToActivity(MainActivity.class);
                finish();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkUtils.ticketOrderCancelById(
                        payRequest.getTicketOrderId(),
                        Info.getInstance().getToken(),
                        new Response.Listener<Result>() {
                            @Override
                            public void onResponse(Result response) {
                                Toast.makeText(getBaseContext(), response.getResultDescription(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                GsonUtils.Response r;
                                try {
//                                    r = GsonUtils.resolveErrorResponse(error);
                                    Snackbar.make(
                                            findViewById(R.id.pay_layout),
                                            error.getMessage(),
                                            Snackbar.LENGTH_LONG).show();
                                    Log.e("ERROR", error.getMessage() + "\n" + error.getLocalizedMessage());
                                } catch (NullPointerException e) {
                                    Snackbar.make(
                                            findViewById(R.id.pay_layout),
                                            error.getMessage(),
                                            Snackbar.LENGTH_LONG).show();
                                } // try end
                            }
                        }
                ); // network end
            }
        }); // listener end
    }

    /**
     * Get Bundle
     */
    private void buildBills() {
        Bundle b = getBundle();
        if (b != null) {
            String start_str = b.getString("route_start");
            String end_str = b.getString("route_end");
            Log.e("SubmitActivity", start_str + end_str);
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

    /**
     * @param info TickOrder never used
     */
    private void setCardInfo(TicketOrder info) {
        Calendar c = Calendar.getInstance();
        startText.setText(start.getName());
        endText.setText(end.getName());
        date.setText(CalendarUtils.format(c));
        dateLimit.setText(CalendarUtils.format_limit(c));
        SubwayLineUtil.setColor(
                this,
                startPic,
                start.getLine()
        );
        SubwayLineUtil.setColor(
                this,
                endPic,
                end.getLine()
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

    /**
     * Change UI here after mode switch
     *
     * @param m Mode will be
     */
    private void setSubmitTitle(String m) {
        mode = m;
        Log.e("Set", "submitTitle");
        if (mode.equals(Mode.SUBMIT)) {
            checkButton.setText(R.string.post_bill);
        } else {

            editPrice.setText(String.valueOf(payRequest.getTicketPrice()));
            double count = payRequest.getTicketPrice() * Integer.parseInt(editCount.getText().toString().trim());
            editAmount.setText(String.valueOf(count));

            backBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.INVISIBLE);
            checkButton.setText(R.string.pay_bill_now);
        }
    }

    /**
     * Submit.Mode
     * using with submit button
     */
    private static class Mode {
        public static String SUBMIT = "submit";
        public static String SUCCESS = "success";

        private Mode() {
        }
    }

    /**
     * Swap start and end when click image
     */
    private class ImageButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Station swap = start;
            start = end;
            end = swap;
            startText.setText(start.getName());
            endText.setText(end.getName());

            SubwayLineUtil.setColor(
                    SubmitActivity.this,
                    startPic,
                    getResources().getColor(SubwayLineUtil.getColor(start.getLine()))
            );
            SubwayLineUtil.setColor(
                    SubmitActivity.this,
                    endPic,
                    getResources().getColor(SubwayLineUtil.getColor(end.getLine()))
            );
        }
    }
}
