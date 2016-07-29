package cn.crepusculo.subway_ticket_android.ui.activity.login;

import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.subwayticket.model.request.LoginRequest;
import com.subwayticket.model.request.PhoneCaptchaRequest;
import com.subwayticket.model.request.RegisterRequest;
import com.subwayticket.model.result.MobileLoginResult;
import com.subwayticket.model.result.Result;

import java.io.UnsupportedEncodingException;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity;
import cn.crepusculo.subway_ticket_android.utils.GsonUtils;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;

public class LoginActivity<T> extends BaseActivity {
    private static class Mode {
        public static String REGISTER = "register";
        public static String LOGIN = "login";
        public static String UPDATE = "update";
        public static String CAPTCHA = "captcha";

        private Mode() {}
    }
    ViewGroup.LayoutParams buttonSize;

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
        buttonSize = new ViewGroup.LayoutParams(
                forgetBtn.getLayoutParams().width,
                forgetBtn.getLayoutParams().height);
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
                                            }, 1500);
                                        }
                                    }, 1500);
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
                                    Snackbar.make(findViewById(R.id.login_activity),r.result_description,Snackbar.LENGTH_LONG).show();
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
        hideView(signBtn);hideView(forgetBtn);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showView(signBtn);
                showView(forgetBtn);
            }
        },2000);
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
        } else if (mode.equals(Mode.UPDATE)) {
            loginBtn.setText(R.string.login_update);
        } else {
            loginBtn.setText(R.string.login_signup);
        }
    }

    private void setSubmitTitle(String mode) {

        loginBtn.setProgress(0);
        setSubmitTitle();

        if (mode.equals(Mode.CAPTCHA)) {
            /**
             * Captcha - pwdEditText
             */
            editTextPassword.setVisibility(View.VISIBLE);
            /**
             * Captcha - captchaEditText
             */
            editTextCaptcha.setVisibility(View.INVISIBLE);
            /**
             * Captcha - forgetBtn
             */
            forgetBtn.setVisibility(View.INVISIBLE);
            /**
             * Captcha - signUpBtn
             */
            signBtn.setVisibility(View.INVISIBLE);

        } // == END IF == captcha
        else if (mode.equals(Mode.REGISTER)) {
            /**
             * Captcha - pwdEditText
             */
            editTextPassword.setVisibility(View.VISIBLE);
            /**
             * Captcha - captchaEditText
             */
            ViewGroup.LayoutParams lp = editTextUserName.getLayoutParams();
            editTextCaptcha.setLayoutParams(lp);
            editTextCaptcha.setVisibility(View.VISIBLE);
            /**
             * Captcha - forgetBtn
             */
            ViewGroup.LayoutParams btz = buttonSize;
            btz.height = 0;
            forgetBtn.setVisibility(View.INVISIBLE);
            forgetBtn.setLayoutParams(btz);
            /**
             * Captcha - signUpBtn
             */
            signBtn.setVisibility(View.INVISIBLE);
            signBtn.setLayoutParams(btz);
        } // == END IF == register
        else if (mode.equals(Mode.LOGIN)) {
            /**
             * Captcha - pwdEditText
             */
            editTextPassword.setVisibility(View.VISIBLE);
            /**
             * Captcha - captchaEditText
             */
            editTextCaptcha.setVisibility(View.VISIBLE);
            /**
             * Captcha - forgetBtn
             */
            forgetBtn.setVisibility(View.VISIBLE);
            /**
             * Captcha - signUpBtn
             */
            signBtn.setVisibility(View.INVISIBLE);
        } // == END IF == login
        else if (mode.equals(Mode.UPDATE)) {
            /**
             * Captcha - pwdEditText
             */
            editTextPassword.setVisibility(View.VISIBLE);
            /**
             * Captcha - captchaEditText
             */
            editTextCaptcha.setVisibility(View.VISIBLE);
            /**
             * Captcha - forgetBtn
             */
            forgetBtn.setVisibility(View.INVISIBLE);
            /**
             * Captcha - signUpBtn
             */
            signBtn.setVisibility(View.INVISIBLE);
        } // == END IF == update
        else {
        } // == END ELSE == else

    }

    protected void showView(View v){
        if(v.getClass().toString().equals(forgetBtn.getClass().toString())){
            // if is btn
            ViewGroup.LayoutParams lp = new AppBarLayout.LayoutParams(buttonSize.width,buttonSize.height);
            v.setLayoutParams(lp);
        }else {
            // if is edit text
            ViewGroup.LayoutParams lp = new AppBarLayout.LayoutParams(
                    editTextUserName.getLayoutParams().width,
                    editTextUserName.getLayoutParams().height);
            v.setLayoutParams(lp);
        }

    }

    protected void hideView(View v){
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = 0;
        v.setLayoutParams(lp);
    }
}
