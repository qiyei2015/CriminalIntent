package com.qiyei.criminalintent.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.qiyei.criminalintent.R;
import com.qiyei.criminalintent.fragment.CrimeFragment;
import com.qiyei.criminalintent.fragment.CrimeListFragment;
import com.qiyei.criminalintent.model.Crime;

/**
 * Created by daner on 2016/6/11.
 * 1273482124@qq.com
 */
public class CrimeListActivity extends BaseCrimeActivity implements CrimeListFragment.Callback
        ,CrimeFragment.Callback{


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_master_detail;
    }


    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }


    @Override
    public void onCrimeSelected(Crime crime) {

        if (findViewById(R.id.fragment_container_detail) == null){
            Intent intent = CrimePagerActivity.newIntent(this,crime.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_detail,newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
