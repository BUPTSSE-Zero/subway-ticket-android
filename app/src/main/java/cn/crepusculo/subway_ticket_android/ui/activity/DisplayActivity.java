package cn.crepusculo.subway_ticket_android.ui.activity;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import cn.crepusculo.subway_ticket_android.R;

/**
 * Created by airfr on 2016/8/9.
 */
public class DisplayActivity extends BaseActivity {
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_display;
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
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
