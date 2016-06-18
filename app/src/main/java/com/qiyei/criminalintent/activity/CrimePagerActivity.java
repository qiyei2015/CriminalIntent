package com.qiyei.criminalintent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.qiyei.criminalintent.R;
import com.qiyei.criminalintent.fragment.CrimeFragment;
import com.qiyei.criminalintent.model.Crime;
import com.qiyei.criminalintent.model.CrimeLab;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity{

    private ViewPager mViewPager;
    private FragmentManager fm;
    private List<Crime> mCrimeList;

    private static final String EXTRA_CRIME_ID = "com.qiyei.activity.crime_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        mCrimeList = CrimeLab.getInstance(getApplicationContext()).getCrimeList();
        fm = getSupportFragmentManager();

        final UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimeList.get(position);
                Fragment fragment = CrimeFragment.newInstance(crime.getId());
                return fragment;
            }

            @Override
            public int getCount() {
                return mCrimeList.size();
            }
        });
        //预加载相邻的两页数据
        mViewPager.setOffscreenPageLimit(2);

        for (int i = 0 ; i < mCrimeList.size() ; i++){
            if (mCrimeList.get(i).getId().equals(uuid)){
                //设置第i个
                mViewPager.setCurrentItem(i);
            }
        }

    }

    /**
     * 创建带UUID的Intent
     * @param context
     * @param id
     * @return
     */
    public static Intent newIntent(Context context , UUID id){
        Intent intent = new Intent(context,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,id);
        return intent;
    }

}
