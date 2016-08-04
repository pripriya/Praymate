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

public class FeatureAdapter extends BaseAdapter {
    Context context;
    public ArrayList<Object> features;

    public FeatureAdapter(Context context, ArrayList<Object> arrayList) {
        this.context = context;
        this.features = arrayList;
    }

    public int getCount() {
        return this.features.size();
    }

    public Object getItem(int position) {
        return this.features.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        itemView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_feature, parent, false);
        TextView fareTextView = (TextView) itemView.findViewById(R.id.fare);
        TextView descriptionTextView = (TextView) itemView.findViewById(R.id.featureDesc);
        HashMap item = (HashMap) getItem(position);
        ((TextView) itemView.findViewById(R.id.featureName)).setText((String) item.get(HKRequestIdentifier.kParameterTempleName));
        descriptionTextView.setText((String) item.get("desc"));
        return itemView;
    }
}
