package com.example.cniaotuan.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cniaotuan.R;


/**
 * Activity工具类
 */
public class ActivityUtils extends Activity {
    /** 用于普通View 快速牵引 */
    private SparseArray<View> mViews;
    /** Toast */
    private Toast mToast;

    /** 初始化 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 初始化View快速牵引 */
        mViews = new SparseArray<View>();
    }

    /** 启动Activity并启动动画 */
    public void startWindow(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.window_left_in, R.anim.window_left_out);
    }
    /** 启动Activity并启动动画 */
    public void startWindow(Class<?> cls){
        startActivity(new Intent(this,cls));
        overridePendingTransition(R.anim.window_left_in, R.anim.window_left_out);
    }
    /** 启动Activity并启动动画 */
    public void startWindow(Intent intent,int requestCode){
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.window_left_in, R.anim.window_left_out);
    }
    /** 退出Activity并启动动画 */
    public void windowRightOut() {
        finish();
        overridePendingTransition(R.anim.window_right_in, R.anim.window_right_out);
    }
    /** Back键拦截 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                windowRightOut();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    /** 快速牵引找到本窗口指定View */
    public <T extends View> T getView(int viewID){
        View view = mViews.get(viewID);
        if(view==null){
            view = findViewById(viewID);
            /* 放入牵引Map,增强效率 */
            mViews.put(viewID,view);
        }
        return (T) view;
    }
    /** 快速得到EditText对象 */
    public EditText getViewEt(int viewID){
        View view = mViews.get(viewID);
        if(view==null){
            view = findViewById(viewID);
            /* 放入牵引Map,增强效率 */
            mViews.put(viewID,view);
        }
        return (EditText) view;
    }
    /** 快速得到extView对象 */
    public TextView getViewTv(int viewID){
        View view = mViews.get(viewID);
        if(view==null){
            view = findViewById(viewID);
            /* 放入牵引Map,增强效率 */
            mViews.put(viewID,view);
        }
        return (TextView) view;
    }
    /** 自定义EditText内容改变监听 */
    public class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    /** Toast */
    public void toast(String message){
        if(mToast!=null){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
