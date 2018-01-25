package com.example.cniaotuan;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.tauth.Tencent;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.yolanda.nohttp.NoHttp;

import c.b.BP;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by hongkl on 16/8/1.
 */
public class MyApplication extends Application {
    private boolean flag = true;
    private Tencent mTencent;

    @Override
    public void onCreate() {

        super.onCreate();
        //NoHttp初始化配置
        NoHttp.initialize(this);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        //Fresco图片加载框架初始化
        Fresco.initialize(this);

        //bmob的初始化
        Bmob.initialize(this, "555b31ec65459cd361ea36884fb733f7");

        if (flag == true) {
            flag = false;
            BmobUpdateAgent.initAppVersion();
        }
        /**
         * 初始化尺寸工具类
         */
        ZXingLibrary.initDisplayOpinion(this);

        BP.init(getApplicationContext(),"555b31ec65459cd361ea36884fb733f7");

    }


}
