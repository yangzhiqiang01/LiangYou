package com.inhim.pj.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.fragment.MyFragment;
import com.inhim.pj.fragment.ReadingFragment;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private ArrayList<Fragment> fs;
    private RadioButton radio1;
    private RadioButton radio2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        viewPager=findViewById(R.id.viewPager);
        radio1=findViewById(R.id.radio1);
        radio2=findViewById(R.id.radio2);
        radio1.setOnClickListener(this);
        radio2.setOnClickListener(this);
        fs=new ArrayList<>();
        fs.add(new ReadingFragment());
        fs.add(new MyFragment());
        FragmentManager fm=getSupportFragmentManager();
        MyPagerAdapter adapter=new MyPagerAdapter(fm);
        viewPager.setAdapter(adapter);
        viewPager .setOffscreenPageLimit(2);
        setListener();
    }
    private void setListener() {
        // TODO Auto-generated method stub
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radio1.setChecked(true);
                        break;
                    case 1:
                        radio2.setChecked(true);
                        break;
                }

            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.radio2:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    /**
     * 自定义适配器  FragmentPagerAdapter
     */
    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fs.get(arg0);
        }

        @Override
        public int getCount() {
            return fs.size();
        }
    }
}
