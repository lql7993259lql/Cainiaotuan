package com.example.hongkl.cainiaotuan;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("asdas");
//        toolbar.setSubtitle("SubTitle");
//        toolbar.setLogo(R.mipmap.sss);

        //设置导航图标要在setSupportActionBar方法之后
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "click NavigationIcon", Toast.LENGTH_SHORT).show();
//            }
//        });
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        MyButton searchView = (MyButton) MenuItemCompat.getActionView(menuItem);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Search !", Toast.LENGTH_SHORT).show();

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search:
                Toast.makeText(MainActivity.this, "Search !", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.action_notifications:
//                Toast.makeText(MainActivity.this, "Notificationa !", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.action_settings:
//                Toast.makeText(MainActivity.this, "Settings !", Toast.LENGTH_SHORT).show();
//                break;
        }
        return true;


    }
}
