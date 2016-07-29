package cn.crepusculo.subway_ticket_android.ui.activity.login;

import android.widget.EditText;
import android.widget.LinearLayout;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity;

public class LoginActivity extends BaseActivity {

    private LinearLayout card;
    private EditText editTextUserName;
    private EditText editTextPassword;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        initCard();
    }

    private void initCard() {
        card = (LinearLayout) findViewById(R.id.card);
        editTextUserName = (EditText) findViewById(R.id.edit_text_id);
        editTextPassword = (EditText) findViewById(R.id.edit_text_pwd);

    }

}
