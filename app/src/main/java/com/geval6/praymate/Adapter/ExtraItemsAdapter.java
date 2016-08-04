package com.geval6.praymate.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import java.util.ArrayList;
import java.util.HashMap;

public class ExtraItemsAdapter extends Adapter<ViewHolder> {
    Context context;
    public ArrayList items;

    public ExtraItemsAdapter(Context context, ArrayList items) {
        this.context = context;
        this.items = items;
    }

    public void addPerson(HashMap item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void count() {
        this.items.size();
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return this.items.size();
    }

    public int getItemViewType(int position) {
        return position;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_addpeople_list, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap item = (HashMap) this.items.get(position);
        holder.name.setText(item.get(HKRequestIdentifier.kParameterTempleName).toString());
        holder.age.setText(item.get("age").toString());
        holder.gender.setText(item.get("gender").toString());
        Log.i("Count", String.valueOf(this.items.size()));
    }
}
