package com.inhim.pj.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.activity.AllCategoriesActivity;
import com.inhim.pj.activity.SearchActivity;
import com.inhim.pj.entity.ReaderStyle;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class ReadingFragment extends Fragment implements View.OnClickListener {
    private ViewPager viewPager;
    private ArrayList<Fragment> fs;
    private RadioButton radio1,radio2,radio3;
    private Context mContext;
    private ReaderStyle readerStyle;
    private Gson gson;
    private RelativeLayout rela_search;
    ImageView iv_more;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_reading, container, false);
        gson=new Gson();
        mContext=getActivity();
        initView(view);
        getReaderStyle();
        return view;
    }

    private void getReaderStyle() {
        MyOkHttpClient.getInstance().asyncGetNoToken(Urls.getReaderStyle("type"), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                readerStyle=gson.fromJson(result,ReaderStyle.class);
                setFragment();
            }

        });
    }
    private void setFragment() {
        Bundle bundle1=new Bundle();
        bundle1.putSerializable("result",readerStyle.getList().get(0).getReaderStyleValue());
        Bundle bundle2=new Bundle();
        bundle2.putSerializable("result",readerStyle.getList().get(1).getReaderStyleValue());
        Bundle bundle3=new Bundle();
        bundle3.putSerializable("result",readerStyle.getList().get(2).getReaderStyleValue());
        ReadingTwoFragment readingTwoFragment1=new ReadingTwoFragment();
        VideoFragment readingTwoFragment2=new VideoFragment();
        ListenFragment readingTwoFragment3=new ListenFragment();
        readingTwoFragment1.setArguments(bundle1);
        readingTwoFragment2.setArguments(bundle2);
        readingTwoFragment3.setArguments(bundle3);
        fs=new ArrayList<>();
        fs.add(readingTwoFragment1);
        fs.add(readingTwoFragment2);
        fs.add(readingTwoFragment3);
        FragmentManager fm=getChildFragmentManager();
        MyPagerAdapter adapter=new MyPagerAdapter(fm);
        viewPager.setAdapter(adapter);
        viewPager .setOffscreenPageLimit(3);
        setListener();
    }
    private void initView(View view) {
        viewPager=view.findViewById(R.id.viewPager);
        radio1=view.findViewById(R.id.radio1);
        radio2=view.findViewById(R.id.radio2);
        radio3=view.findViewById(R.id.radio3);
        radio1.setOnClickListener(this);
        radio2.setOnClickListener(this);
        radio3.setOnClickListener(this);
        rela_search=view.findViewById(R.id.rela_search);
        rela_search.setOnClickListener(this);
        iv_more=view.findViewById(R.id.iv_more);
        iv_more.setOnClickListener(this);
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
                    case 2:
                        radio3.setChecked(true);
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
            case R.id.radio3:
                viewPager.setCurrentItem(2);
                break;
            case R.id.iv_more:
                Intent intent1=new Intent(mContext, AllCategoriesActivity.class);
                startActivity(intent1);
                break;
            case R.id.rela_search:
                Intent intent=new Intent(mContext, SearchActivity.class);
                startActivity(intent);
                break;
                default:
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
