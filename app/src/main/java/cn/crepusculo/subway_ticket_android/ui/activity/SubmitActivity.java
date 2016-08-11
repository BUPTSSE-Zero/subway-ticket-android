package cn.crepusculo.subway_ticket_android.ui.activity;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.subwayticket.database.model.SubwayStation;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.request.SubmitOrderRequest;
import com.subwayticket.model.result.Result;
import com.subwayticket.model.result.SubmitOrderResult;

import java.util.Calendar;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.activity.login.LoginActivity;
import cn.crepusculo.subway_ticket_android.util.GsonUtils;
import cn.crepusculo.subway_ticket_android.util.NetworkUtils;
import cn.crepusculo.subway_ticket_android.util.SubwayLineUtil;

public class SubmitActivity extends BaseActivity {
    public static String BUNDLE_KEY_START_STATION = "StartStation";
    public static String BUNDLE_KEY_END_STATION = "EndStation";
    public static String BUNDLE_KEY_TICKET_PRICE = "TicketPrice";

    private String mode;
    /**
     * Compat
     */
    private ImageButton startPic;
    private ImageButton endPic;
    private TextView startStationText;
    private TextView endStationText;
    private TextView textCount;
    private TextView textAmount;
    private TextView textPrice;
    private ActionProcessButton checkButton;
    private Button backBtn;
    private Button cancelBtn;
    private ImageButton addAmountBtn;
    private ImageButton subAmountBtn;
    private SubwayStation startStation;
    private SubwayStation endStation;
    private float ticketPrice;
    private TicketOrder payRequest;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_submit;
    }

    @Override
    protected void initView() {
        // init toolbar with homeAsUp button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // init functions
        buildBills();
        loadCompacts();
        setCardInfo();

        // init mode
        mode = Mode.SUBMIT;
    }

    private void loadCompacts() {
        // start part
        startStationText = (TextView) findViewById(R.id.start);
        startPic = (ImageButton) findViewById(R.id.start_pic);
        // end part
        endStationText = (TextView) findViewById(R.id.destination);
        endPic = (ImageButton) findViewById(R.id.destination_pic);
        // text
        textCount = (TextView) findViewById(R.id.count);
        textPrice = (TextView) findViewById(R.id.price);
        textAmount = (TextView) findViewById(R.id.amount);
        textPrice.setText(String.valueOf(ticketPrice));
        textAmount.setText(String.valueOf(ticketPrice * Integer.parseInt(textCount.getText().toString())));
        // button
        addAmountBtn = (ImageButton) findViewById(R.id.btn_increase_amount);
        addAmountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newCount = Integer.parseInt(textCount.getText().toString());
                newCount++;
                if(newCount > 10)
                    newCount = 10;
                textCount.setText(String.valueOf(newCount));
                textAmount.setText(String.valueOf(ticketPrice * newCount));
            }
        });
        subAmountBtn = (ImageButton) findViewById(R.id.btn_subtract_amount);
        subAmountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newCount = Integer.parseInt(textCount.getText().toString());
                newCount--;
                if(newCount < 1)
                    newCount = 1;
                textCount.setText(String.valueOf(newCount));
                textAmount.setText(String.valueOf(ticketPrice * newCount));
            }
        });
        // check button
        checkButton = (ActionProcessButton) findViewById(R.id.check_button);
        checkButton.setMode(ActionProcessButton.Mode.ENDLESS);
        backBtn = (Button) findViewById(R.id.back_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);

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
                    final int count = TextUtils.isEmpty(textCount.getText()) ?
                            0 : Integer.parseInt(textCount.getText().toString().trim());

                    if(Info.getInstance().getToken() == null){
                        checkButton.setProgress(0);
                        Intent intent = new Intent(SubmitActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return;
                    }

                    NetworkUtils.ticketOrderSubmit(
                            new SubmitOrderRequest(
                                    startStation.getSubwayStationId(),
                                    endStation.getSubwayStationId(),
                                    count
                            ),
                            Info.getInstance().getToken(),
                            new Response.Listener<SubmitOrderResult>() {
                                @Override
                                public void onResponse(SubmitOrderResult response) {
                                    Log.e("Request", "Success!!");
                                    payRequest = response.getTicketOrder();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            checkButton.setProgress(100);
                                            setSubmitTitle(Mode.SUCCESS);
                                        }
                                    }, 1000);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    checkButton.setProgress(-1);
                                    GsonUtils.Response r;
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
                        b.putString(PayActivity.BUNDLE_KEY_ORDER_ID, payRequest.getTicketOrderId());
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
            String start_str = b.getString(BUNDLE_KEY_START_STATION);
            String end_str = b.getString(BUNDLE_KEY_END_STATION);
            startStation = new Gson().fromJson(start_str, SubwayStation.class);
            endStation = new Gson().fromJson(end_str, SubwayStation.class);
            ticketPrice = b.getFloat(BUNDLE_KEY_TICKET_PRICE);
        }
    }

    private void setCardInfo() {
        Calendar c = Calendar.getInstance();
        startStationText.setText(startStation.getDisplayName());
        endStationText.setText(endStation.getDisplayName());
        SubwayLineUtil.setColor(
                this,
                startPic,
                Station.SubwayStationAdapter(startStation).getLine()
        );
        SubwayLineUtil.setColor(
                this,
                endPic,
                Station.SubwayStationAdapter(endStation).getLine()
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
}
