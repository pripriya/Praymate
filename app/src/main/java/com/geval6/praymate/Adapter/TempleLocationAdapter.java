package com.geval6.praymate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import java.util.HashMap;

public class TempleLocationAdapter extends BaseAdapter {
    HashMap content;
    Context context;

    /* renamed from: com.geval6.praymate.Adapter.TempleLocationAdapter.1 */
    class C01911 implements OnClickListener {
        C01911() {
        }

        public void onClick(View v) {
            String latitude = TempleLocationAdapter.this.content.get(HKRequestIdentifier.kParameterLatitude).toString();
            String longitude = TempleLocationAdapter.this.content.get(HKRequestIdentifier.kParameterLongitude).toString();
            String name = TempleLocationAdapter.this.content.get(HKRequestIdentifier.kParameterTempleName).toString();
            TempleLocationAdapter.this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("geo:<lat><long>?q=<" + latitude + ">,<" + longitude + ">(" + name + "),(" + TempleLocationAdapter.this.content.get(HKRequestIdentifier.kParameterTempleArea).toString() + ")")));
        }
    }

    public TempleLocationAdapter(Context context, HashMap content) {
        this.context = context;
        this.content = content;
    }

    public int getCount() {
        return 1;
    }

    public Object getItem(int position) {
        return this.content.get(Integer.valueOf(position));
    }

    public long getItemId(int position) {
        return 1;
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        itemView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.temple_location_adapter, parent, false);
        TextView templearea = (TextView) itemView.findViewById(R.id.temple_area);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.locationImage);
        ((TextView) itemView.findViewById(R.id.temple_name)).setText((String) this.content.get(HKRequestIdentifier.kParameterTempleName));
        templearea.setText((String) this.content.get(HKRequestIdentifier.kParameterTempleArea));
        imageView.setOnClickListener(new C01911());
        return itemView;
    }
}
