package com.example.cniaotuan.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cniaotuan.R;
import com.example.cniaotuan.bean.Config;
import com.example.cniaotuan.entity.DetailInfo;
import com.example.cniaotuan.entity.FavorInfo;
import com.example.cniaotuan.entity.GoodsPayInfo;
import com.example.cniaotuan.listner.BmobQueryAllCallback;
import com.example.cniaotuan.nohttp.CallServer;
import com.example.cniaotuan.nohttp.HttpListner;
import com.example.cniaotuan.utils.BmobManager;
import com.example.cniaotuan.utils.SharedPreferencesUtils;
import com.example.cniaotuan.widget.ObservableScrollView;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.exception.BmobException;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


public class DetailActivity extends AppCompatActivity implements HttpListner<String>, ObservableScrollView.ScrollViewListener {

    @InjectView(R.id.iv_detail)
    ImageView mIvDetail;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    @InjectView(R.id.tv_decs)
    TextView mTvDecs;
    @InjectView(R.id.textView)
    TextView mTextView;
    @InjectView(R.id.tv_bought)
    TextView mTvBought;
    @InjectView(R.id.tv_title2)
    TextView mTvTitle2;
    @InjectView(R.id.tv_address)
    TextView mTvAddress;
    @InjectView(R.id.tv_time)
    TextView mTvTime;
    @InjectView(R.id.web_detail)
    WebView mWebDetail;
    @InjectView(R.id.web_notice)
    WebView mWebNotice;
    @InjectView(R.id.list_recommend)
    ListView mListRecommend;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.iv_favor)
    ImageView mIvFavor;
    @InjectView(R.id.iv_share)
    ImageView mIvShare;
    @InjectView(R.id.tv_price)
    TextView mTvPrice;
    @InjectView(R.id.tv_value)
    TextView mTvValue;
    @InjectView(R.id.btn_buy)
    Button mBtnBuy;

    @InjectView(R.id.layout_title)
    RelativeLayout mLayoutTitle;
    @InjectView(R.id.layout_buy)
    RelativeLayout mLayoutBuy;

    //titlebar的标题
    @InjectView(R.id.tv_titlebar)
    TextView mTvTitlebar;
    //自定义的scrollView
    @InjectView(R.id.scrollView)
    ObservableScrollView mScrollView;

    private DetailInfo mDetailInfo;
    private int mImageHeight;
    private boolean isFavor;
    private String mGoods_id;
    private String mObjID;
    private int mBought;
    ProgressDialog dialog;

    private String orderID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        mGoods_id = intent.getStringExtra("goods_id");
        mBought = intent.getIntExtra("bought", 0);
        /**商品详情的请求**/
        Request<String> recommendRequest = NoHttp.createStringRequest(Config.baseUrl + mGoods_id + ".txt", RequestMethod.GET);
        CallServer.getInstance().add(this, 0, recommendRequest, this, true, true);

        initData();
        initListener();

    }

    /***
     * 初始化数据
     */
    private void initData() {
        BmobManager.getInstance(new BmobQueryAllCallback() {
            @Override
            public void queryAllSuccess(List<FavorInfo> object) {
                boolean favor = object.get(0).isFavor();
                mIvFavor.setImageResource(favor ? R.drawable.icon_collected_black : R.drawable.icon_uncollect_black);
                isFavor = favor?true:false;
                mObjID = object.get(0).getObjectId();
            }

            @Override
            public void queryAllFailure(BmobException e) {

            }
        }).queryAllData("goods_id", mGoods_id);

    }

    /***
     * 获取顶部的图片高度,设置滚动监听
     */
    private void initListener() {
        ViewTreeObserver vto = mIvDetail.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mIvDetail.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                mImageHeight = mIvDetail.getHeight();

                mScrollView.setScrollViewListener(DetailActivity.this);
            }
        });
    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        switch (what) {
            case 0:
                Gson gson = new Gson();
                mDetailInfo = gson.fromJson(response.get(), DetailInfo.class);
                //本单详情的网页信息
                String details = mDetailInfo.getResult().getDetails();
                //温馨提示
                String notice = mDetailInfo.getResult().getNotice();

                mWebDetail.loadDataWithBaseURL("about:blank", details, "text/html", "UTF-8", null);
                mWebNotice.loadDataWithBaseURL("about:blank", notice, "text/html", "UTF-8", null);
                //标题
                mTvTitle.setText(mDetailInfo.getResult().getProduct());
                //描述
                mTvDecs.setText(mDetailInfo.getResult().getTitle());
                //已售
                mTvBought.setText(""+mBought);
                //详情界面的图片
                Uri uri = Uri.parse(mDetailInfo.getResult().getImages().get(0).getImage());
                mIvDetail.setImageURI(uri);
                mTvTitle2.setText(mDetailInfo.getResult().getProduct());
                mTvPrice.setText(mDetailInfo.getResult().getPrice());
                mTvValue.setText(mDetailInfo.getResult().getValue());
                mTvValue.setPaintFlags(mTvValue.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                break;
        }
    }

    @Override
    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

    }

    @OnClick({R.id.iv_detail, R.id.iv_back, R.id.iv_favor, R.id.iv_share, R.id.btn_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_detail:

                Intent intent = new Intent(DetailActivity.this, ImageGalleryActivity.class);
                intent.putExtra("detailInfo", mDetailInfo);
                startActivity(intent);

                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_favor:
                FavorInfo favorInfo = new FavorInfo();
                favorInfo.setGoods_id(mDetailInfo.getResult().getGoods_id());
                favorInfo.setFavor(true);
                favorInfo.setPruduct(mDetailInfo.getResult().getProduct());
                favorInfo.setPrice(mDetailInfo.getResult().getPrice());
                favorInfo.setValue(mDetailInfo.getResult().getValue());
                favorInfo.setImageUrl(mDetailInfo.getResult().getImages().get(0).getImage());
                if (!isFavor) {
                    Toast.makeText(DetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    mIvFavor.setImageResource(R.drawable.icon_collected_black);
                    BmobManager.getInstance(null).insertData(favorInfo);
                    isFavor = true;
                } else {
                    Toast.makeText(DetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    mIvFavor.setImageResource(R.drawable.icon_uncollect_black);
                    BmobManager.getInstance(null).deleteData(favorInfo);
                    isFavor = false;
                }

                break;
            case R.id.iv_share:
                showShare();
                break;
            //立即购买
            case R.id.btn_buy:
                //用户是否登录
                Boolean isLogin = (Boolean) SharedPreferencesUtils.getParam(DetailActivity.this, "isLogin", false);
                
                if (isLogin){
                    pay(false);
                }else{
                    startActivity(new Intent(DetailActivity.this,LoginActivity.class));
                }
                break;
        }
    }


    /**
     * 调用支付
     *
     * @param alipayOrWechatPay
     *            支付类型，true为支付宝支付,false为微信支付
     */
    void pay(final boolean alipayOrWechatPay) {
        showDialog("正在获取订单...");

        final String product = mDetailInfo.getResult().getProduct();

        final String price = mDetailInfo.getResult().getPrice();
        BP.pay(product, product, 0.1, alipayOrWechatPay, new PListener() {

            public String orderID;

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(DetailActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
//                tv.append(name + "'s pay status is unknow\n\n");
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(DetailActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
//                tv.append(name + "'s pay status is success\n\n");
                BmobManager.getInstance(null).insertData(
                        new GoodsPayInfo(orderID,product,
                                mDetailInfo.getResult().getDetail_imags().get(0),true,price));
                hideDialog();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
//                order.setText(orderId);
//                tv.append(name + "'s orderid is " + orderId + "\n\n");
                this.orderID = orderId;
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                BmobManager.getInstance(null).insertData(
                        new GoodsPayInfo(orderID,product,
                                mDetailInfo.getResult().getDetail_imags().get(0),false,price));
                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Toast.makeText(
                            DetailActivity.this,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
                    installBmobPayPlugin("bp.db");
                } else {
                    Toast.makeText(DetailActivity.this, "支付中断!", Toast.LENGTH_SHORT)
                            .show();
                }
//                tv.append(name + "'s pay status is fail, error code is \n"
//                        + code + " ,reason is " + reason + "\n\n");
                hideDialog();
            }
        });
    }

    //显示对话框
    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    //隐藏对话框
    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }

    //安装插件
    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    //点击分享
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            mTvTitlebar.setVisibility(View.GONE);
            mLayoutTitle.setBackgroundColor(Color.argb(0, 0, 0, 0));
        } else if (y > 0 && y <= mImageHeight) {
            float scale = (float) y / mImageHeight;
            float alpha = (255 * scale);
            mTvTitlebar.setVisibility(View.VISIBLE);
            mTvTitlebar.setText(mDetailInfo.getResult().getProduct());
            mTvTitlebar.setTextColor(Color.argb((int) alpha, 0, 0, 0));
            mLayoutTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
        } else {
            mTvTitlebar.setVisibility(View.VISIBLE);
            mTvTitlebar.setText(mDetailInfo.getResult().getProduct());
            mTvTitlebar.setTextColor(Color.argb(0, 0, 0, 0));
            mLayoutTitle.setBackgroundColor(Color.argb(255, 255, 255, 255));
        }
    }
}
