package cn.crepusculo.subway_ticket_android.ui.activity.login;

import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
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
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity;
import cn.crepusculo.subway_ticket_android.ui.activity.MainActivity;
import cn.crepusculo.subway_ticket_android.utils.CircularAnimUtil;
import cn.crepusculo.subway_ticket_android.utils.GsonUtils;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;

public class LoginActivity<T> extends BaseActivity implements View.OnClickListener {
    private static class Mode {
        public static String REGISTER = "register";
        public static String LOGIN = "login";
        public static String UPDATE = "update";
        public static String CAPTCHA = "captcha";

        private Mode() {
        }
    }

    ViewGroup.LayoutParams buttonSize;

    // Default method login
    private String mode = Mode.LOGIN;

    private LinearLayout card;
    private MaterialEditText editTextUserName;
    private MaterialEditText editTextPassword;

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
        buttonSize = new ViewGroup.LayoutParams(
                forgetBtn.getLayoutParams().width,
                forgetBtn.getLayoutParams().height);
    }

    private void initCard() {
        card = (LinearLayout) findViewById(R.id.card);

        editTextUserName = (MaterialEditText) findViewById(R.id.edit_text_id);
        editTextPassword = (MaterialEditText) findViewById(R.id.edit_text_pwd);

        editTextCaptcha = (MaterialEditText) findViewById(R.id.edit_text_captcha);

        signBtn = (Button) findViewById(R.id.login_signup);
        forgetBtn = (Button) findViewById(R.id.login_forget);
        loginBtn = (ActionProcessButton) findViewById(R.id.login_login);

        loginBtn.setOnClickListener(this);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSubmitTitle(Mode.CAPTCHA);

            }
        });
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSubmitTitle(Mode.UPDATE);
            }
        });

        // --------- Fill with cache ------------
        editTextUserName.setText(Info.getInstance().user.getId());
        editTextPassword.setText(Info.getInstance().user.getPassword());
    }

    private void initExpandableMode() {

    }

    @Override
    public void onBackPressed() {
        loginBtn.setProgress(0);
        if (forgetBtn.getVisibility() == View.INVISIBLE) {
            mode = Mode.LOGIN;
            showView(forgetBtn);
            showView(signBtn);
            if (editTextCaptcha.getVisibility() == View.VISIBLE) {
                hideView(editTextCaptcha);
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
        } else if (mode.equals(Mode.UPDATE)) {
            loginBtn.setText(R.string.login_update);
        } else {
            loginBtn.setText(R.string.login_signup);
        }
    }

    private void setSubmitTitle(String mode) {
        this.mode = mode;
        loginBtn.setProgress(0);
        setSubmitTitle();

        if (mode.equals(Mode.CAPTCHA)) {

            /**
             * Captcha - pwdEditText
             */
            editTextPassword.setHint(getString(R.string.login_user_password));
            /**
             * Captcha - captchaEditText
             */
            hideView(editTextCaptcha);
            /**
             * Captcha - signUpBtn
             */
            changeBtn(signBtn);
            resetBtn(forgetBtn);
        }

        // == END IF == captcha
        else if (mode.equals(Mode.REGISTER)) {
            /**
             * Register - pwdEditText
             */
            editTextPassword.setHint(getString(R.string.login_user_password));
            /**
             * Register - captchaEditText
             */
            showView(editTextCaptcha);
        }

        // == END IF == register
        else if (mode.equals(Mode.LOGIN)) {
            /**
             * Login - pwdEditText
             */
            editTextPassword.setHint(getString(R.string.login_user_password));
            /**
             * Login - captchaEditText
             */
            hideView(editTextCaptcha);
            /**
             * Login - forgetBtn
             */
            resetBtn(forgetBtn);
            /**
             * Login - signUpBtn
             */
            resetBtn(signBtn);
        }

        // == END IF == login
        else if (mode.equals(Mode.UPDATE)) {
            /**
             * Update - pwdEditText
             */
            editTextPassword.setHint(getString(R.string.login_user_password_update));
            /**
             * Update - captchaEditText
             */
            showView(editTextCaptcha);
            /**
             * Update - forgetBtn
             */
            /**
             * Update - signUpBtn
             */
            changeBtn(forgetBtn);
            resetBtn(signBtn);
        } // == END IF == update
        else {
        } // == END ELSE == else

    }

    protected void showView(View v) {
        if (v.getClass().toString().equals(forgetBtn.getClass().toString())) {
            // if is btn
            ViewGroup.LayoutParams lp = new AppBarLayout.LayoutParams(buttonSize.width, buttonSize.height);
            v.setLayoutParams(lp);
        } else {
            // if is edit text
            ViewGroup.LayoutParams lp = new AppBarLayout.LayoutParams(
                    editTextUserName.getLayoutParams().width,
                    editTextUserName.getLayoutParams().height);
            v.setLayoutParams(lp);
        }
        v.setVisibility(View.VISIBLE);
    }

    protected void hideView(View v) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = 0;
        v.setLayoutParams(lp);
        v.setVisibility(View.INVISIBLE);
    }

    protected void changeBtn(View v) {
        Button btn = (Button) v;
        if (btn.getText().toString().trim().equals(getResources().getString(R.string.login_signup))
                || btn.getText().toString().trim().equals(getResources().getString(R.string.login_forget))
                || btn.getText().toString().trim().equals(getResources().getString(R.string.login_captcha))) {
            btn.setText(R.string.login_backup);
            btn.setTextColor(getResources().getColor(R.color.accent));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSubmitTitle(Mode.LOGIN);
                }
            });
        }
    }

    protected void resetBtn(View v) {
        Button btn = (Button) v;
        btn.setTextColor(getResources().getColor(R.color.primary));
        if (btn.getId() == R.id.login_forget) {
            btn.setText(R.string.login_forget);
            btn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           setSubmitTitle(Mode.UPDATE);
                                       }
                                   }
            );
        } else if (btn.getId() == R.id.login_signup) {
            btn.setText(R.string.login_signup);
            btn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           setSubmitTitle(Mode.CAPTCHA);
                                       }
                                   }
            );
        }
    }

    @Override
    public void onClick(View view) {
        loginBtn.setMode(ActionProcessButton.Mode.ENDLESS);
        loginBtn.setProgress(32);

        final String loginId = editTextUserName.getText().toString().trim();
        final String loginPwd = editTextPassword.getText().toString().trim();

        /**
         * Mode.LOGIN
         * Use to login
         */
        if (mode.equals(Mode.LOGIN)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    NetworkUtils.accountLogin(
                            new LoginRequest(loginId, loginPwd),
                            new Response.Listener<MobileLoginResult>() {
                                @Override
                                public void onResponse(MobileLoginResult response) {
                                    int code = response.getResultCode();
                                    Log.e("Login", "Success!" + response.getResultCode() + response.getResultDescription());

                                    Info.getInstance().user.setId(loginId);
                                    Info.getInstance().user.setPassword(loginPwd);
                                    Info.getInstance().setToken("" + code);

                                    loginBtn.setProgress(100);
                                    CircularAnimUtil.PERFECT_MILLS = 329;
                                    CircularAnimUtil.startActivity(
                                            LoginActivity.this,
                                            MainActivity.class,
                                            (View) loginBtn, R
                                                    .color.primary);
                                    CircularAnimUtil.resetMills();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    loginBtn.setProgress(-1);
                                }
                            }
                    );
                }
            }, 1500);
        } // END IF

        /**
         * Mode.REGISTER
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
                                    }, 1500);
                                }
                            }, 1500);
                            /**
                             * IF AND ONLY IF getCaptcha successful
                             *
                             */
                            showView(editTextCaptcha);
                            hideView(signBtn);
                            hideView(forgetBtn);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Register GetCaptcha", "Error!" + editTextUserName.getText().toString().trim());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Update mode
                                    loginBtn.setProgress(-1);
                                }
                            }, 1500);
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
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Update mode
                                    loginBtn.setProgress(-1);
                                }
                            }, 1500);
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                            Snackbar.make(findViewById(R.id.login_activity), r.result_description, Snackbar.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Update mode
                                    loginBtn.setProgress(-1);
                                }
                            }, 1500);
                        }
                    }
            );
        }
    }

}
