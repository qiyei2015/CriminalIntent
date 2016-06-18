package com.qiyei.criminalintent.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.qiyei.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by qiyei on 2016/6/13.
 */
public class DatePickerFragment extends DialogFragment {

    private DatePicker mDatePicker;

    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "com.qiyei.fragment.date";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date,null);
        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);

        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);

        mDatePicker.init(year,month,day,null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date tempDate = new GregorianCalendar(year,month,day).getTime();

                                sendResult(Activity.RESULT_OK,tempDate);
                            }
                        })
                .create();
    }

    /**
     * 返回带Date Argument 的DatePickerFragment实例
     * @param date
     * @return
     */
    public static DatePickerFragment newInstance(Date date){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE,date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }

    /**
     * 发送启动结果给TegraFragment
     * @param resultCode
     * @param date
     */
    private void sendResult(int resultCode , Date date){
        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
