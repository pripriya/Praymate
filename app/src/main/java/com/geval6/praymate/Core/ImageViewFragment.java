package com.geval6.praymate.Core;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.geval6.praymate.Listener.TempleImageListener;
import com.geval6.praymate.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageViewFragment extends Fragment {
    public HashMap content;
    Context context;
    public ArrayList images;
    public TempleImageListener templeImageListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.templeimageadapter, container, false);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        return rootView;
    }
}
