package com.example.sch_agro.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public  class DatePicker extends  DialogFragment implements DatePickerDialog.OnDateSetListener{
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
       // return new DatePickerDialog(getActivity());

        return new DatePickerDialog(requireActivity(), (DatePickerDialog.OnDateSetListener) this, yy, mm, dd);

        //return new DatePickerDialog(requireActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), yy, mm, dd);

        //DatePickerDialog dialog = new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener)getActivity(), yy, mm,dd);
        //DatePickerDialog dialog = new DatePickerDialog((Context) requireActivity(), (ScheduleMatchOptionActivity)getActivity(), yy, mm,dd);
       // return dialog;

    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        android.icu.util.Calendar mCalendar = android.icu.util.Calendar.getInstance();
        mCalendar.set(android.icu.util.Calendar.YEAR, year);
        mCalendar.set(android.icu.util.Calendar.MONTH, month);
        mCalendar.set(android.icu.util.Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(mCalendar.getTime());
      //  tvDate.setText(selectedDate);
    }

}