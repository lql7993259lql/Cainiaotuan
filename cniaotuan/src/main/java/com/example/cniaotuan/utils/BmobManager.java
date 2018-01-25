package com.example.cniaotuan.utils;

import android.util.Log;
import android.view.View;

import com.example.cniaotuan.entity.BaseModel;
import com.example.cniaotuan.entity.FavorInfo;
import com.example.cniaotuan.entity.GoodsPayInfo;
import com.example.cniaotuan.entity.MyUser;
import com.example.cniaotuan.listner.IBmobListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hongkl on 16/9/5.
 * 单例模式
 */
final public class BmobManager {

    private static BmobManager manager = null;
    private static String objID;

    private static IBmobListener mListener;

    public synchronized static BmobManager getInstance(IBmobListener listener) {
        mListener = listener;
        if (manager == null) {
            return new BmobManager();
        }
        return manager;
    }


    public void requestSMSCode(String phoneNumber) {
        BmobSMS.requestSMSCode(phoneNumber, "xiaokge", new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {//验证码发送成功

                }
            }
        });
    }

    public void loginBySMSCode(String phoneNumber, String code) {
        BmobUser.loginBySMSCode("11位手机号码", code, new LogInListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                if (user != null) {
                    Log.i("smile", "用户登陆成功");
                }
            }
        });
    }


//    public void initListener(IBmobListener listener){
//        mListener = listener;
//    }

    public void insertData(BaseModel model) {
        model.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    objID = objectId;
                } else {

                }
            }
        });
    }

    public void deleteData(BaseModel model) {
        model.setObjectId(objID);
        Log.e("objID", "objID: " + objID);
        model.delete(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {

                } else {

                }
            }
        });

    }

    public void updateData(View view) {

    }

    /***
     * 根据ID查询数据
     *
     * @param model
     */
    public void queryData(FavorInfo model) {
        BmobQuery<FavorInfo> query = new BmobQuery<>();
        query.getObject(objID, new QueryListener<FavorInfo>() {

            @Override
            public void done(FavorInfo object, BmobException e) {
                if (e == null) {
                    if (mListener != null) {

                        mListener.querySuccess(object);
                    }
                } else {
                    if (mListener != null) {
                        mListener.queryFailure(e);
                    }
                }
            }

        });
    }


    public void queryAllData(String queryKey, Object queryValue) {
        BmobQuery<FavorInfo> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo(queryKey, queryValue);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<FavorInfo>() {
            @Override
            public void done(List<FavorInfo> object, BmobException e) {
                if (e == null) {
                    if (mListener != null) {
                        mListener.queryAllSuccess(object);
                    }
                } else {
                    if (mListener != null) {
                        mListener.queryAllFailure(e);
                    }
                }
            }
        });
    }


    public void queryOrderData(String queryKey, Object queryValue) {
        BmobQuery<GoodsPayInfo> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo(queryKey, queryValue);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<GoodsPayInfo>() {
            @Override
            public void done(List<GoodsPayInfo> object, BmobException e) {
                if (e == null) {
                    if (mListener != null) {
                        mListener.queryOrderSuccess(object);
                    }
                } else {
                    if (mListener != null) {
                        mListener.queryOrderFailure(e);
                    }
                }
            }
        });
    }

}
