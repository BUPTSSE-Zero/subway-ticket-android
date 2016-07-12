package cn.crepusculo.subway_ticket_android.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.BillsCardViewContent;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;

public class TicketDialogActivity extends BaseActivity {
    @Override
    protected int getLayoutResource() {
        return R.layout.layout_dialog_bills;
    }

    private TextView start;
    private TextView destination;
    private TextView status;
    private TextView date;
    private BillsCardViewContent bills;

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
            bills = new Gson().fromJson(myBills, BillsCardViewContent.class);
        }
    }

    private void initResource(){
        start = (TextView)findViewById(R.id.start_dialog);
        destination = (TextView)findViewById(R.id.destination_dialog);
        status =(TextView)findViewById(R.id.status);
        date = (TextView)findViewById(R.id.date);
    }
    private void initTextView(){
        start.setText(bills.start);
        destination.setText(bills.destination);
        date.setText("2017-4-26");
        status.setText(bills.getStatus());
        ImageView v_s = (ImageView)findViewById(R.id.come_dialog);
        ImageView v_d = (ImageView)findViewById(R.id.go_dialog);
//        v_s.setColorFilter(R.color.accent);
        BillsCardViewContent.setTagColor(getBaseContext(), v_s, SubwayLineUtil.getColor(bills.start_line),
                v_d,SubwayLineUtil.getColor(bills.destination_line));
    }
}
