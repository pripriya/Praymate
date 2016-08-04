package com.geval6.praymate.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.geval6.praymate.Core.FeatureActivity;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import com.geval6.praymate.Utlis.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;

public class TemplepackageAdapter extends BaseAdapter {
    Context context;
    ImageLoader imageLoader;
    public ArrayList<Object> packages;
    String temple_id;

    class C01921 implements OnClickListener {
        final /* synthetic */ HashMap val$item;

        C01921(HashMap hashMap) {
            this.val$item = hashMap;
        }

        public void onClick(View v) {
            Intent intent1 = ((Activity) TemplepackageAdapter.this.context).getIntent();
            TemplepackageAdapter.this.temple_id = intent1.getStringExtra(HKRequestIdentifier.kParameterUserId);
            Intent intent = new Intent(TemplepackageAdapter.this.context, FeatureActivity.class);
            intent.putExtra("featureId", this.val$item.get(HKRequestIdentifier.kParameterTempleName).toString());
            intent.putExtra(HKRequestIdentifier.kParameterPackageId, this.val$item.get(HKRequestIdentifier.kParameterPackageId).toString());
            intent.putExtra(HKRequestIdentifier.kParameterUserId, TemplepackageAdapter.this.temple_id);
            TemplepackageAdapter.this.context.startActivity(intent);
        }
    }

    public TemplepackageAdapter(Context context, ArrayList<Object> arrayList) {
        this.context = context;
        this.packages = arrayList;
        this.imageLoader = new ImageLoader(context);
    }

    public int getCount() {
        return this.packages.size();
    }

    public Object getItem(int position) {
        return this.packages.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        itemView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_package, parent, false);
        ImageButton imageView = (ImageButton) itemView.findViewById(R.id.image);
        TextView descriptionTextView = (TextView) itemView.findViewById(R.id.packageDesc);
        TextView packageCostDesc = (TextView) itemView.findViewById(R.id.packageCostDesc);
        TextView packageCost = (TextView) itemView.findViewById(R.id.packageCost);
        HashMap item = (HashMap) getItem(position);
        ((TextView) itemView.findViewById(R.id.packageName)).setText((String) item.get(HKRequestIdentifier.kParameterTempleName));
        this.imageLoader.DisplayImage("https://s3-us-west-2.amazonaws.com/praymatebucket/packages/" + item.get("image"), imageView);
        descriptionTextView.setText((String) item.get("description"));
        packageCostDesc.setText((String) item.get("fare_desc"));
        packageCost.setText("\u20b9" + item.get("fare"));
        itemView.setOnClickListener(new C01921(item));
        return itemView;
    }
}
