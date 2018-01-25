package com.example.hongkl.cainiaotuan;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Administrator on 2018/1/24.
 */

public class MyButton extends Button {

    public MyButton(Context context) {
        super(context);
        init();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        setBackgroundColor(Color.RED);
        setText("你好");
        setClickable(true);
        setFocusable(true);
    }


}
