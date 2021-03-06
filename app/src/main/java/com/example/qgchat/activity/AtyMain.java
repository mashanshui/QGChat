package com.example.qgchat.activity;

import android.Manifest;
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

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.qgchat.R;
import com.example.qgchat.adapter.DrawerAdapter;
import com.example.qgchat.adapter.MainViewPageFragmentAdapter;
import com.example.qgchat.addfriend.AtyAddFriend;
import com.example.qgchat.bean.DrawerList;
import com.example.qgchat.bean.UserBean;
import com.example.qgchat.bean.Weather;
import com.example.qgchat.db.DBChatMsg;
import com.example.qgchat.db.DBUser;
import com.example.qgchat.db.DBUserGruop;
import com.example.qgchat.db.DBUserItemMsg;
import com.example.qgchat.db.DBUserList;
import com.example.qgchat.db.DBUserMoments;
import com.example.qgchat.db.DBWeather;
import com.example.qgchat.fragment.LayoutChats;
import com.example.qgchat.fragment.LayoutContacts;
import com.example.qgchat.fragment.LayoutMoments;
import com.example.qgchat.listener.PermissionListener;
import com.example.qgchat.service.QGService;
import com.example.qgchat.socket.ServerManager;
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.BeanUtil;
import com.example.qgchat.util.DBUtil;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.util.UltimateBar;
import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.baidu.location.h.k.D;
import static com.example.qgchat.service.QGService.account;


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

    public LocationClient mLocationClient = null;
    /**
     * 经度
     */
    public static double lon = 0;
    /**
     * 纬度
     */
    public static double lat = 0;

    private MenuItem menuItem;
    private Intent serviceIntent;
    private QGService.QGBinder mBinder = null;
    private DrawerAdapter drawerAdapter;
    //侧滑菜单菜单项
    private List<DrawerList> drawerList = Arrays.asList(
            new DrawerList(R.drawable.ic_search_black_24dp, R.string.drawer_menu_search),
            new DrawerList(R.drawable.ic_settings_black_24dp, R.string.drawer_menu_setting),
            new DrawerList(R.drawable.ic_menu_share, R.string.drawer_menu_share),
            new DrawerList(R.drawable.ic_menu_camera, R.string.drawer_menu_camera),
            new DrawerList(R.drawable.ic_menu_gallery, R.string.drawer_menu_gallery),
            new DrawerList(R.drawable.ic_exit_black_24dp, R.string.exit_login)
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

    //绑定服务
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (QGService.QGBinder) service;
            if (serverManager.getAccount() == null) {
                EventBus.getDefault().post(new ServerManager.Connection(true));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_main);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBarForDrawer(ContextCompat.getColor(this, R.color.colorPrimary));
        ButterKnife.bind(this);
        //shareSDK初始化
        MobSDK.init(this, "22971d59d2102", "695d328d1359fe91132c5c4c6ae96277");
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(new MyLocationListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        titleText.setText("消息");

        //开启绑定服务
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
                if (slideOffset == 1) {
                    loadWeather();
                }
            }
        });
        toggle.syncState();
        setDrawerLayout();
        initViews();
        requestRuntimePermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                initLocation();
                mLocationClient.start();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

            }
        });
    }


    /**
     * 初始化百度地图
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setScanSpan(5000);
        //可选，设置是否需要地址信息，默认不需要
        //option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    /**
     * 百度地图定位监听
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            lon = bdLocation.getLongitude();    //获取经度信息
            lat = bdLocation.getLatitude();    //获取纬度信息
        }

    }

    /**
     * 加载天气信息
     */
    private void loadWeather() {
        if (AccessNetwork.getNetworkState(AtyMain.this) != AccessNetwork.INTERNET_NONE && lat != 0 && lon != 0) {
            String url = HttpUtil.getWeatherURL.replace("CITY", lon + "," + lat);
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Weather weather = BeanUtil.handleWeatherResponse(result);
                    DBUtil.saveWeather(serverManager.getAccount(), weather);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateWeather();
                        }
                    });
                }
            });
        } else {
            updateWeather();
        }
    }

    private void updateWeather() {
        DBWeather weather = DataSupport.findFirst(DBWeather.class);
        if (weather != null) {
            weatherArea.setText(weather.getCity());
            weatherState.setText(weather.getTxt());
            weatherTemp.setText(weather.getTmp() + "°");
            weatherQuality.setText(weather.getDir() + weather.getSc());
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
                    if (!result.equals("failed")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UserBean bean = BeanUtil.handleUserBeanResponse(result);
                                DBUtil.saveUser(bean);
                                setDrawerHander(bean.getIconURL(), bean.getUsername());
                            }
                        });
                    }
                }

            });
        } else {
            DBUser bean = DataSupport.findFirst(DBUser.class);
            setDrawerHander(bean.getIconURL(), bean.getUsername());
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
                    case R.string.exit_login:
                        clearSharePreferences();
                        deleteDataBase();
                        restartApplication();
                        break;
                    case R.string.drawer_menu_share:
                        showShare();
                        break;
                }
            }
        });
    }

    /**
     *分享
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
// text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    private void deleteDataBase() {
        DataSupport.deleteAll(DBUserGruop.class);
        DataSupport.deleteAll(DBUserItemMsg.class);
        DataSupport.deleteAll(DBUserList.class);
        DataSupport.deleteAll(DBUserMoments.class);
        DataSupport.deleteAll(DBChatMsg.class);
        DataSupport.deleteAll(DBUser.class);
        DataSupport.deleteAll(DBWeather.class);
    }

    private void clearSharePreferences() {
        SharedPreferences preferences = getSharedPreferences("qgchat", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("login");
        editor.remove("account");
        editor.remove("password");
        editor.apply();
    }

    /**
     * 重启应用
     */
    private void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * @param icon
     * @param title
     * 设置DrawerLayout的图形和文字
     */
    private void setDrawerHander(String icon, String title) {
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
        mLocationClient.stop();
        stopService(serviceIntent);
        unbindService(connection);
    }

}
