package com.example.qgchat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.qgchat.R;
import com.example.qgchat.adapter.GuideAdapter;
import com.example.qgchat.loginAndregister.AtyLogin;
import com.example.qgchat.util.UltimateBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AtyGuide extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    @BindView(R.id.vp_guide)
    ViewPager viewPager;
    @BindView(R.id.iv_indicator_dot1)
    ImageView image1;
    @BindView(R.id.iv_indicator_dot2)
    ImageView image2;
    @BindView(R.id.iv_indicator_dot3)
    ImageView image3;

    ImageView imageview[] =new ImageView[3];
    private List<View> viewList;
    private Button btnTomain;
    private GuideAdapter adapter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_guide);
        ButterKnife.bind(this);
        preferences=getSharedPreferences("qgchat",MODE_PRIVATE);
        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            UltimateBar ultimateBar = new UltimateBar(this);
            ultimateBar.setHintBar();
        }
    }

    private void goLogin() {
        Intent intent = new Intent(AtyGuide.this, AtyLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goHome() {
        Intent intent = new Intent(AtyGuide.this, AtyMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void initView() {
        //设置ViewPage
        viewList = new ArrayList<>();
        final LayoutInflater inflater = LayoutInflater.from(this);
        View view1=inflater.inflate(R.layout.guide_page1, null);
        View view2=inflater.inflate(R.layout.guide_page2, null);
        View view3=inflater.inflate(R.layout.guide_page3, null);
        ImageView guide_image1 = (ImageView) view1.findViewById(R.id.guide_image1);
        ImageView guide_image2 = (ImageView) view2.findViewById(R.id.guide_image2);
        ImageView guide_image3 = (ImageView) view3.findViewById(R.id.guide_image3);
        Glide.with(this).load(R.drawable.shot1).into(guide_image1);
        Glide.with(this).load(R.drawable.shot2).into(guide_image2);
        Glide.with(this).load(R.drawable.shot3).into(guide_image3);

        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        adapter = new GuideAdapter(viewList,AtyGuide.this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        imageview[0]=image1;
        imageview[1]=image2;
        imageview[2]=image3;
        //跳转到第三个页面后点击“进入首页”按钮进入主页
        btnTomain = (Button) (viewList.get(2)).findViewById(R.id.btn_to_main);
        btnTomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=getSharedPreferences("qgchat",MODE_PRIVATE);
                /**
                 * 是否登陆过，也就是是否有缓存的帐号密码
                 */
                boolean login = preferences.getBoolean("login", false);
                if (login) {
                    goHome();
                } else {
                    goLogin();
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i=0;i<imageview.length;i++) {
            if (i == position) {
                imageview[i].setImageResource(R.drawable.selected);
            } else {
                imageview[i].setImageResource(R.drawable.unselected);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
