package com.geval6.praymate.Core;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.geval6.praymate.Adapter.TempleDetailAdapter;
import com.geval6.praymate.Adapter.TempleDetailImageAdapter;
import com.geval6.praymate.Listener.TempleImageListener;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKRequest;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import com.geval6.praymate.RequestManager.HKRequestIdentifier.HKIdentifier;
import com.geval6.praymate.RequestManager.HKRequestListener;
import com.geval6.praymate.RequestManager.HKRequestTask;
import java.util.ArrayList;
import java.util.HashMap;

public class TempleDetailActivity extends Activity implements HKRequestListener, TempleImageInterface, TempleImageListener {
    public HashMap content;
    public ArrayList images;
    ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temple_detail_listview);
        executeGetTempleDetail();
    }

    private void executeGetTempleDetail() {

        Integer temple_id = Integer.valueOf(getIntent().getIntExtra("temple_id", 0));
        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterUserId, "5");
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierTempleDetail, parameters), this).execute(new Void[0]);
    }

    public void onBeginRequest(HKRequest request) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Loading..");
        this.progressDialog.show();
    }

    public void onRequestCompleted(HKRequest request) {
        if (request.identifier == HKIdentifier.HKIdentifierTempleDetail) {
            HashMap response = (HashMap) request.getResponseObject();
            if (response == null || response.getClass() != HashMap.class) {
                Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show();
                finish();
            } else if (response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                HashMap response1 = (HashMap) response.get(HKRequestIdentifier.kParameterResponse);
                showTempleDetail((HashMap) response1.get("content"), (ArrayList) response1.get("order"));
                this.progressDialog.dismiss();
            } else {
                Toast.makeText(this, ((HashMap) response.get(HKRequestIdentifier.kParameterError)).get(HKRequestIdentifier.kParameterMessage).toString(), 0).show();
            }
        }
    }

    public void onRequestFailed(HKRequest request) {
        finish();
    }

    public void showTempleDetail(HashMap content, ArrayList order) {
        ListView listview = (ListView) findViewById(R.id.listview);
        TempleDetailAdapter templeDetailAdapter = new TempleDetailAdapter(this, order, content);
        templeDetailAdapter.templeImageInterface = this;
        listview.setAdapter(templeDetailAdapter);
    }

    public void onImageTapped(ArrayList images, int position) {
        ImageViewFragment imageViewFragment = new ImageViewFragment();
        imageViewFragment.content = this.content;
        imageViewFragment.images = images;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame, imageViewFragment);
        fragmentTransaction.commit();
    }

    public void templeImageTapped(TempleDetailImageAdapter templeListAdapter, int position) {
    }
}
