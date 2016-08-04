package com.geval6.praymate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import java.util.ArrayList;
import java.util.HashMap;

public class AddPeopleAdapter extends BaseAdapter {
    Context context;
    ArrayList items;

    public AddPeopleAdapter(Context context, ArrayList items) {
        this.context = context;
        this.items = items;
    }

    public void addPerson(HashMap item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int position) {
        return this.items.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View addPeople = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_addpeople_list, parent, false);
        TextView age = (TextView) addPeople.findViewById(R.id.ageList);
        TextView gender = (TextView) addPeople.findViewById(R.id.genderList);
        HashMap item = (HashMap) getItem(position);
        ((TextView) addPeople.findViewById(R.id.nameList)).setText(item.get(HKRequestIdentifier.kParameterTempleName).toString());
        age.setText(item.get("age").toString());
        gender.setText(item.get("gender").toString());
        return addPeople;
    }
}
