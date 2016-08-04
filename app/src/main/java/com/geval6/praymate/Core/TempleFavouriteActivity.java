package com.geval6.praymate.Core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.geval6.praymate.Adapter.TempleDetailAdapter;
import com.geval6.praymate.Adapter.TempleListAdapter;
import com.geval6.praymate.Listener.TempleListListener;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKRequest;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import com.geval6.praymate.RequestManager.HKRequestIdentifier.HKIdentifier;
import com.geval6.praymate.RequestManager.HKRequestListener;
import com.geval6.praymate.RequestManager.HKRequestTask;
import com.geval6.praymate.Utlis.LocationFinder;
import com.geval6.praymate.Utlis.SharedPreference;
import java.util.ArrayList;
import java.util.HashMap;

public class TempleFavouriteActivity extends Activity implements TempleListListener, HKRequestListener {
    public static final String ARG_ITEM_ID = "favorite_list";
    Activity activity;
    public Location location;
    LocationFinder locationFinder;
    LinearLayout progressCircle;
    ProgressDialog progressDialog;
    public SharedPreference sharedPreference;
    public ArrayList templeList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        this.progressCircle = (LinearLayout) findViewById(R.id.progressCircle);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.sharedPreference = new SharedPreference();
        this.locationFinder = new LocationFinder(this);
        this.location = this.locationFinder.getLocation();
        executeTempleFavourite();
    }

    private void executeTempleFavourite() {
        this.progressCircle.setVisibility(View.VISIBLE);
        this.templeList = this.sharedPreference.getFavorites(this);
        if (this.templeList == null) {
            ((TextView) findViewById(R.id.internetConnection)).setText("No favourite temples");
            this.progressCircle.setVisibility(View.INVISIBLE);
        } else if (this.templeList.size() == 0) {
            ((TextView) findViewById(R.id.internetConnection)).setText("No favourite temples");
            this.progressCircle.setVisibility(View.INVISIBLE);
        }
        if (this.templeList != null) {
            Log.i("&&&&", this.templeList.toString());
            showTempleList(this.templeList);
            this.progressCircle.setVisibility(View.INVISIBLE);
        }
    }

    public void onBeginRequest(HKRequest request) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("loading");
        this.progressDialog.show();
    }

    public void onRequestFailed(HKRequest request) {
    }

    public void onRequestCompleted(HKRequest request) {
        if (request.identifier == HKIdentifier.HKIdentifierTempleDetail) {
            HashMap response = (HashMap) request.getResponseObject();
            if (response != null && response.getClass() == HashMap.class && response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                String response3 = ((HashMap) ((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get("content")).get(HKRequestIdentifier.kParameterUserId).toString();
                Intent intent = new Intent(this, _TempleActivity.class);
                intent.putExtra(HKRequestIdentifier.kParameterResponse, request.responseString);
                intent.putExtra(HKRequestIdentifier.kParameterUserId, response3);
                startActivity(intent);
                this.progressDialog.dismiss();
            }
        }
    }

    private void showTempleList(ArrayList templeList) {
        ListView listView = (ListView) findViewById(R.id.listview);
        TempleListAdapter adapter = new TempleListAdapter(this, templeList);
        adapter.templeListListener = this;
        adapter.location = this.location;
        adapter.sortedListWithDistance();
        listView.setAdapter(adapter);
    }

    public void templeItemToggleFavouriteAtPosition(TempleDetailAdapter templeListAdapter, int position) {
    }

    public void templeItemSelectedAtPosition(TempleListAdapter templeListAdapter, int position) {
        HashMap temple = (HashMap) templeListAdapter.getItem(position);
        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterUserId, temple.get(HKRequestIdentifier.kParameterUserId).toString());
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierTempleDetail, parameters), this).execute(new Void[0]);
    }
}
