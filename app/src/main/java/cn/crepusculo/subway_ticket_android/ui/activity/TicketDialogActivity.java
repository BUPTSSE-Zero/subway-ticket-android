package cn.crepusculo.subway_ticket_android.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.TicketOrder;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;

public class TicketDialogActivity extends BaseActivity {
    private TextView start;
    private TextView destination;
    private TextView status;
    private TextView date;
    private TicketOrder bills;

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_dialog_bills;
    }

    @Override
    protected void initView() {
        getInfo();
        initResource();
        initTextView();
    }
    private void getInfo(){
        String myBills;
        Bundle bundle = getBundle();
        if (bundle != null){
            myBills = bundle.getString("BILLS");
            bills = new Gson().fromJson(myBills, TicketOrder.class);
        }
    }

    private void initResource(){
        start = (TextView)findViewById(R.id.start_dialog);
        destination = (TextView)findViewById(R.id.destination_dialog);
        status =(TextView)findViewById(R.id.status);
        date = (TextView)findViewById(R.id.date);
    }
    private void initTextView(){
        start.setText(bills.getStartStation().getSubwayStationName());
        destination.setText(bills.getEndStation().getSubwayStationName());
        date.setText("" + bills.getTicketOrderTime());
        status.setText(bills.getStatus());
        ImageView v_s = (ImageView)findViewById(R.id.come_dialog);
        ImageView v_d = (ImageView)findViewById(R.id.go_dialog);
//        v_s.setColorFilter(R.color.accent);
        SubwayLineUtil.setColor(
                v_s,
                SubwayLineUtil.getColor(
                        SubwayLineUtil.ToClientTypeId(bills.getStartStation().getSubwayLine().getSubwayLineId()
                        )
                )
        );
        SubwayLineUtil.setColor(
                v_d,
                SubwayLineUtil.getColor(
                        SubwayLineUtil.ToClientTypeId(bills.getEndStation().getSubwayLine().getSubwayLineId()
                        )
                )
        );
    }
}
