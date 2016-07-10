package cn.crepusculo.subway_ticket_android.ui.activity;

import android.widget.TextView;

import cn.crepusculo.subway_ticket_android.R;

public class TicketDialogActivity extends BaseActivity {
    @Override
    protected int getLayoutResource() {
        return R.layout.item_dialog_bills;
    }

    private TextView start;
    private TextView destination;
    private TextView status;
    private TextView time;

    @Override
    protected void initView() {
        start = (TextView)findViewById(R.id.start);
        destination = (TextView)findViewById(R.id.destination);
        status =(TextView)findViewById(R.id.status);
        time = (TextView)findViewById(R.id.time);
    }
}
