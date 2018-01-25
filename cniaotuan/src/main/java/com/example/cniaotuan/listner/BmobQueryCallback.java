package com.example.cniaotuan.listner;

import com.example.cniaotuan.entity.FavorInfo;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by hongkl on 16/9/5.
 */
public abstract class BmobQueryCallback implements IBmobListener {
    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFailure() {

    }



    @Override
    public void queryAllSuccess(List<FavorInfo> object) {

    }

    @Override
    public void queryAllFailure(BmobException e) {

    }
}
