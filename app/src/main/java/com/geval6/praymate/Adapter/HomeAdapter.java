package com.geval6.praymate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.geval6.praymate.Core.TempleSearchActivity;
import com.geval6.praymate.R;

public class HomeAdapter extends BaseAdapter {
    Context context;
    public OnClickListener listener;

    class C01851 implements OnClickListener {
        C01851() {
        }

        public void onClick(View v) {
            HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, TempleSearchActivity.class));
        }
    }

    public HomeAdapter(Context context, OnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setButtonListener(OnClickListener listener) {
        this.listener = listener;
    }

    public int getCount() {
        return 1;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View homeView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_home_items, parent, false);
        Button nearByTemples = (Button) homeView.findViewById(R.id.nearByTemples);
        Button favourites = (Button) homeView.findViewById(R.id.favourites);
        Button bookings = (Button) homeView.findViewById(R.id.bookings);
        Button settings = (Button) homeView.findViewById(R.id.settings);
        Button signOutButton = (Button) homeView.findViewById(R.id.signOutButton);
        TextView searchTextView = (TextView) homeView.findViewById(R.id.searchTextView);
        searchTextView.setOnClickListener(new C01851());
        nearByTemples.setOnClickListener(this.listener);
        signOutButton.setOnClickListener(this.listener);
        searchTextView.setOnClickListener(this.listener);
        favourites.setOnClickListener(this.listener);
        return homeView;
    }
}
