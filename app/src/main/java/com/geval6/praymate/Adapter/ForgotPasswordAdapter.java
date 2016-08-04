package com.geval6.praymate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.geval6.praymate.R;

public class ForgotPasswordAdapter extends BaseAdapter {
    Context context;

    public ForgotPasswordAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        return 3;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View passwordview = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_forgot, parent, false);
        EditText editText = (EditText) passwordview.findViewById(R.id.tokenEditText);
        TextView textview = (TextView) passwordview.findViewById(R.id.token);
        if (position == 0) {
            textview.setText("Token");
            editText.setHint("Enter Token");
            editText.setInputType(1);
        }
        if (position == 1) {
            textview.setText("New Password");
            editText.setHint("Enter New Password");
            editText.setInputType(129);
        }
        if (position == 0) {
            textview.setText("Confirm Password");
            editText.setHint("Confirm Password");
            editText.setInputType(129);
        }
        return passwordview;
    }
}
