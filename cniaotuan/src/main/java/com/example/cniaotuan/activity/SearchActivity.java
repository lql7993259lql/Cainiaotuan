package com.example.cniaotuan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cniaotuan.R;
import com.example.cniaotuan.adapter.CommenAdapter;
import com.example.cniaotuan.adapter.ViewHolder;
import com.example.cniaotuan.listner.MyTextWatch;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity implements View.OnClickListener {
    /* Tag */
    private String TAG = "SearchActivity";
    /* 历史记录 */
    private boolean isShowHistoryClear;
    private View mHistoryClearup;       //清空历史记录
    private ListView mHistoryListView;
    private MyListViewAdapter mHistoryAdapter;
    private ArrayList<String> mHistoryData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        /* 初始化View */
        initView();
        /* 初始化ListView */
        initListView();
        /* 初始化监听 */
        initListener();
        /* 初始化数据 */
        initData();
    }
    /** 初始化View */
    private void initView(){
        mHistoryClearup = View.inflate(SearchActivity.this, R.layout.item_search_history_clearup, null);
        mHistoryClearup.findViewById(R.id.rl_history_clearup).setVisibility(View.GONE);
    }
    /** 初始化ListView */
    private void initListView(){
        mHistoryListView = (ListView) findViewById(R.id.searchHistoryListView);
        mHistoryAdapter = new MyListViewAdapter(mHistoryData);
        mHistoryListView.setAdapter(mHistoryAdapter);
        mHistoryListView.addFooterView(mHistoryClearup);
    }
    /** 初始化监听 */
    private void initListener(){
        findViewById(R.id.backSearchImageView).setOnClickListener(this);
        //搜索文本发生改变时

        EditText etSearch = (EditText) findViewById(R.id.searchContentEditText);

        etSearch.addTextChangedListener(new MyTextWatch() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s + "";
                if (text.length() > 0) {
                    findViewById(R.id.searchDeleteImageView).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.searchDeleteImageView).setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //清空搜索框内容
        findViewById(R.id.searchDeleteImageView).setOnClickListener(this);
        //搜索记录ListView
        mHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemClick " + position);
                if(position==mHistoryData.size()){
                    //清空搜索记录
                    mHistoryData.clear();
                    //隐藏布局
                    mHistoryClearup.findViewById(R.id.rl_history_clearup).setVisibility(View.GONE);
                    //刷新适配器
                    mHistoryAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    /** 初始化数据 */
    private void initData(){
        for (int i = 0; i < 10; i++) {
            mHistoryData.add(i + "");
        }
        mHistoryAdapter.notifyDataSetChanged();
    }
    /** OnClick */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backSearchImageView:
//                windowRightOut();
                break;
            case R.id.searchDeleteImageView:
                EditText et = (EditText) findViewById(R.id.searchContentEditText);
                et.setText("");
                break;
        }
    }
    /** 自定义ListView适配器  */
    class MyListViewAdapter extends CommenAdapter{

        public MyListViewAdapter(List data) {
            super(data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(!isShowHistoryClear){
                isShowHistoryClear = true;
                mHistoryClearup.findViewById(R.id.rl_history_clearup).setVisibility(View.VISIBLE);
            }
            String content = (String) mHistoryData.get(position);
            ViewHolder holder = ViewHolder.get(SearchActivity.this, R.layout.item_search_history, convertView, parent);
            ((TextView)holder.getView(R.id.historyContentTextView)).setText(content);
            return holder.getConvertView();
        }
    }
}
