package com.geval6.praymate.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.geval6.praymate.Core.TempleImageInterface;
import com.geval6.praymate.Listener.TempleImageListener;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKFunctions;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import com.geval6.praymate.Utlis.ImageLoaderForTempleDetail;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class TempleDetailImageAdapter extends PagerAdapter {
    HashMap content;
    Context context;
    ArrayList images;
    TempleImageInterface templeImageInterface;
    public TempleImageListener templeImageListener;

    class C01871 implements OnClickListener {
        final /* synthetic */ int val$position;

        C01871(int i) {
            this.val$position = i;
        }

        public void onClick(View v) {
            TempleDetailImageAdapter.this.templeImageInterface.onImageTapped(TempleDetailImageAdapter.this.images, this.val$position);
        }
    }

    public TempleDetailImageAdapter(Context context, HashMap content) {
        this.context = context;
        this.content = content;
        this.images = (ArrayList) HKFunctions.objectFromJson(content.get("images").toString());
        Log.i("$$$$$", content.get("images").getClass().toString());
    }

    public Object getItem(int position) {
        return this.images.get(position);
    }

    public int getCount() {
        return this.images.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View viewItem = ((Activity) this.context).getLayoutInflater().inflate(R.layout.image_view_for_temple_detail, container, false);
        ImageView imageView = (ImageView) viewItem.findViewById(R.id.imageView);
        new ImageLoaderForTempleDetail(this.context).DisplayImage("https://s3-us-west-2.amazonaws.com/praymatebucket/images/" + this.content.get(HKRequestIdentifier.kParameterUserId) + "/" + getItem(position), imageView);
        ((ViewPager) container).addView(viewItem);
        String allImage = "https://s3-us-west-2.amazonaws.com/praymatebucket/images/" + this.content.get(HKRequestIdentifier.kParameterUserId) + "/" + getItem(position);
        imageView.setOnClickListener(new C01871(position));
        return viewItem;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
