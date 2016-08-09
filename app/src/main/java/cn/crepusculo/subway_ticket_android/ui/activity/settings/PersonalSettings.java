package cn.crepusculo.subway_ticket_android.ui.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.subwayticket.model.result.Result;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.activity.AppCompatPreferenceActivity;
import cn.crepusculo.subway_ticket_android.util.NetworkUtils;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */

public class PersonalSettings extends AppCompatPreferenceActivity {
    private Info info = Info.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.personal_settings);
        findPreference(getResources().getString(R.string.action_logout)).
                setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        NetworkUtils.accountLogout(info.getToken(), new Response.Listener<Result>() {
                            @Override
                            public void onResponse(Result response) {
                                info.setToken(null);
                                PersonalSettings.this.finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                info.setToken(null);
                                PersonalSettings.this.finish();
                            }
                        });
                        return true;
                    }
                });
        findPreference(getString(R.string.action_modify_password)).
                setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent(PersonalSettings.this, ModifyPassword.class);
                        startActivity(intent);
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
