package com.example.hongkl.cainiaotuan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();
        NdkUtil n = new NdkUtil();
        System.out.println("================================>>>"+n.getStringFromNative()+"");
//        toolbar.setTitle("asdas");
//        toolbar.setSubtitle("SubTitle");
//        toolbar.setLogo(R.mipmap.sss);

        //设置导航图标要在setSupportActionBar方法之后
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "click NavigationIcon", Toast.LENGTH_SHORT).show();
//            }
//        });
//        ActionBar actionBar =  getSupportActionBar();
//        if(actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(false);
//        }
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.right_menu, menu);
//        LinearLayout badgeLayout = (LinearLayout) menu.findItem(R.id.action_btn).getActionView();
//        Button btn = (Button) badgeLayout.findViewById(R.id.button1);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Search123 !", Toast.LENGTH_SHORT).show();
//            }
//        });
//        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
//        MyButton searchView = (MyButton) MenuItemCompat.getActionView(menuItem);
//
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Search !", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//
//            case R.id.action_btn:
//                Toast.makeText(MainActivity.this, "Search123 !", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.action_search:
//                Toast.makeText(MainActivity.this, "Search !", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.action_notifications:
//                Toast.makeText(MainActivity.this, "Notificationa !", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.action_settings:
//                Toast.makeText(MainActivity.this, "Settings !", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return true;
//
//
//    }


    private void setToolbar() {
        toolbar.setTitle("凤凰卫士");   //设置标题
        toolbar.setSubtitle("新浪消息");    //设置副标题
        toolbar.setSubtitleTextColor(Color.WHITE);  //设置副标题字体颜色
        setSupportActionBar(toolbar);   //必须使用
        //添加左边图标点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //添加menu项点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_r_img:
                        Toast.makeText(MainActivity.this, "点击了右边图标", Toast.LENGTH_SHORT).show();
//                        Log.e("Test---->","点击了右边图标");
                        break;
                    case R.id.toolbar_r_1:
                        Toast.makeText(MainActivity.this, "点击了弹出菜单1", Toast.LENGTH_SHORT).show();
//                        Log.e("Test---->","点击了弹出菜单1");
                        break;
                    case R.id.toolbar_r_2:
                        Toast.makeText(MainActivity.this, "点击了弹出菜单2", Toast.LENGTH_SHORT).show();
//                        Log.e("Test---->","点击了弹出菜单2");
                        break;
                    case R.id.toolbar_r_3:
                        Toast.makeText(MainActivity.this, "点击了弹出菜单3", Toast.LENGTH_SHORT).show();
//                        Log.e("Test---->","点击了弹出菜单3");
                        break;
                }
                return true;    //返回为true
            }
        });
    }
    //设置menu（右边图标）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); //解析menu布局文件到menu
        return true;
    }


}
