package cn.crepusculo.subway_ticket_android.ui.activity.login;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.subwayticket.model.result.Result;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity;
import cn.crepusculo.subway_ticket_android.ui.activity.MainActivity;
import cn.crepusculo.subway_ticket_android.util.CircularAnimUtil;
import cn.crepusculo.subway_ticket_android.util.GsonUtils;
import cn.crepusculo.subway_ticket_android.util.NetworkUtils;

public class SplashActivity extends BaseActivity {
    View view;
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        view = findViewById(R.id.welcome_view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Info.getInstance().getToken() != null){
                    NetworkUtils.accountCheckLogin(Info.getInstance().token, new Response.Listener<Result>() {
                        @Override
                        public void onResponse(Result response) {
                            goToMainActivity();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                            Info.getInstance().setToken(null);
                            if(r.result_code == 401)
                                Toast.makeText(SplashActivity.this, getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                            goToMainActivity();
                        }
                    });
                }else {
                    goToMainActivity();
                }
            }
        }, 1500);
    }

    private void goToMainActivity(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        CircularAnimUtil.startActivityThenFinish(SplashActivity.this, intent, false, view, R.color.primary, 300);
    }
}
