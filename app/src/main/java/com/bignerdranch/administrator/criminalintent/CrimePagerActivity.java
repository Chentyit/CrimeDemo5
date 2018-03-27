package com.bignerdranch.administrator.criminalintent;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_TD = "com.bignerdranch.android.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button ToFirst;
    private Button ToLast;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_TD, crimeId);
        return intent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        UUID crimed = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_TD);
        //找到ViewPager布局
        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        //获取数据集
        mCrimes = CrimeLab.get(this).getCrimes();
        //获取FragmentManager实例
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                /**
                 * 查看源代码时注意看这里的日志打印
                 */
                Log.i("CrimePagerActivity", "当前item的getCurrentItem()位置" + mViewPager.getCurrentItem());
                Log.i("CrimePagerActivity", "当前item的position位置" + position);
                /**
                 * 这里是获取到点击进来的item的位置：在这里特别说明，getItem()方法的形参position
                 * 得到的第当前item前一个和后羿后一个的item的位置，获得位置后预先加载先一个位置
                 * 的视图，而mAdapter.getCurrentItem()得到的是当前item的位置，但是！这里要注意！
                 * 当item翻到position==1时，已经将position==0位置的视图加载好了，当翻到第0个item
                 * 时，getCurrentItem()不会更新为0，还是1，所以在这里的setButtonView()方法的作用
                 * 只是为了得到从列表点击进来后启动的item，滑动获得的item交给addOnPageChangeListener
                 */
                setButtonView(mViewPager.getCurrentItem());
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        /**
         * 在此声明，这个方法在这个挑战中很重要！很重要！很重要！当滑动item是就会调用这个方法
         * 这个方法中的onPageSelected()回调方法是用来获取item滑动变化后当前的item，而他的形参
         * 就是当前item的位置
         */
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setButtonView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ToFirst = (Button) findViewById(R.id.jump_first);
        ToLast = (Button) findViewById(R.id.jump_last);
        ToFirst.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });
        ToLast.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                mViewPager.setCurrentItem(mCrimes.size() - 1);
            }
        });
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimed)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    /**
     * 这个就不用解释了
     */
    private void setButtonView(int position) {
        if (position == 0){
            ToFirst.setVisibility(View.INVISIBLE);
            ToLast.setVisibility(View.VISIBLE);
        }
        if (position == mCrimes.size() - 1){
            ToLast.setVisibility(View.INVISIBLE);
            ToFirst.setVisibility(View.VISIBLE);
        }
        if (position != 0 && position != mCrimes.size() - 1) {
            ToFirst.setVisibility(View.VISIBLE);
            ToLast.setVisibility(View.VISIBLE);
        }
    }

}
