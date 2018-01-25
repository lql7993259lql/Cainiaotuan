package com.example.cniaotuan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cniaotuan.R;
import com.example.cniaotuan.adapter.CommenAdapter;
import com.example.cniaotuan.entity.GoodsPayInfo;
import com.example.cniaotuan.listner.BmobQueryOrderCallback;
import com.example.cniaotuan.utils.BmobManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.exception.BmobException;

public class OrderActivity extends AppCompatActivity {

    @InjectView(R.id.list_order)
    ListView mListOrder;
    private List<GoodsPayInfo> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.inject(this);
        final CommenAdapter<GoodsPayInfo> adapter = new CommenAdapter<GoodsPayInfo>(mData) {
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View inflate = getLayoutInflater().inflate(R.layout.order_item, null);
                TextView tvOrder = (TextView) inflate.findViewById(R.id.tv_order);
                tvOrder.setText(mData.get(i).getProduct());
                return inflate;
            }
        };
        BmobManager.getInstance(new BmobQueryOrderCallback() {
            @Override
            public void queryOrderSuccess(List<GoodsPayInfo> object) {
                Toast.makeText(OrderActivity.this, "" + object.size(), Toast.LENGTH_SHORT).show();
                mData.addAll(object);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void queryOrderFailure(BmobException e) {

            }
        }).queryOrderData("orderStatus", true);
        mListOrder.setAdapter(adapter);

    }
}
