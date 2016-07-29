package cn.crepusculo.subway_ticket_android.ui.activity.login;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dd.processbutton.iml.ActionProcessButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.subwayticket.model.request.LoginRequest;
import com.subwayticket.model.request.PhoneCaptchaRequest;
import com.subwayticket.model.request.RegisterRequest;
import com.subwayticket.model.result.MobileLoginResult;
import com.subwayticket.model.result.Result;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;

public class LoginActivity extends BaseActivity {
    private static class Mode {
        public static String REGISTER = "register";
        public static String LOGIN = "login";
        public static String UPDATE = "update";
        public static String CAPTCHA = "captcha";

        private Mode() {

        }
    }

    // Default method login
    private String mode = Mode.LOGIN;

    private LinearLayout card;
    private MaterialEditText editTextUserName;
    private MaterialEditText editTextPassword;

    private MaterialEditText editTextCheck;
    private MaterialEditText editTextCaptcha;

    private ActionProcessButton loginBtn;
    private Button signBtn;
    private Button forgetBtn;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        initCard();
        initExpandableMode();
    }

    private void initCard() {
        card = (LinearLayout) findViewById(R.id.card);

        editTextUserName = (MaterialEditText) findViewById(R.id.edit_text_id);
        editTextPassword = (MaterialEditText) findViewById(R.id.edit_text_pwd);

        editTextCaptcha = (MaterialEditText) findViewById(R.id.edit_text_captcha);
        editTextCheck = (MaterialEditText) findViewById(R.id.edit_text_check);
        signBtn = (Button) findViewById(R.id.login_signup);
        forgetBtn = (Button) findViewById(R.id.login_forget);
        loginBtn = (ActionProcessButton) findViewById(R.id.login_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setMode(ActionProcessButton.Mode.ENDLESS);
                loginBtn.setProgress(32);

                /**
                 * Mode.LOGIN
                 * Use to login
                 */
                if (mode.equals(Mode.LOGIN)) {
                    NetworkUtils.accountLogin(
                            new LoginRequest(
                                    editTextUserName.getText().toString().trim(),
                                    editTextPassword.getText().toString().trim()
                            ),
                            new Response.Listener<MobileLoginResult>() {
                                @Override
                                public void onResponse(MobileLoginResult response) {
                                    int code = response.getResultCode();
                                    Log.e("Login", "Success!" + response.getResultCode() + response.getResultDescription());
                                    loginBtn.setProgress(100);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Login", "Error!" + error.getCause() + error.getMessage() + error.getLocalizedMessage());
                                    Log.e("Login", "Error!" + editTextUserName.getText().toString().trim());
                                    Log.e("Login", "Error!" + editTextPassword.getText().toString().trim());
                                    loginBtn.setProgress(-1);
                                }
                            }
                    );
                } // END IF

                /**
                 * Mode.LOGIN
                 * Use to Register a new account
                 * Step 1. get Captcha
                 * Step 2. register
                 *
                 */
                else if (mode.equals(Mode.CAPTCHA)) {
                    //TODO:: check phomeNumber invalid
                    NetworkUtils.accountGetCaptcha(
                            new PhoneCaptchaRequest(editTextUserName.getText().toString().trim()),
                            new Response.Listener<Result>() {
                                @Override
                                public void onResponse(Result response) {
                                    Log.e("Register GetCaptcha", "Success!" + response.getResultCode());
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loginBtn.setProgress(100);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Update mode
                                                    loginBtn.setProgress(0);
                                                    mode = Mode.REGISTER;
                                                    setSubmitTitle();
                                                }
                                            },1500);
                                        }
                                    },1500);
                                    /**
                                     * IF AND ONLY IF getCaptcha successful
                                     *
                                     */
                                    editTextCaptcha.setVisibility(View.VISIBLE);
                                    ViewGroup.LayoutParams lp = editTextCaptcha.getLayoutParams();
                                    ViewGroup.LayoutParams newlp = editTextUserName.getLayoutParams();
                                    lp.height = newlp.height;
                                    editTextCaptcha.setLayoutParams(lp);
                                    signBtn.setVisibility(View.INVISIBLE);
                                    forgetBtn.setVisibility(View.INVISIBLE);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Register GetCaptcha", "Error!" + editTextUserName.getText().toString().trim());
                                    loginBtn.setProgress(-1);
                                }
                            });
                } else if (mode.equals(Mode.REGISTER)) {

                    NetworkUtils.accountRegister(new RegisterRequest(
                                    editTextUserName.getText().toString().trim(),
                                    editTextPassword.getText().toString().trim(),
                                    editTextCaptcha.getText().toString().trim()),
                            new Response.Listener<Result>() {
                                @Override
                                public void onResponse(Result response) {
                                    Log.e("Register", "Success!" + response.getResultCode());
                                    loginBtn.setProgress(100);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Register", "Error!" + error.getCause() + error.getMessage() + error.getLocalizedMessage());
                                    loginBtn.setProgress(-1);
                                }
                            }
                    );
                }
            }
        });
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = Mode.CAPTCHA;
                setSubmitTitle();
            }
        });
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = Mode.UPDATE;
                editTextCheck.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams lp = editTextCheck.getLayoutParams();
                ViewGroup.LayoutParams newlp = editTextUserName.getLayoutParams();
                lp.height = newlp.height;
                editTextCheck.setLayoutParams(lp);
                signBtn.setVisibility(View.INVISIBLE);
                forgetBtn.setVisibility(View.INVISIBLE);
                setSubmitTitle();
            }
        });
    }

    private void initExpandableMode() {

    }

    @Override
    public void onBackPressed() {
        loginBtn.setProgress(0);
        if (forgetBtn.getVisibility() == View.INVISIBLE) {
            mode = Mode.LOGIN;
            forgetBtn.setVisibility(View.VISIBLE);
            signBtn.setVisibility(View.VISIBLE);
            if (editTextCaptcha.getVisibility() == View.VISIBLE) {
                ViewGroup.LayoutParams lp = editTextCaptcha.getLayoutParams();
                lp.height = 0;
                editTextCaptcha.setLayoutParams(lp);
                editTextCaptcha.setVisibility(View.INVISIBLE);
            } else {
                ViewGroup.LayoutParams lp = editTextCheck.getLayoutParams();
                lp.height = 0;
                editTextCheck.setLayoutParams(lp);
                editTextCheck.setVisibility(View.INVISIBLE);
            }
            setSubmitTitle();
        }
    }

    private void setSubmitTitle() {
        Log.e("Set", "submitTitle");
        if (mode.equals(Mode.LOGIN)) {
            loginBtn.setText(R.string.login_submit);
        } else if (mode.equals(Mode.CAPTCHA)) {
            loginBtn.setText(R.string.login_captcha);
        } else if (mode.equals(Mode.UPDATE)){
            loginBtn.setText(R.string.login_update);
        } else {
            loginBtn.setText(R.string.login_signup);
        }
    }

}
