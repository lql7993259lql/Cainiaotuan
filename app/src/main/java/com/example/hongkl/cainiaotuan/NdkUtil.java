package com.example.hongkl.cainiaotuan;

/**
 * Created by Administrator on 2018/2/8.
 */
//ndk环境配置
//https://www.cnblogs.com/ut2016-progam/p/6066855.html
//http://blog.csdn.net/aplixy/article/details/51429305
public class NdkUtil {
    static {
        System.loadLibrary("myNativeLib");//导入生成的链接库文件
    }
    public native String getStringFromNative();
}
