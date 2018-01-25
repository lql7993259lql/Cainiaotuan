package com.example.cniaotuan.listner;

import com.example.cniaotuan.entity.FavorInfo;
import com.example.cniaotuan.entity.GoodsPayInfo;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by hongkl on 16/9/5.
 */
public interface IBmobListener {

    void loginSuccess();
    void loginFailure();
    void querySuccess(FavorInfo object);
    void queryFailure(BmobException e);
    void queryAllSuccess(List<FavorInfo> object);
    void queryAllFailure(BmobException e);
    void queryOrderSuccess(List<GoodsPayInfo> object);
    void queryOrderFailure(BmobException e);

}
