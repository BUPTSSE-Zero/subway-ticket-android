package cn.crepusculo.subway_ticket_android.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.subwayticket.database.model.TicketOrder;

import cn.crepusculo.subway_ticket_android.R;

/**
 * Created by airfr on 2016/8/9.
 */
public class DisplayActivity extends BaseActivity {
    private TicketOrder ticketOrder;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_display;
    }

    @Override
    protected void initView() {
        initActionbar();
        initTicketOrder();
    }

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    private void initTicketOrder() {
        Bundle b = getBundle();
        if (!b.isEmpty())
            ticketOrder = new Gson().fromJson(b.getString(PayActivity.KEY_WORD), TicketOrder.class);
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
}
