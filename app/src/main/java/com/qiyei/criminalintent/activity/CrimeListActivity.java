package com.qiyei.criminalintent.activity;

import android.support.v4.app.Fragment;

import com.qiyei.criminalintent.fragment.CrimeListFragment;

/**
 * Created by daner on 2016/6/11.
 * 1273482124@qq.com
 */
public class CrimeListActivity extends BaseCrimeActivity {


    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

}
