package com.geval6.praymate.Adapter;

import android.view.View;
import android.widget.TextView;
import com.geval6.praymate.R;

public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
    TextView age;
    TextView gender;
    TextView name;

    public ViewHolder(View itemView) {
        super(itemView);
        this.name = (TextView) itemView.findViewById(R.id.nameList);
        this.age = (TextView) itemView.findViewById(R.id.ageList);
        this.gender = (TextView) itemView.findViewById(R.id.genderList);
    }
}
