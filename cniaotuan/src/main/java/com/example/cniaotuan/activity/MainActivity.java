package com.example.cniaotuan.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cniaotuan.R;
import com.example.cniaotuan.bean.Config;
import com.example.cniaotuan.fragment.AroundFragment;
import com.example.cniaotuan.fragment.MainFragment;
import com.example.cniaotuan.fragment.MineFragment;
import com.example.cniaotuan.fragment.MoreFragment;
import com.example.cniaotuan.utils.DataCleanManager;
import com.igexin.sdk.PushManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements Config {
    @InjectView(R.id.tabHost)
    FragmentTabHost mTabHost;
    private Class[] fragments = new Class[]{
            MainFragment.class,
            AroundFragment.class,
            MineFragment.class,
            MoreFragment.class};
    private int[] imgRes = new int[]{
            R.drawable.ic_tab_artists_selector,
            R.drawable.ic_tab_albums_selector,
            R.drawable.ic_tab_songs_selector,
            R.drawable.ic_tab_playlists_selector
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //个推的初始化
        PushManager.getInstance().initialize(this.getApplicationContext());
        ButterKnife.inject(this);
        initFragmentTabhost();
    }


    private void initFragmentTabhost() {
        //初始化
        mTabHost.setup(MainActivity.this, getSupportFragmentManager(), android.R.id.tabcontent);

        for (int i = 0; i < title.length; i++) {
            View inflate = getLayoutInflater().inflate(R.layout.tab_item, null);
            ImageView ivTab = (ImageView) inflate.findViewById(R.id.iv_tab);
            TextView tvTab = (TextView) inflate.findViewById(R.id.tv_tab);
            ivTab.setImageResource(imgRes[i]);
            tvTab.setText(title[i]);
            mTabHost.addTab(mTabHost.newTabSpec(Config.title[i]).setIndicator(inflate), fragments[i], null);
        }
    }
    /** 最后一次点击返回键的时间  */
    public long lastBackTimer;
    /** 按键消息拦截 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long timer = System.currentTimeMillis() - lastBackTimer;
                lastBackTimer = System.currentTimeMillis();
                if(timer >= 1000){
                    Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void setTab(int tabs){
        mTabHost.setCurrentTab(tabs);
    }

    @Override
    protected void onDestroy() {
        DataCleanManager.cleanSharedPreference(this);
        super.onDestroy();
    }
}
