package cn.crepusculo.subway_ticket_android.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Calendar;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.BillsCardViewContent;
import cn.crepusculo.subway_ticket_android.utils.CalendarUtils;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;

public class PayActivity extends BaseActivity {
    private ImageButton startPic;
    private ImageButton destinationPic;

    private TextView startTitle;
    private TextView start;
    private TextView destinaionTitle;
    private TextView destination;

    private TextView date;
    private TextView dateLimit;
    private BillsCardViewContent payRequest;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initView() {
        payRequest = new BillsCardViewContent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        loadCompats();
        buildBills();
    }

    private void loadCompats() {
        startPic = (ImageButton) findViewById(R.id.start_pic);
        destinationPic = (ImageButton) findViewById(R.id.destination_pic);
        startTitle = (TextView) findViewById(R.id.start_title);
        start = (TextView) findViewById(R.id.start);
        destinaionTitle = (TextView) findViewById(R.id.destination_title);
        destination = (TextView) findViewById(R.id.destination);
        date = (TextView) findViewById(R.id.date);
        dateLimit = (TextView) findViewById(R.id.date_limit);
        destinationPic.setOnClickListener(new ImageButtonOnClickListener());
        startPic.setOnClickListener(new ImageButtonOnClickListener());
    }

    private void buildBills() {
        Bundle b = getBundle();
        if (b != null) {
            String start_str = b.getString("route_start");
            String destination_str = b.getString("route_end");
            Log.e("PayActivity", start_str + destination_str);
            payRequest.start = start_str;
            payRequest.start_line = SubwayLineUtil.getLine(start_str);
            payRequest.destination = destination_str;
            payRequest.destination_line = SubwayLineUtil.getLine(destination_str);
            /* default set date with system date */
            Calendar c = Calendar.getInstance();
            payRequest.date = CalendarUtils.format(c);
            payRequest.status = BillsCardViewContent.TICKET_UNPAID;
            setCardInfo(payRequest);
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

    private void setCardInfo(BillsCardViewContent info) {
        Calendar c = Calendar.getInstance();
        start.setText(info.start);
        destination.setText(info.destination);
        date.setText(CalendarUtils.format(c));
        BillsCardViewContent.setTagColor(this,
                startPic, SubwayLineUtil.getColor(info.start_line),
                destinationPic, SubwayLineUtil.getColor(info.destination_line));
        dateLimit.setText(CalendarUtils.format_limit(c));
    }
    private class ImageButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String swap = start.getText().toString();
            start.setText(destination.getText());
            destination.setText(swap);
        }
    }
}
