package com.example.qgchat.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.qgchat.R;
import com.example.qgchat.adapter.DrawerAdapter;
import com.example.qgchat.adapter.MainViewPageFragmentAdapter;
import com.example.qgchat.addfriend.AtyAddFriend;
import com.example.qgchat.bean.DrawerList;
import com.example.qgchat.bean.UserBean;
import com.example.qgchat.db.DBUser;
import com.example.qgchat.fragment.LayoutChats;
import com.example.qgchat.fragment.LayoutContacts;
import com.example.qgchat.fragment.LayoutMoments;
import com.example.qgchat.service.QGService;
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.BeanUtil;
import com.example.qgchat.util.DBUtil;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.util.UltimateBar;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AtyMain extends BaseActivity {
    @BindView(R.id.vp_main)
    ViewPager viewPager;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.sdv_icon)
    CircleImageView sdvIcon;
    @BindView(R.id.nav_recycler)
    RecyclerView navRecycler;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.weather_state)
    TextView weatherState;
    @BindView(R.id.weather_quality)
    TextView weatherQuality;
    @BindView(R.id.weather_temp)
    TextView weatherTemp;
    @BindView(R.id.weather_area)
    TextView weatherArea;

    private MenuItem menuItem;
    private Intent serviceIntent;
    private QGService.QGBinder mBinder = null;
    private DrawerAdapter drawerAdapter;
    private List<DrawerList> drawerList = Arrays.asList(
            new DrawerList(R.drawable.ic_search_black_24dp, R.string.drawer_menu_search),
            new DrawerList(R.drawable.ic_settings_black_24dp, R.string.drawer_menu_setting)
    );

    //BottomNavigationView的监听事件
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //点击菜单项时跳转ViewPage
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (QGService.QGBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_main);
        autoLogin();
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBarForDrawer(ContextCompat.getColor(this, R.color.colorPrimary));
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        titleText.setText("消息");

        serviceIntent = new Intent(this, QGService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                findViewById(R.id.content).setTranslationX(slideOffset * drawerView.getWidth());
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    loadWeather();
                }
            }
        });
        toggle.syncState();
        setDrawerLayout();
        initViews();
    }

    private void loadWeather() {
        if (AccessNetwork.getNetworkState(AtyMain.this) != AccessNetwork.INTERNET_NONE) {

        }
    }

    private void setDrawerLayout() {
        SharedPreferences preferences = getSharedPreferences("qgchat", MODE_PRIVATE);
        account = preferences.getString("account", "");

        if (AccessNetwork.getNetworkState(AtyMain.this) != AccessNetwork.INTERNET_NONE) {
            String url = HttpUtil.getAccountMessageURL + "?account=" + account;
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UserBean bean = BeanUtil.handleUserBeanResponse(result);
                            DBUtil.saveUser(bean);
                            setDrawerHander(bean.getIconURL(),bean.getUsername());
                        }
                    });
                }

            });
        } else {
            DBUser bean = DataSupport.findFirst(DBUser.class);
            setDrawerHander(bean.getIconURL(),bean.getUsername());
        }

        drawerAdapter = new DrawerAdapter(drawerList);
        navRecycler.setAdapter(drawerAdapter);
        navRecycler.setLayoutManager(new LinearLayoutManager(AtyMain.this));
        drawerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DrawerList list = drawerList.get(position);
                switch (list.getResTitleId()) {
                    case R.string.drawer_menu_search:
                        break;
                    case R.string.drawer_menu_setting:
                        break;
                }
            }
        });
    }

    private void setDrawerHander(String icon,String title) {
        Glide.with(AtyMain.this).load(icon).into(sdvIcon);
        tvLogin.setText(title);
    }

    private void initViews() {
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MainViewPageFragmentAdapter adapter = new MainViewPageFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new LayoutChats());
        adapter.addFragment(new LayoutContacts());
        adapter.addFragment(new LayoutMoments());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 将当前的页面对应的底部标签设为选中状态
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
                //设置标题
                if (position == 0) {
                    titleText.setText(R.string.title_message);
                } else if (position == 1) {
                    titleText.setText(R.string.title_person);
                } else if (position == 2) {
                    titleText.setText(R.string.title_condition);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                //moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//            //moveTaskToBack(true);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.aty_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_friend) {
            Intent intent = new Intent(AtyMain.this, AtyAddFriend.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceIntent);
        unbindService(connection);
    }

}
