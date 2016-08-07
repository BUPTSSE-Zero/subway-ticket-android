package cn.crepusculo.subway_ticket_android.ui.activity.settings;

import android.support.v7.app.ActionBar;

import com.rengwuxian.materialedittext.MaterialEditText;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity;

/**
 * Reset Password
 * <p/>
 * Created by airfr on 2016/8/7.
 */
public class ResetPassword extends BaseActivity {
    MaterialEditText oriEditText;
    MaterialEditText newEditText;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void initView() {
        bindEditText();
        /**
         * Get action bar and set display HomeAsUp button
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    protected void bindEditText() {
        oriEditText = (MaterialEditText) findViewById(R.id.ori_password);
        newEditText = (MaterialEditText) findViewById(R.id.new_password);
    }
}
