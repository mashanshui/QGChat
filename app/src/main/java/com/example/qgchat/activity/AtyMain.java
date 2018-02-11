package com.example.qgchat.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.BeanUtil;
import com.example.qgchat.util.DBUtil;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.util.UltimateBar;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.mob.MobSDK;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    private SharedPreferences preferences;
    /**
     * ViewPage中的聊天页面
     */
    private LayoutChats layoutChats;
    /**
     * ViewPage中的联系人页面
     */
    private LayoutContacts layoutContacts;
    /**
     * ViewPage中的音乐界面
     */
    private LayoutMoments layoutMoments;
    /**
     * 百度地图定位
     */
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
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Map<String, EaseUser> map;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                layoutContacts.setContactsMap(map);
                layoutContacts.refresh();
            }
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

        preferences = getSharedPreferences("qgchat", MODE_PRIVATE);
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

    private void initViews() {
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MainViewPageFragmentAdapter adapter = new MainViewPageFragmentAdapter(getSupportFragmentManager());

        //消息页面
        layoutChats = new LayoutChats();
        layoutChats.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(AtyMain.this, AtyChatRoom.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });

        //联系人页面
        layoutContacts = new LayoutContacts();
        new Thread(new Runnable() {
            @Override
            public void run() {
                map = getContacts();
                handler.sendEmptyMessage(1);
            }
        }).start();
        layoutContacts.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                startActivity(new Intent(AtyMain.this, AtyChatRoom.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
            }
        });

        //音乐页面
        layoutMoments = new LayoutMoments();

        adapter.addFragment(layoutChats);
        adapter.addFragment(layoutContacts);
        adapter.addFragment(layoutMoments);
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
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            for (EMMessage message : messages) {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
            layoutChats.refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };

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
                    String account = preferences.getString("account", null);
                    DBUtil.saveWeather(account, weather);
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
            String url = HttpUtil.getUserMessageURL + "?account=" + account;
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
                        loginOut();
                        break;
                    case R.string.drawer_menu_share:
                        showShare();
                        break;
                }
            }
        });
    }

    private void loginOut() {
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                clearSharePreferences();
                deleteDataBase();
                restartApplication();
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                setToast("退出登录失败");
            }
        });
    }

    /**
     * 分享
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
     * @param title 设置DrawerLayout的图形和文字
     */
    private void setDrawerHander(String icon, String title) {
        Glide.with(AtyMain.this).load(icon).into(sdvIcon);
        tvLogin.setText(title);
    }

    /**
     * 获取联系人
     *
     * @return
     */
    private Map<String, EaseUser> getContacts() {
        Map<String, EaseUser> map = new HashMap<>();
        try {
            List<String> userNames = EMClient.getInstance().contactManager().getAllContactsFromServer();
            for (String userId : userNames) {
                Log.i(TAG, "getContacts: " + "好友列表中有 : " + userId);
                map.put(userId, new EaseUser(userId));
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return map;
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

    @SuppressLint("RestrictedApi")
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
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

}
