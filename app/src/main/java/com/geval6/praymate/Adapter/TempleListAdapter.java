package com.geval6.praymate.Adapter;

import android.content.Context;
import android.location.Location;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.geval6.praymate.BuildConfig;
import com.geval6.praymate.Listener.TempleListListener;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import com.geval6.praymate.Utlis.ImageLoader;
import com.geval6.praymate.Utlis.SharedPreference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class TempleListAdapter extends BaseAdapter {
    Context context;
    private SparseBooleanArray enabledItems;
    ImageLoader imageLoader;
    ListView listView;
    public ArrayList<Object> listViewItems;
    public Location location;
    SharedPreference sharedPreference;
    public TempleListListener templeListListener;

    class C01881 implements OnClickListener {
        final /* synthetic */ int val$position;

        C01881(int i) {
            this.val$position = i;
        }

        public void onClick(View v) {
            TempleListAdapter.this.templeListListener.templeItemSelectedAtPosition(TempleListAdapter.this, this.val$position);
        }
    }

    class C01892 implements OnClickListener {
        final /* synthetic */ HashMap val$hashMap;

        C01892(HashMap hashMap) {
            this.val$hashMap = hashMap;
        }

        public void onClick(View v) {
            if (TempleListAdapter.this.checkFavoriteItem(this.val$hashMap)) {
                TempleListAdapter.this.sharedPreference.removeFavourite(TempleListAdapter.this.context, this.val$hashMap);
                Toast.makeText(TempleListAdapter.this.context, TempleListAdapter.this.context.getResources().getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
            } else {
                TempleListAdapter.this.sharedPreference.addFavourite(TempleListAdapter.this.context, this.val$hashMap);
                Toast.makeText(TempleListAdapter.this.context, TempleListAdapter.this.context.getResources().getString(R.string.add_favr), Toast.LENGTH_SHORT).show();
            }
            TempleListAdapter.this.notifyDataSetChanged();
        }
    }

    class C01903 implements Comparator<Object> {
        C01903() {
        }

        public int compare(Object lhs, Object rhs) {
            return TempleListAdapter.this.getDistanceFromTempleToCurrentLocation(TempleListAdapter.this.location, (HashMap) lhs) < TempleListAdapter.this.getDistanceFromTempleToCurrentLocation(TempleListAdapter.this.location, (HashMap) rhs) ? -1 : 1;
        }
    }

    public TempleListAdapter(Context context, ArrayList<Object> arrayList) {
        this.enabledItems = new SparseBooleanArray();
        this.context = context;
        this.listViewItems = arrayList;
        this.imageLoader = new ImageLoader(context);
        this.sharedPreference = new SharedPreference();
    }

    public int getCount() {
        return this.listViewItems.size();
    }

    public Object getItem(int position) {
        return this.listViewItems.get(position);
    }

    public long getItemId(int position) {
        return Long.parseLong(((HashMap) getItem(position)).get(HKRequestIdentifier.kParameterUserId).toString());
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View templeView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_item, parent, false);
        TextView templeAreaTextView = (TextView) templeView.findViewById(R.id.temple_area);
        TextView templeLatitudeTextView = (TextView) templeView.findViewById(R.id.temple_latitude);
        ImageView favourites = (ImageView) templeView.findViewById(R.id.favourites);
        ImageView imageView = (ImageView) templeView.findViewById(R.id.flag);
        HashMap hashMap = (HashMap) getItem(position);
        ((TextView) templeView.findViewById(R.id.temple_name)).setText(hashMap.get(HKRequestIdentifier.kParameterTempleName).toString());
        templeAreaTextView.setText(hashMap.get(HKRequestIdentifier.kParameterTempleArea).toString());
        if (this.location != null) {
            double roundOff = ((double) Math.round((getDistanceFromTempleToCurrentLocation(this.location, hashMap) / 1000.0d) * 100.0d)) / 100.0d;
            if (roundOff < 1.0d) {
                templeLatitudeTextView.setText(String.valueOf(roundOff) + "meters");
            } else {
                templeLatitudeTextView.setText(String.valueOf(roundOff) + "km");
            }
        } else {
            templeLatitudeTextView.setText("-");
        }
        this.imageLoader.DisplayImage("https://s3-us-west-2.amazonaws.com/praymatebucket/thumbs/" + hashMap.get("thumbs"), imageView);
        templeView.setOnClickListener(new C01881(position));
        favourites.setOnClickListener(new C01892(hashMap));
        if (checkFavoriteItem(hashMap)) {
            favourites.setImageResource(R.drawable.darkred);
        } else {
            favourites.setImageResource(R.drawable.lightredfavourite);
        }
        return templeView;
    }

    public boolean isEnabled(int position) {
        return false;
    }

    public double getDistanceBetweenCoordinates(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            return 0.0d;
        }
        return (double) location1.distanceTo(location2);
    }

    public double getDistanceFromTempleToCurrentLocation(Location currentLocation, HashMap temples) {
        Location location = new Location(BuildConfig.FLAVOR);
        try {
            location.setLatitude(Double.valueOf(temples.get(HKRequestIdentifier.kParameterLatitude).toString()).doubleValue());
            location.setLongitude(Double.valueOf(temples.get(HKRequestIdentifier.kParameterLongitude).toString()).doubleValue());
        } catch (NumberFormatException e) {
        }
        return getDistanceBetweenCoordinates(currentLocation, location);
    }

    public void sortedListWithDistance() {
        Collections.sort(this.listViewItems, new C01903());
    }

    public boolean checkFavoriteItem(HashMap checkTemple) {
        ArrayList favorites = this.sharedPreference.getFavorites(this.context);
        if (favorites == null) {
            return false;
        }
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).equals(checkTemple)) {
                return true;
            }
        }
        return false;
    }
}
