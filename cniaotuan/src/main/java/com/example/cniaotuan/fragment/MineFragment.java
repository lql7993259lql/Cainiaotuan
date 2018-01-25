package com.example.cniaotuan.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cniaotuan.R;
import com.example.cniaotuan.activity.CollectActivity;
import com.example.cniaotuan.activity.LoginActivity;
import com.example.cniaotuan.activity.OrderActivity;
import com.example.cniaotuan.activity.UnOrderActivity;
import com.example.cniaotuan.entity.UserEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {

    @InjectView(R.id.btn_login)
    Button mBtnLogin;
    @InjectView(R.id.rl_unlogin)
    RelativeLayout mUnLogin;
    @InjectView(R.id.iv_arrow_more)
    Button mIvArrowMore;
    @InjectView(R.id.rl_logined)
    RelativeLayout mRlLogined;
    @InjectView(R.id.tv_unlogin_notice)
    TextView mTvUnloginNotice;
    @InjectView(R.id.iv_user)
    ImageView mIvUser;
    @InjectView(R.id.tv_name)
    TextView mTvName;
    @InjectView(R.id.tv_ticket)
    TextView mTvTicket;
    @InjectView(R.id.tv_collect)
    TextView mTvCollect;
    @InjectView(R.id.tv_recent_view)
    TextView mTvRecentView;
    @InjectView(R.id.iv_arrow_right_first)
    ImageView mIvArrowRightFirst;
    @InjectView(R.id.tv_unpaied_count)
    TextView mTvUnpaiedCount;
    //待付款
    @InjectView(R.id.rl_unpaied_order)
    RelativeLayout mRlUnpaiedOrder;
    //已付款
    @InjectView(R.id.rl_paied_order)
    RelativeLayout mRlPaiedOrder;

    @InjectView(R.id.iv_paied)
    ImageView mIvPaied;
    @InjectView(R.id.iv_arrow_right_second)
    ImageView mIvArrowRightSecond;
    @InjectView(R.id.tv_uncomment_count)
    TextView mTvUncommentCount;
    @InjectView(R.id.tv_haspaied_count)
    TextView mTvHaspaiedCount;

    @InjectView(R.id.iv_arrow_right_third)
    ImageView mIvArrowRightThird;
    @InjectView(R.id.tv_lottery_count)
    TextView mTvLotteryCount;
    @InjectView(R.id.rl_lottery)
    RelativeLayout mRlLottery;
    @InjectView(R.id.tv_generdisplay_name)
    TextView mTvGenerdisplayName;
    @InjectView(R.id.iv_arrow_right_four)
    ImageView mIvArrowRightFour;
    @InjectView(R.id.tv_generdisplay_count)
    TextView mTvGenerdisplayCount;
    @InjectView(R.id.rl_generdisplay)
    RelativeLayout mRlGenerdisplay;
    @InjectView(R.id.iv_arrow_right_banck)
    ImageView mIvArrowRightBanck;
    @InjectView(R.id.tv_goto_mybank)
    TextView mTvGotoMybank;
    @InjectView(R.id.rl_bankcard)
    RelativeLayout mRlBankcard;
    @InjectView(R.id.iv_tuijian)
    ImageView mIvTuijian;
    @InjectView(R.id.iv_arrow_right_fourth)
    ImageView mIvArrowRightFourth;
    @InjectView(R.id.tv_tuijian)
    TextView mTvTuijian;
    @InjectView(R.id.rl_tuijian)
    RelativeLayout mRlTuijian;
    @InjectView(R.id.iv_diyongquan)
    ImageView mIvDiyongquan;
    @InjectView(R.id.iv_arrow_right_fifth)
    ImageView mIvArrowRightFifth;
    @InjectView(R.id.tv_coupon_count)
    TextView mTvCouponCount;
    @InjectView(R.id.rl_coupon)
    RelativeLayout mRlCoupon;
    @InjectView(R.id.scan_qr_icon)
    ImageView mScanQrIcon;
    @InjectView(R.id.iv_arrow_right)
    ImageView mIvArrowRight;
    @InjectView(R.id.scan_qr_layout)
    RelativeLayout mScanQrLayout;
    private View mInflate;
    private View mUnLoginLayout;
    private View mLoginLayout;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_mine, container, false);
            ButterKnife.inject(this, mInflate);
            //进行EventBus的注册
            EventBus.getDefault().register(this);

            mUnLoginLayout = mInflate.findViewById(R.id.rl_unlogin);
            mLoginLayout = mInflate.findViewById(R.id.rl_logined);

        }
        ButterKnife.inject(this, mInflate);
        return mInflate;
    }

    //接受订阅事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(UserEvent userEvent) {

        mUnLogin.setVisibility(View.GONE);
        mRlLogined.setVisibility(View.VISIBLE);
    }

    //接受订阅事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setQQNickname(String nickname) {
        mUnLogin.setVisibility(View.GONE);
        mRlLogined.setVisibility(View.VISIBLE);
        mTvName.setText(nickname);
    }

    //接受订阅事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setQQAvtar(Bitmap avtar) {
        mUnLogin.setVisibility(View.GONE);
        mRlLogined.setVisibility(View.VISIBLE);
        mIvUser.setImageBitmap(avtar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);

    }

    @OnClick({R.id.btn_login, R.id.tv_collect, R.id.tv_recent_view, R.id.iv_arrow_more,R.id.rl_unpaied_order,R.id.rl_paied_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;

            case R.id.tv_collect:
                startActivity(new Intent(getActivity(), CollectActivity.class));

                break;
            case R.id.tv_recent_view:
                break;
            case R.id.iv_arrow_more:
                BmobUser.logOut();   //清除缓存用户对象
                mUnLoginLayout.setVisibility(View.VISIBLE);
                mLoginLayout.setVisibility(View.GONE);
                break;
            case R.id.rl_unpaied_order:

                startActivity(new Intent(getActivity(), UnOrderActivity.class));
                break;
            case R.id.rl_paied_order:
                startActivity(new Intent(getActivity(), OrderActivity.class));
                break;
        }
    }


}
