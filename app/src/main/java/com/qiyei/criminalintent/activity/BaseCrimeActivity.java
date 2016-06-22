package com.qiyei.criminalintent.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.qiyei.criminalintent.R;

/**
 * Created by daner on 2016/6/11.
 * 1273482124@qq.com
 */
public abstract class BaseCrimeActivity extends AppCompatActivity {
    private FragmentManager fm;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());

        fm = getSupportFragmentManager();
        mFragment = fm.findFragmentById(R.id.fragment_container);
        if (mFragment == null){
            mFragment = createFragment();
            //添加提交fragment事务
            fm.beginTransaction().add(R.id.fragment_container,mFragment).commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 返回有效布局资源Id
     * @return
     */
    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    /**
     * 抽象方法，用于创建Fragment
     * @return 创建的fragment
     */
    protected abstract Fragment createFragment();




}
