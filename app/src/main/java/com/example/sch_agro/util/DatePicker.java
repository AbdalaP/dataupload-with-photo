package com.example.sch_agro.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public  class DatePicker extends  DialogFragment  {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
       // return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener), yy, mm, dd);

       // return new DatePickerDialog(requireActivity(), (DatePickerDialog.OnDateSetListener) this, yy, mm, dd);

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), yy, mm, dd);

       // DatePickerDialog dialog = new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener)getActivity(), year, month,dayOfMonth);
     //   DatePickerDialog dialog = new DatePickerDialog((Context) requireActivity(), (ScheduleMatchOptionActivity)getActivity(), yy, mm,dd);
       // return dialog;

    }



}