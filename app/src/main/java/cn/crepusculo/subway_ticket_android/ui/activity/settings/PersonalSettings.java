package cn.crepusculo.subway_ticket_android.ui.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.subwayticket.model.result.Result;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.activity.login.LoginActivity;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class PersonalSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View settingsView = new AboutPage(this)
                .isRTL(false)
                .setDescription(" ")
                .setImage(R.mipmap.side_menu_bk)
                .addItem(getUpdater())
                .addItem(getLogout())
                .create();
        /**
         * Build View Here
         */
        setContentView(settingsView);
        /**
         * Get action bar and set display HomeAsUp button
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    Element getUpdater() {
        Element element = new Element();
        element.setTitle("更改密码");
        element.setIcon(R.drawable.ic_lock_black_24dp);
        element.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        element.setGravity(Gravity.START);
        element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalSettings.this, ResetPassword.class);
                startActivity(intent);
                finish();
            }
        });
        return element;
    }

    Element getLogout() {
        Element element = new Element();
        element.setTitle("注销登陆");
        element.setIcon(R.drawable.ic_alert);
        element.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        element.setGravity(Gravity.START);
        element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUtils.accountLogout(
                        Info.getInstance().getToken(),
                        new Response.Listener<Result>() {
                            @Override
                            public void onResponse(Result response) {
                                Intent intent = new Intent(PersonalSettings.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Intent intent = new Intent(PersonalSettings.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                );
            }
        });
        return element;
    }
}
