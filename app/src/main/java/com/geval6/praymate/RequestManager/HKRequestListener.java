package com.geval6.praymate.RequestManager;

public interface HKRequestListener {
    void onBeginRequest(HKRequest hKRequest);

    void onRequestCompleted(HKRequest hKRequest);

    void onRequestFailed(HKRequest hKRequest);
}
