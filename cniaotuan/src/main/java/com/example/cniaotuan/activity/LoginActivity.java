package com.example.cniaotuan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cniaotuan.R;
import com.example.cniaotuan.core.weibo.ISinaInfo;
import com.example.cniaotuan.core.weibo.ISinaLogin;
import com.example.cniaotuan.core.weibo.WeiboUtils;
import com.example.cniaotuan.entity.UserEvent;
import com.example.cniaotuan.listner.BmobLoginCallback;
import com.example.cniaotuan.listner.MyTextWatch;
import com.example.cniaotuan.nohttp.CallServer;
import com.example.cniaotuan.nohttp.HttpListner;
import com.example.cniaotuan.utils.BmobManager;
import com.example.cniaotuan.utils.SharedPreferencesUtils;
import com.example.cniaotuan.utils.Util;
import com.sina.weibo.sdk.openapi.models.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements HttpListner<String> {
    /**
     * 用户名密码输入框
     **/
    @InjectView(R.id.username)
    EditText mUsername;
    @InjectView(R.id.password)
    EditText mPassword;

    /**
     * 快速登录
     */
    @InjectView(R.id.tv_quick_register)
    TextView mTvQuickRegister;
    /**
     * 帐号登录
     */
    @InjectView(R.id.tv_count_register)
    TextView mTvCountRegister;

    /**
     * tab下划线
     */
    @InjectView(R.id.view_line_left)
    View mViewLineLeft;
    @InjectView(R.id.view_line_right)
    View mViewLineRight;

    /**
     * 帐号登录的布局
     */
    @InjectView(R.id.ll_login)
    LinearLayout mLlLogin;
    /**
     * 快速登录的布局
     */
    @InjectView(R.id.ll_quick_login)
    LinearLayout mLlQuickLogin;
    /***
     * 忘记密码
     **/
    @InjectView(R.id.ll_forget_pwd)
    LinearLayout mLlForgetPwd;
    @InjectView(R.id.et_quick_phone)
    EditText mQuickPhone;
    @InjectView(R.id.et_quick_code)
    EditText mQuickCode;


    @InjectView(R.id.quick_login_btn)
    Button mBtnLogin;
    @InjectView(R.id.btn_get_code)
    Button mBtnGetCode;
    @InjectView(R.id.qq_account)
    TextView mQqAccount;
    @InjectView(R.id.sina_weibo)
    TextView mSinaWeibo;
    /**
     * tab下划线动画
     */
    private Animation mAnimRight;
    private Animation mAnimLeft;
    /**
     * 字体颜色
     */
    private int mOrrange;
    private int mGray;
    /**
     * 帐号名密码是否为空
     **/
    private boolean isUsernameNull = false;
    private boolean isPwdNull = false;
    /**
     * 手机号验证码是否为空
     **/
    private boolean isPhoneNull = false;
    private boolean isCodeNull = false;
    private UserInfo mInfo = null;
    /**
     * 倒计时秒数
     */
    private int mCount;
    private static Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        ButterKnife.inject(this);
        init();
        initAnimation();
    }

    /***
     * 初始化操作
     */
    private void init() {
        WeiboUtils.initWeibo(this);
        //QQ的初始化
        mTencent = Tencent.createInstance("222222", this.getApplicationContext());
        mInfo = new UserInfo(this, mTencent.getQQToken());

        mOrrange = getResources().getColor(R.color.orange);
        mGray = getResources().getColor(R.color.content_color);

        mQuickPhone.addTextChangedListener(new MyTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                isPhoneNull = TextUtils.isEmpty(editable.toString()) ? false : true;
                mBtnLogin.setEnabled((isPhoneNull && isCodeNull) ? true : false);
            }
        });
        mQuickCode.addTextChangedListener(new MyTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                isCodeNull = TextUtils.isEmpty(editable.toString()) ? false : true;
                mBtnLogin.setEnabled((isPhoneNull && isCodeNull) ? true : false);
            }
        });

        mUsername.addTextChangedListener(new MyTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                isUsernameNull = TextUtils.isEmpty(editable.toString()) ? false : true;
                mBtnLogin.setEnabled((isUsernameNull && isPwdNull) ? true : false);
            }
        });
        mPassword.addTextChangedListener(new MyTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                isPwdNull = TextUtils.isEmpty(editable.toString()) ? false : true;
                mBtnLogin.setEnabled((isUsernameNull && isPwdNull) ? true : false);
            }
        });

    }

    /***
     * 初始化动画
     */
    private void initAnimation() {
          /* 线左边移动 */
        mAnimRight = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.view_line_move_left);
        mAnimRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mTvQuickRegister.setTextColor(mOrrange);
                mTvCountRegister.setTextColor(mGray);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewLineLeft.setVisibility(View.VISIBLE);
                mViewLineRight.setVisibility(View.INVISIBLE);
                mLlLogin.setVisibility(View.GONE);
                mLlQuickLogin.setVisibility(View.VISIBLE);
                mLlForgetPwd.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        /* 线右边移动 */
        mAnimLeft = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.view_line_move_right);
        mAnimLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mTvQuickRegister.setTextColor(mGray);
                mTvCountRegister.setTextColor(mOrrange);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewLineLeft.setVisibility(View.INVISIBLE);
                mViewLineRight.setVisibility(View.VISIBLE);
                mLlLogin.setVisibility(View.VISIBLE);
                mLlQuickLogin.setVisibility(View.GONE);
                mLlForgetPwd.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 发送验证码倒计时
     */
    private void countdownTimer() {
        mBtnGetCode.setEnabled(false);
        mCount = 60;
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCount--;
                        mBtnGetCode.setText(mCount + "");
                        if (mCount <= 0) {
                            mBtnGetCode.setText("重新发送");
                            mBtnGetCode.setEnabled(true);
                            timer.cancel();
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    /***
     * 登录操作
     */
    public void login() {
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "您的用户名或者密码为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        Request<String> request = NoHttp.createStringRequest("https://api.bmob.cn/1/users", RequestMethod.POST);
        //添加头部
        request.addHeader("X-Bmob-Application-Id", "555b31ec65459cd361ea36884fb733f7");
        request.addHeader("X-Bmob-REST-API-Key", "63df6b698ddce30d5162ce84c3bf0d75");

        //添加Body
        //{"username":"1111","password":"1111"}
        String body = "{\"username\"" + ":" + username + "\"password\"" + ":" + password;
        request.setDefineRequestBodyForJson(body);

        CallServer.getInstance().add(LoginActivity.this, 0, request, this, true, true);
    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        switch (what) {
            case 0:
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                finish();
                EventBus.getDefault().post(new UserEvent(mUsername.getText().toString()
                        , mPassword.getText().toString()));
                SharedPreferencesUtils.setParam(LoginActivity.this,"isLogin",true);
                break;
        }
    }

    @Override
    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
        switch (what) {
            case 0:
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnClick({R.id.sina_weibo, R.id.qq_account, R.id.quick_login_btn, R.id.tv_quick_register, R.id.tv_count_register, R.id.tv_register, R.id.btn_get_code})
    public void onClick(View view) {
        switch (view.getId()) {
            //帐号登录
            case R.id.quick_login_btn:
                login();
                break;
            //注册
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.tv_quick_register:
                mViewLineRight.startAnimation(mAnimRight);
                break;
            case R.id.tv_count_register:
                mViewLineLeft.startAnimation(mAnimLeft);
                break;
            //获取验证码
            case R.id.btn_get_code:
                if (TextUtils.isEmpty(mQuickPhone.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "电话号码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobManager.getInstance(new BmobLoginCallback() {
                    @Override
                    public void loginSuccess() {
                        Toast.makeText(LoginActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        countdownTimer();
                    }

                    @Override
                    public void loginFailure() {

                    }
                }).requestSMSCode("18559802120");
                break;
            //微博登录
            case R.id.sina_weibo:
                WeiboUtils.loginWeibo(this, new ISinaLogin() {
                    @Override
                    public void weiboLoginSuccess() {
                        WeiboUtils.getWeiboInfo(LoginActivity.this, new ISinaInfo() {
                            @Override
                            public void getWBInfoSuccess(User user) {
                                EventBus.getDefault().post(new UserEvent(user.screen_name
                                        , user.created_at));
                                finish();
                            }

                            @Override
                            public void getWBInfoFailure() {

                            }
                        });
                    }

                    @Override
                    public void weiboLoginFarlure() {
                    }
                });
                break;
            //QQ登录
            case R.id.qq_account:
                mTencent.login(this, "all", loginListener);
                updateUserInfo();
                break;
        }


    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {

                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    new Thread(){

                        @Override
                        public void run() {
                            JSONObject json = (JSONObject)response;
                            if(json.has("figureurl")){
                                Bitmap bitmap = null;
                                try {
                                    bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                                } catch (JSONException e) {

                                }
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        }

                    }.start();
                    finish();
                }

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        } else {
//            mUserInfo.setText("");
//            mUserInfo.setVisibility(android.view.View.GONE);
//            mUserLogo.setVisibility(android.view.View.GONE);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                SharedPreferencesUtils.setParam(LoginActivity.this,"isLogin",true);
                JSONObject response = (JSONObject) msg.obj;
//                if (response.has("nickname")) {
//                    try {
//                        mUserInfo.setVisibility(android.view.View.VISIBLE);
//                        mUserInfo.setText(response.getString("nickname"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
                String nickname = null;
                try {
                    nickname = response.getString("nickname");
                    EventBus.getDefault().post(nickname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(msg.what == 1){
                Bitmap bitmap = (Bitmap)msg.obj;
                EventBus.getDefault().post(bitmap);

//                mUserLogo.setImageBitmap(bitmap);
//                mUserLogo.setVisibility(android.view.View.VISIBLE);
            }
        }
    };








    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);
        }
    };

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            doComplete((JSONObject) response);
        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage(LoginActivity.this, "onError: " + e.errorDetail);

        }

        @Override
        public void onCancel() {
            Util.toastMessage(LoginActivity.this, "onCancel: ");

        }

        protected void doComplete(JSONObject values) {

        }
    }


    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //进行反注册
        EventBus.getDefault().unregister(this);
    }

}
