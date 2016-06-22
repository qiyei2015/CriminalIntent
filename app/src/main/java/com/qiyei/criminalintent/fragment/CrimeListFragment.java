package com.qiyei.criminalintent.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.qiyei.criminalintent.R;
import com.qiyei.criminalintent.model.Crime;
import com.qiyei.criminalintent.model.CrimeLab;
import com.qiyei.criminalintent.utils.Utils;

import java.util.List;

/**
 * Created by daner on 2016/6/11.
 * 1273482124@qq.com
 */
public class CrimeListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private boolean mSubTitleVisible = false;
    private static final String SAVE_SUNTITLE_VISIBLE = "subtitle";

    private Callback mCallback;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义的有菜单
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (savedInstanceState != null){
            mSubTitleVisible = savedInstanceState.getBoolean(SAVE_SUNTITLE_VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime_menu,menu);

        MenuItem subItem = menu.findItem(R.id.menu_item_show_subtitle);

        if (mSubTitleVisible){
            subItem.setTitle(R.string.hide_Subtitle);
        } else {
            subItem.setTitle(R.string.show_Subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.getInstance(getContext()).add(crime);
                updateUI();
                mCallback.onCrimeSelected(crime);
                break;
            case R.id.menu_item_show_subtitle:
                mSubTitleVisible = !mSubTitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_SUNTITLE_VISIBLE,mSubTitleVisible);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    /**
     * 更新UI
     */
    public void updateUI(){

        CrimeLab lab = CrimeLab.getInstance(getContext());
        List<Crime> list = lab.getCrimeList();

        //不为空就通知更新Adapter数据
        if (mCrimeAdapter == null){
            mCrimeAdapter = new CrimeAdapter(list);
            mRecyclerView.setAdapter(mCrimeAdapter);

        } else {
            mCrimeAdapter.setCrimeList(list);
            mCrimeAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    /**
     * 更新显示子标题
     */
    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.getInstance(getContext());
        int count = crimeLab.getCrimeList().size();
        String subTitle = getString(R.string.subtitle_format,count);

        if (!mSubTitleVisible){
            subTitle = "";
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subTitle);
    }


    /**
     * RecyclerView.ViewHolder的子类
     */
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        private Crime mCrime;

        public CrimeHolder(View itemView){
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
            itemView.setOnClickListener(this);//这里的this代表 CrimeHolder对象
        }

        /**
         * 绑定数据到ViewHolder
         * @param crime
         */
        public void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(Utils.dateFormatToString(mCrime.getDate()));
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(getContext(),"点击了" + mCrime.getTitle(),Toast.LENGTH_SHORT).show();
            //Intent intent = CrimePagerActivity.newIntent(getContext(),mCrime.getId());
            //startActivity(intent);
            mCallback.onCrimeSelected(mCrime);

        }
    }

    /**
     * RecyclerView.Adapter子类
     */
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimeList;

        public CrimeAdapter(List<Crime> list){
            mCrimeList = list;
        }

        public Crime getItem(int position){
            return mCrimeList.get(position);
        }

        @Override
        public int getItemCount() {
            return mCrimeList.size();
        }

        /**
         * 创建ViewHolder，根据视图布局文件创建
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.list_item_crime,parent,false);
            CrimeHolder crimeHolder = new CrimeHolder(view);

            return crimeHolder;
        }

        /**
         * 绑定模型数据到ViewHolder
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {

            Crime crime = getItem(position);
            holder.bindCrime(crime);
        }

        /**
         * 设置mCrimeList
         * @param list
         */
        public void setCrimeList(List<Crime> list){
            mCrimeList = list;
        }
    }

    /**
     * Fragment 回调接口，由Activity实现，可以直接调用Activity的方法
     */
    public interface Callback{
        //
        void onCrimeSelected(Crime crime);
    }

}
