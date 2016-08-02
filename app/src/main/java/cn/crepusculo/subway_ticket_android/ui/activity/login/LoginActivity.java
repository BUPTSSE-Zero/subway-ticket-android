package cn.crepusculo.subway_ticket_android.ui.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dd.processbutton.iml.ActionProcessButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.subwayticket.model.request.LoginRequest;
import com.subwayticket.model.request.PhoneCaptchaRequest;
import com.subwayticket.model.request.RegisterRequest;
import com.subwayticket.model.request.ResetPasswordRequest;
import com.subwayticket.model.result.MobileLoginResult;
import com.subwayticket.model.result.Result;

import org.w3c.dom.Text;

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

    private TextSwitcher textSwitcher;
    private TextSwitcher textSwitcher2;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        initCard();
        initHint();
        initExpandableMode();
        buttonSize = new ViewGroup.LayoutParams(
                forgetBtn.getLayoutParams().width,
                forgetBtn.getLayoutParams().height);
    }
    private void initHint(){
        textSwitcher = (TextSwitcher)findViewById(R.id.hint);
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
//                TextView textView = new TextView(LoginActivity.this);
                LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
                TextView textView = (TextView) inflater.inflate(R.layout.item_hint , null);
                textView.setPadding(40,70,0,0);

                return textView;
            }
        });
        textSwitcher.setInAnimation(this,R.anim.fade_in_center);
        textSwitcher.setOutAnimation(this,R.anim.fade_out_center);

        textSwitcher2 = (TextSwitcher)findViewById(R.id.hint_bigger);
        textSwitcher2.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
//                TextView textView = new TextView(LoginActivity.this);
                LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
                TextView textView = (TextView) inflater.inflate(R.layout.item_hint_bigger , null);
                textView.setPadding(40,100,0,0);
                return textView;
            }
        });
        textSwitcher2.setInAnimation(this,R.anim.fade_in_center);
        textSwitcher2.setOutAnimation(this,R.anim.fade_out_center);

        textSwitcher.setText(getResources().getString(R.string.login_backup));
        textSwitcher2.setText(getResources().getString(R.string.login_backup_e));
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
            textSwitcher.setText(getResources().getString(R.string.login_signup));
            textSwitcher2.setText(getResources().getString(R.string.login_signup_e));
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
            textSwitcher.setText(getResources().getString(R.string.login_backup));
            textSwitcher2.setText(getResources().getString(R.string.login_backup_e));
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
            textSwitcher.setText(getResources().getString(R.string.login_update));
            textSwitcher2.setText(getResources().getString(R.string.login_update_e));
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
//                                    jumpToActivity(MainActivity.class);
//                                    finish();
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    CircularAnimUtil.startActivityThenFinish(
                                            LoginActivity.this,
                                            intent,
                                            false,
                                            loginBtn,R.color.green_complete,329);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                                    Snackbar.make(findViewById(R.id.login_activity), r.result_description, Snackbar.LENGTH_LONG).show();
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
                                    loginBtn.setProgress(100);
                                    try {
                                        Thread.sleep(1000);
                                        setSubmitTitle(Mode.LOGIN);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

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
        else if(mode.equals(Mode.UPDATE)){
            NetworkUtils.accountResetPassword(
                    new ResetPasswordRequest(
                            editTextUserName.getText().toString().trim(),
                            editTextPassword.getText().toString().trim(),
                            editTextCaptcha.getText().toString().trim()),
                    new Response.Listener<Result>() {
                        @Override
                        public void onResponse(Result response) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Update mode
                                    loginBtn.setProgress(100);
                                    try {
                                        Thread.sleep(1000);
                                        setSubmitTitle(Mode.LOGIN);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
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
                    });

        }
    }

}
