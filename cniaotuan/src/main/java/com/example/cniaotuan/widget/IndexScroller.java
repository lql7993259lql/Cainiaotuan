/*
 * Copyright 2011 woozzu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cniaotuan.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class IndexScroller {
	
	private float mIndexbarWidth;		// 索引条的宽度
	private float mIndexbarMargin;		// 索引条距离右侧边缘的距离
	private float mPreviewPadding;		// 中间显示预览文本 与四周的距离
	private float mDensity;				// 当前屏幕密度 / 160
	private float mScaledDensity;		// 当前屏幕密度除以160（设置字体的尺寸）
	private int mListViewWidth;				// 当前ListVIew的 宽度
	private int mListViewHeight;			// 当前ListView的 高度
	private int mCurrentSection = -1;		// 索引
	private boolean mIsIndexing = false;
	private ListView mListView = null;			//牵引条对应的ListView
	private SectionIndexer mIndexer = null;		//ListView实现接口SectionIndexer 用来操作里面的方法
	private String[] mSections = null;			//右侧牵引列表的文本
	private RectF mIndexbarRect;				//整个索引条的区域

	// 缩影条初始化尺寸本地化
	public IndexScroller(Context context, ListView lv) {
		//当前屏幕的密度 假设当前屏幕密度是 320 / 160 mDensity = 2
		mDensity = context.getResources().getDisplayMetrics().density;
		//与字体有关的密度比值
		mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		//ListView
		mListView = lv;
		setAdapter(mListView.getAdapter());
		//根据屏幕密度设置牵引条的宽度 (单位：像素)
		mIndexbarWidth = 20 * mDensity;
		mIndexbarMargin = 5 * mDensity;	  //距离右侧边缘的距离
		mPreviewPadding = 5 * mDensity;		  //中间显示预览文本 与四周的距离
	}

	// 绘制牵引条与预览文本
	public void draw(Canvas canvas) {
		// 1.绘制牵引条，包括牵引条的背景和文本
		// 2.绘制预览文本和背景

		//设置索引条背景的绘制属性
		Paint indexbarPaint = new Paint();
		indexbarPaint.setColor(Color.BLACK);
		indexbarPaint.setAlpha(64);		//背景透明度
		indexbarPaint.setAntiAlias(true);

		//绘制索引条(四个角都是圆角的矩形区域)
		canvas.drawRoundRect(
				mIndexbarRect,		//矩形区
				3 * mDensity,  		//圆角度
				3 * mDensity,		//圆角度
				indexbarPaint
				);

		//绘制Sections
		if(mSections != null && mSections.length > 0){
			//绘制中间预览文本和背景
			if(mCurrentSection >= 0){
				//预览文本背景
				Paint previewPaint = new Paint();
				previewPaint.setColor(Color.BLACK);
				previewPaint.setAlpha(96);
				previewPaint.setAntiAlias(true);
				//文本
				Paint previewTextPaint = new Paint();
				previewTextPaint.setColor(Color.WHITE);
				previewTextPaint.setAntiAlias(true);
				previewTextPaint.setTextSize(50 * mScaledDensity);
				//文本的宽度
				float previewTextWidth = previewTextPaint.measureText(mSections[mCurrentSection]);
				//预览框的尺寸 2 * 四周间距 + 文本高度
				float previewSize = 2 * mPreviewPadding + previewTextPaint.descent() - previewTextPaint.ascent();
				// 预览文本的矩形区
				RectF previewRect = new RectF(
						(mListViewWidth - previewSize) / 2		//left
						, (mListViewHeight - previewSize) / 2	//top
						, (mListViewWidth - previewSize) / 2 + previewSize		//right
						, (mListViewHeight - previewSize) / 2 + previewSize);	//bottom
				// 绘制背景
				canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity, previewPaint);
				// 绘制预览文本 基于 背景
				canvas.drawText(
						mSections[mCurrentSection],
						previewRect.left + (previewSize - previewTextWidth) / 2 - 1,
						previewRect.top + mPreviewPadding - previewTextPaint.ascent() + 1,
						previewTextPaint);
			}

			// 设置索引的绘制属性
			Paint indexPaint = new Paint();
			indexPaint.setColor(Color.WHITE);
			indexPaint.setAlpha(255);
			indexPaint.setAntiAlias(true);
			indexPaint.setTextSize(12 * mScaledDensity);

			float sectionHeight = (mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length;
			float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;
			for (int i = 0; i < mSections.length; i++) {
				float paddingLeft = (mIndexbarWidth - indexPaint.measureText(mSections[i])) / 2;
				//绘制索引条上的文字
				canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft, mIndexbarRect.top + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint);
			}
		}


	}
	/** 管理触摸索引条的触摸事件方法 */
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:		//按下
			// 如果没有隐藏 并且 按到牵引项目
			if (contains(ev.getX(), ev.getY())) {
				// It demonstrates that the motion event started from index bar
				mIsIndexing = true;
				// 通过触摸点获取当前的Section的牵引
				mCurrentSection = getSectionByPoint(ev.getY());
				// 将ListView定位到指定的iten
				mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mIsIndexing) {
				// If this event moves inside index bar
				if (contains(ev.getX(), ev.getY())) {
					// 通过触摸点获取当前的Section的牵引
					mCurrentSection = getSectionByPoint(ev.getY());
					// 将ListView定位到指定的iten
					mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mIsIndexing) {
				mIsIndexing = false;
				mCurrentSection = -1;
			}
			break;
		}
		return false;
	}
	//索引条 ListView大小改变时，索引条大小
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		//如果已经有大小了，不再重新改变
		if(mListViewWidth==0 || mListViewHeight==0){
			mListViewWidth = w;
			mListViewHeight = h;
			mIndexbarRect = new RectF(w - mIndexbarMargin - mIndexbarWidth
					, mIndexbarMargin
					, w - mIndexbarMargin
					, h - mIndexbarMargin);
		}

	}

	/** 通过Adapter 得到成员变量 */
	public void setAdapter(Adapter adapter) {
		if (adapter instanceof SectionIndexer) {
			mIndexer = (SectionIndexer) adapter;		//向上造型，mIndex就可以使用里面的方法
			mSections = (String[]) mIndexer.getSections();
		}
	}

	public boolean contains(float x, float y) {
		// 确定该点是在索引栏区，其中包括栏右侧缘
		return (x >= mIndexbarRect.left && y >= mIndexbarRect.top && y <= mIndexbarRect.top + mIndexbarRect.height());
	}
	
	private int getSectionByPoint(float y) {
		if (mSections == null || mSections.length == 0)
			return 0;
		if (y < mIndexbarRect.top + mIndexbarMargin)
			return 0;
		if (y >= mIndexbarRect.top + mIndexbarRect.height() - mIndexbarMargin)
			return mSections.length - 1;
		return (int) ((y - mIndexbarRect.top - mIndexbarMargin) / ((mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length));
	}


}
