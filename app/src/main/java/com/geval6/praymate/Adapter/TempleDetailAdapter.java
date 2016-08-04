package com.geval6.praymate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.geval6.praymate.Core.TempleImageInterface;
import com.geval6.praymate.Listener.TempleImageListener;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import java.util.ArrayList;
import java.util.HashMap;

public class TempleDetailAdapter extends BaseAdapter implements TempleImageInterface, TempleImageListener {
    HashMap content;
    Context context;
    ArrayList order;
    public TempleImageInterface templeImageInterface;

    class C01861 implements OnClickListener {
        C01861() {
        }

        public void onClick(View v) {
            String latitude = TempleDetailAdapter.this.content.get(HKRequestIdentifier.kParameterLatitude).toString();
            String longitude = TempleDetailAdapter.this.content.get(HKRequestIdentifier.kParameterLongitude).toString();
            String name = TempleDetailAdapter.this.content.get(HKRequestIdentifier.kParameterTempleName).toString();
            TempleDetailAdapter.this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("geo:<lat><long>?q=<" + latitude + ">,<" + longitude + ">(" + name + "),(" + TempleDetailAdapter.this.content.get(HKRequestIdentifier.kParameterTempleArea).toString() + ")")));
        }
    }

    public TempleDetailAdapter(Context context, ArrayList order, HashMap content) {
        this.context = context;
        this.order = order;
        this.content = content;
    }

    public int getCount() {
        return this.order.size() + 2;
    }

    public Object getItem(int position) {
        if (position >= 2) {
            return this.content.get(this.order.get(position - 2));
        }
        return null;
    }

    public long getItemId(int position) {
        return 1;
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        if (position == 0) {
            itemView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.templeimageadapter, parent, false);
            ViewPager viewPager = (ViewPager) itemView.findViewById(R.id.viewPager);
            TempleDetailImageAdapter templeDetailImageAdapter = new TempleDetailImageAdapter(this.context, this.content);
            templeDetailImageAdapter.templeImageInterface = this.templeImageInterface;
            viewPager.setAdapter(templeDetailImageAdapter);
            return itemView;
        } else if (position == 1) {
            itemView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.temple_location_adapter, parent, false);
            TextView templearea = (TextView) itemView.findViewById(R.id.temple_area);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.locationImage);
            ((TextView) itemView.findViewById(R.id.temple_name)).setText((String) this.content.get(HKRequestIdentifier.kParameterTempleName));
            templearea.setText((String) this.content.get(HKRequestIdentifier.kParameterTempleArea));
            imageView.setOnClickListener(new C01861());
            return itemView;
        } else {
            itemView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.temple_detail_activity, parent, false);
            TextView descriptionTextView = (TextView) itemView.findViewById(R.id.description);
            HashMap item = (HashMap) getItem(position);
            ((TextView) itemView.findViewById(R.id.deities)).setText((String) item.get("label"));
            descriptionTextView.setText((String) item.get("content"));
            return itemView;
        }
    }

    public void onImageTapped(ArrayList images, int position) {
        this.templeImageInterface.onImageTapped(images, position);
    }

    public void templeImageTapped(TempleDetailImageAdapter templeListAdapter, int position) {
    }
}
