package com.example.cniaotuan.core.weibo;

import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by Administrator on 0003.
 */
public interface ISinaInfo {
    //获取微博信息成功
    void getWBInfoSuccess(User user);
    //获取微博信息失败
    void getWBInfoFailure();

}
