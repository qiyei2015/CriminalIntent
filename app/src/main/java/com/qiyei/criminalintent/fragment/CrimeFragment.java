package com.qiyei.criminalintent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.qiyei.criminalintent.R;
import com.qiyei.criminalintent.activity.PhotoViewActivity;
import com.qiyei.criminalintent.model.Crime;
import com.qiyei.criminalintent.model.CrimeLab;
import com.qiyei.criminalintent.utils.Utils;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by daner on 2016/6/10.
 * 1273482124@qq.com
 *
 */
public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleFiled;
    private EditText mCrimeDetail;
    private Button mDateButton;
    private Button mChooseSuspect;
    private Button mSendCrimeReport;
    private CheckBox mSolvedCheckBox;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;

    private File mPhotoFile;
    private Callback mCallback;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATA = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID uuid = null;
        uuid = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        if (savedInstanceState != null){
            uuid = (UUID) savedInstanceState.get("uuid");
        }
        mCrime = CrimeLab.getInstance(getContext()).getCrime(uuid);
        mPhotoFile = CrimeLab.getInstance(getContext()).getPhotoFile(mCrime);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleFiled = (EditText) view.findViewById(R.id.crime_title);
        mCrimeDetail = (EditText) view.findViewById(R.id.crime_detail);
        mDateButton = (Button) view.findViewById(R.id.crime_date);
        mChooseSuspect = (Button) view.findViewById(R.id.crime_suspect);
        mSendCrimeReport = (Button) view.findViewById(R.id.crime_report);
        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mPhotoView = (ImageView) view.findViewById(R.id.crime_photo_view);
        mPhotoButton = (ImageButton) view.findViewById(R.id.crime_camera_button);

        //显示数据
        mTitleFiled.setText(mCrime.getTitle());
        mTitleFiled.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //保存输入的title
                mCrime.setTitle(s.toString());
                updateCrime();
                //mDateButton.setEnabled(false);//禁用Button
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCrimeDetail.setText(mCrime.getDetail());
        mCrimeDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //保存信息内容
                mCrime.setDetail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateDate(mCrime);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());

                //设置CrimeFragment为DatePickerFragment的目标Fragment
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATA);

                dialog.show(fragmentManager,DIALOG_DATE);
            }
        });

        final Intent pickIntent = new Intent();
        pickIntent.setAction(Intent.ACTION_PICK);
        pickIntent.setData(ContactsContract.Contacts.CONTENT_URI);

        //pickIntent.addCategory(Intent.CATEGORY_HOME);

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickIntent,PackageManager.SIGNATURE_MATCH) == null){
            mChooseSuspect.setEnabled(false);
        }

        mChooseSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动成功后会回调onActivityResult()
                startActivityForResult(pickIntent,REQUEST_CONTACT);
            }
        });

        mSendCrimeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动发送
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_suspect));
                startActivity(intent);
            }
        });

        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //更新crime的solved状态
                mCrime.setSolved(isChecked);
                updateCrime();
            }
        });

        final Intent captureImage = new Intent();
        captureImage.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null
                && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        if (canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });

        updatePhotoView();

        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (mPhotoFile != null && mPhotoFile.exists()){
                   Intent intent = new Intent(getContext(), PhotoViewActivity.class);
                   intent.putExtra("photoView",mPhotoFile.getPath());
                   intent.putExtra("uuid",mCrime.getId());
                   startActivity(intent);
               }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATA){
            Date newDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(newDate);
            updateCrime();
            updateDate(mCrime);
        }

        if (requestCode == REQUEST_CONTACT && data != null){
            Uri uri = data.getData();

            //查询所有的名字
            String[] queryFileds = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = getActivity().getContentResolver().query(uri,queryFileds,null,null,null);

            try {
                if (cursor.getCount() == 0){
                    return;
                }

                //只有一条记录
                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                mCrime.setSuspect(suspect);
                updateCrime();
                mChooseSuspect.setText(suspect);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }

        }

        if (requestCode == REQUEST_PHOTO){
            updateCrime();
            updatePhotoView();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        //更新mCrime
        CrimeLab.getInstance(getContext()).updateCrime(mCrime);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("uuid",mCrime.getId());
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
     * 更新Crime
     */
    private void updateCrime(){
        CrimeLab.getInstance(getContext()).updateCrime(mCrime);
        mCallback.onCrimeUpdated(mCrime);
    }

    /**
     * 更新日期
     * @param crime
     */
    private void updateDate(Crime crime) {
        mDateButton.setText(Utils.dateFormatToString(crime.getDate()));
    }

    /**
     * 更新PhotoView图片显示
     */
    private void updatePhotoView(){
        if (mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = Utils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    /**
     * 创建带Argument的Fragment
     * @param id
     * @return
     */
    public static CrimeFragment newInstance(UUID id){

        Bundle arg = new Bundle();
        arg.putSerializable(ARG_CRIME_ID,id);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(arg);
        return crimeFragment;
    }

    /**
     * 返回消息模板
     * @return
     */
    private String getCrimeReport(){
        String solvedString = null;
        if (mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        //日期格式化
        String dateFormat = "MMM dd ,EEE";
        String dateString = DateFormat.format(dateFormat,mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_suspect);
        } else {
            suspect = getString(R.string.crime_report_no_suspect);
        }

        String report = getString(R.string.crime_report,mCrime.getTitle(),dateString,solvedString
                ,suspect);

        return report;
    }

    /**
     * 回调接口
     */
    public interface Callback{

        void onCrimeUpdated(Crime crime);

    }

}
