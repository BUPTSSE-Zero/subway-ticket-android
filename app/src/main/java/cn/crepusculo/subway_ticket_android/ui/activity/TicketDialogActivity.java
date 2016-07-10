package cn.crepusculo.subway_ticket_android.ui.activity;

import android.support.v7.app.ActionBar;
import android.view.Window;
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
    public void beforeAddContent() {
        super.beforeAddContent();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void initView() {
        start = (TextView)findViewById(R.id.start);
        destination = (TextView)findViewById(R.id.destination);
        status =(TextView)findViewById(R.id.status);
        time = (TextView)findViewById(R.id.time);
    }
}
