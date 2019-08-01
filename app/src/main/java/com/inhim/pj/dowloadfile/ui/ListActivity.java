package com.inhim.pj.dowloadfile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.dowloadvedio.DidNotDownloadFragment;

import java.util.ArrayList;


/**
 * How to use Android Downloader in RecyclerView.
 */
public class ListActivity extends FragmentActivity implements View.OnClickListener{


  private TextView tvCourse,tv_edit;
  private ViewPager viewPager;
  private ArrayList<Fragment> fs;
  private RadioButton rb0;
  private RadioButton rb1;
  private ImageView iv_back;
  private boolean isOneFragment=true;
  private Fragment goOutFragment;
  private Fragment goOutTwoFragment;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    initView();
    fs=new ArrayList<>();
    goOutFragment=new HaveDownloadedFragment();
    goOutTwoFragment=new DidNotDownloadFragment();
    Bundle bundle=new Bundle();
    goOutFragment.setArguments(bundle);
    goOutTwoFragment.setArguments(bundle);
    fs.add(goOutFragment);
    fs.add(goOutTwoFragment);
    FragmentManager fm=getSupportFragmentManager();
    MyPagerAdapter adapter=new MyPagerAdapter(fm);
    viewPager.setAdapter(adapter);
    setListener();
  }

  private void setListener() {
    // TODO Auto-generated method stub
    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      public void onPageSelected(int position) {
        switch (position) {
          case 0:
            isOneFragment=true;
            rb0.setChecked(true);
            break;
          case 1:
            isOneFragment=false;
            Intent intent = new Intent();
            intent.setAction("the.search");
            sendBroadcast(intent);
            rb1.setChecked(true);
            break;
        }

      }
      public void onPageScrolled(int arg0, float arg1, int arg2) {

      }
      public void onPageScrollStateChanged(int arg0) {

      }
    });
  }


  public void initView() {
    viewPager= findViewById(R.id.viewpager);
    rb0= findViewById(R.id.radiobutton0);
    rb1= findViewById(R.id.radiobutton1);
    iv_back=findViewById(R.id.iv_back);
    iv_back.setOnClickListener(this);
    rb0.setOnClickListener(this);
    rb1.setOnClickListener(this);
    tvCourse=findViewById(R.id.tvCourse);
    tvCourse.setText("下载管理");
    tv_edit=findViewById(R.id.tv_edit);
    tv_edit.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()){
      case R.id.radiobutton0:
        isOneFragment=true;
        viewPager.setCurrentItem(0);
        break;
      case R.id.radiobutton1:
        isOneFragment=false;
        Intent intent = new Intent();
        intent.setAction("the.search");
        sendBroadcast(intent);
        viewPager.setCurrentItem(1);
        break;
      case R.id.iv_back:
        finish();
        break;
      case R.id.tv_edit:
        if(isOneFragment){
          Intent intent1 = new Intent();
          intent1.setAction("one.fragment.delete");
          sendBroadcast(intent1);
        }else{
          Intent intent2 = new Intent();
          intent2.setAction("two.fragment.delete");
          sendBroadcast(intent2);
        }
        break;
        default:
          break;
    }
  }

  /*自定义适配器  FragmentPagerAdapter*/
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
