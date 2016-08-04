package com.geval6.praymate.Listener;

import com.geval6.praymate.Adapter.TempleDetailAdapter;
import com.geval6.praymate.Adapter.TempleListAdapter;

public interface TempleListListener {
    void templeItemSelectedAtPosition(TempleListAdapter templeListAdapter, int i);

    void templeItemToggleFavouriteAtPosition(TempleDetailAdapter templeDetailAdapter, int i);
}
