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
import android.widget.Toast;
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
import java.util.ArrayList;
import java.util.HashMap;

public class TempleListActivity extends Activity implements HKRequestListener, TempleListListener {
    public Location location;
    LocationFinder locationFinder;
    LinearLayout progressCircle;
    ProgressDialog progressDialog;
    public ArrayList templeList;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        this.progressCircle = (LinearLayout) findViewById(R.id.progressCircle);
    }

    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        this.locationFinder = new LocationFinder(this);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("loading");
        this.progressDialog.show();
        this.location = this.locationFinder.getLocation();
        this.progressDialog.dismiss();
        executeGetTempleNearby();
    }

    private void executeGetTempleNearby() {

        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterLatitude, String.valueOf(this.locationFinder.getLatitude()));
        parameters.put(HKRequestIdentifier.kParameterLongitude, String.valueOf(this.locationFinder.getLongitude()));
        parameters.put(HKRequestIdentifier.kParameterDistance, "4000");
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierTemplesNearby, parameters), this).execute();
    }

    public void onBeginRequest(HKRequest request) {
        progressCircle.setVisibility(View.VISIBLE);
    }

    public void onRequestCompleted(HKRequest request) {
        HashMap response;
        if (request.identifier == HKIdentifier.HKIdentifierTemplesNearby) {
            response = (HashMap) request.getResponseObject();
            if (response == null || response.getClass() != HashMap.class) {
                Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show();
                finish();
            }

            else if (response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                this.templeList = (ArrayList) ((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get("temples");
                showTempleList(this.templeList);
            }
            else {
                ((TextView) findViewById(R.id.internetConnection)).setText((String) response.get(HKRequestIdentifier.kParameterMessage));
                progressCircle.setVisibility(View.INVISIBLE);
            }
         }

        else if (request.identifier == HKIdentifier.HKIdentifierTempleDetail) {
            response = (HashMap) request.getResponseObject();

            if (response != null && response.getClass() == HashMap.class && response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                String response3 = ((HashMap) ((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get("content")).get(HKRequestIdentifier.kParameterUserId).toString();
                Log.i("^^^^^^^^^^^", response3);

                Intent intent = new Intent(this, _TempleActivity.class);
                intent.putExtra(HKRequestIdentifier.kParameterResponse, request.responseString);
                Log.i("RRRRR", request.responseString);
                intent.putExtra(HKRequestIdentifier.kParameterUserId, response3);
                startActivity(intent);
                progressCircle.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void onRequestFailed(HKRequest request) {
    }

    private void showTempleList(ArrayList templeList) {
        ListView listView = (ListView) findViewById(R.id.listview);
        TempleListAdapter adapter = new TempleListAdapter(this, templeList);
        adapter.templeListListener = this;
        adapter.location = this.location;
        adapter.sortedListWithDistance();
        listView.setAdapter(adapter);
        progressCircle.setVisibility(View.INVISIBLE);
    }

    public void onResume() {
        super.onResume();
    }

    public void templeItemSelectedAtPosition(TempleListAdapter templeListAdapter, int position) {
        HashMap temple = (HashMap) templeListAdapter.getItem(position);
        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterUserId, temple.get(HKRequestIdentifier.kParameterUserId).toString());
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierTempleDetail, parameters), this).execute(new Void[0]);
    }

    public void templeItemToggleFavouriteAtPosition(TempleDetailAdapter templeListAdapter, int position) {
    }
}
