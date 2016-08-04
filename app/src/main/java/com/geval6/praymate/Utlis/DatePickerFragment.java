package com.geval6.praymate.Utlis;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class DatePickerFragment extends DialogFragment {
    private int day;
    private int month;
    OnDateSetListener ondateSet;
    private int year;

    public void setCallBack(OnDateSetListener ondate) {
        this.ondateSet = ondate;
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.year = args.getInt("year");
        this.month = args.getInt("month");
        this.day = args.getInt("day");
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), this.ondateSet, this.year, this.month, this.day);
    }
}
