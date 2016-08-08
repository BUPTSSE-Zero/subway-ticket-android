package cn.crepusculo.subway_ticket_android.ui.activity.settings;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.subwayticket.model.request.ModifyPasswordRequest;
import com.subwayticket.model.result.Result;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity;
import cn.crepusculo.subway_ticket_android.util.GsonUtils;
import cn.crepusculo.subway_ticket_android.util.NetworkUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Reset Password
 * <p/>
 * Created by airfr on 2016/8/7.
 */
public class ResetPassword extends BaseActivity {
    MaterialEditText oriEditText;
    MaterialEditText newEditText;
    Button nextButton;
    SmoothProgressBar progressBar;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        progressBar = (SmoothProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        bindEditText();
        bindButton();

    }

    protected void bindEditText() {
        oriEditText = (MaterialEditText) findViewById(R.id.ori_password);
        newEditText = (MaterialEditText) findViewById(R.id.new_password);
    }

    protected void bindButton() {

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                /**
                 * Make network request
                 *
                 * Read password from EditText
                 *
                 */
                progressBar.setVisibility(View.VISIBLE);
                final String newPassword = newEditText.getText().toString().trim();
                NetworkUtils.accountModifyPassword(
                        new ModifyPasswordRequest(
                                oriEditText.getText().toString().trim(),
                                newPassword),
                        Info.getInstance().getToken(),
                        new Response.Listener<Result>() {
                            @Override
                            public void onResponse(Result response) {
                                /**
                                 * Make delay display effect
                                 */
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Snackbar.make(findViewById(R.id.action_reset_pwd), "Success!", Snackbar.LENGTH_LONG).show();
                                        Info.getInstance().user.setPassword(newPassword);
                                        oriEditText.setText(null);
                                        newEditText.setText(null);
                                    }
                                }, 1500);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                try {
                                    GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                                    Snackbar.make(findViewById(R.id.action_reset_pwd), r.result_description, Snackbar.LENGTH_LONG).show();
                                } catch (NullPointerException e) {
                                    Snackbar.make(findViewById(R.id.action_reset_pwd), "网络访问超时", Snackbar.LENGTH_LONG).show();
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }, 2000);
                            }
                        });
            }
        });
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
