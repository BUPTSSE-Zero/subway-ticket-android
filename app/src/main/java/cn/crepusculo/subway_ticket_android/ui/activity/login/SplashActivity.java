package cn.crepusculo.subway_ticket_android.ui.activity.login;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity;
import cn.crepusculo.subway_ticket_android.utils.CircularAnimUtil;

public class SplashActivity extends BaseActivity {
    View view;
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        view = findViewById(R.id.view);
        /**
         * TODO:: May we should add a background for it ?
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToLoginActivity();
            }
        }, 2000);
    }

    private void goToLoginActivity() {
//        jumpToActivity(LoginActivity.class);
        Intent intent = new Intent(this,LoginActivity.class);
        CircularAnimUtil.startActivityThenFinish(this,intent,false,view,R.color.primary,329);
    }

}
