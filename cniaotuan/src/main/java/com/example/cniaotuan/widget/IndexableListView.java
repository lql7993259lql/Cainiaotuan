package com.example.cniaotuan.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 封装了右侧的牵引条及其相关的动作
 */
public class IndexableListView extends ListView {
    /* 是否快速牵引 */
    private boolean mIsFastScrollEnabled;
    /* 负责右侧牵引条 */
    private IndexScroller mScroller;

    public IndexableListView(Context context) {
        super(context);
    }

    public IndexableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isFastScrollEanbled(){
        return mIsFastScrollEnabled;
    }

    /** 重写主要用来绘制右侧索引条 */
    @Override
    public void setFastScrollEnabled(boolean enabled) {
        mIsFastScrollEnabled = enabled;
        //如果允许快速滚动的话
        if(enabled){
            if(mScroller == null){
                // 创建IndexScroll对象
                mScroller = new IndexScroller(getContext(),this);
            }
        }else{
            if(mScroller != null){
                mScroller = null;
            }
        }
    }

    /** 在绘制listView时，绘制右侧的牵引条 */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);       //不可少，用于绘制原来的
        if(mScroller != null){
            // 绘制右侧的索引条
            mScroller.draw(canvas);
        }
    }

    //监听

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //如果mScorll自己来处理触摸事件，该方法返回true
        //处理触摸索引条的事件
        if(mScroller != null && mScroller.onTouchEvent(ev)){
            return true;
        }
        return super.onTouchEvent(ev);
    }
    //点击位置拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mScroller.contains(ev.getX(), ev.getY()))
            return true;

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if(mScroller != null){
            mScroller.setAdapter(adapter);
        }
    }

    /** ListView大小发生改变，一般出现在切换横竖屏的时候 */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mScroller != null){
            //ListView大小发生变化时，索引条也发生变化
            mScroller.onSizeChanged(w,h,oldw,oldh);
        }
    }
}
