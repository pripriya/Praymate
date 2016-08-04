package com.geval6.praymate.Core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
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

public class TempleSearchActivity extends Activity implements OnQueryTextListener, HKRequestListener, TempleListListener {
    Location location;
    LocationFinder locationFinder;
    LinearLayout progressCircle;
    ProgressDialog progressDialog;
    public SearchView searchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(8);
        setContentView(R.layout.activity_templesearch);
        this.searchView = (SearchView) findViewById(R.id.search_view);
        this.progressCircle = (LinearLayout) findViewById(R.id.progressCircle);
        setupSearchView();
    }

    private void setupSearchView() {
        this.searchView.setIconifiedByDefault(false);
        this.searchView.setOnQueryTextListener(this);
        this.searchView.setQueryHint("Temple search");
    }

    public boolean onQueryTextSubmit(String newText) {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.searchView.getWindowToken(), 0);
        templeSearch();
        return true;
    }

    public boolean onQueryTextChange(String newText) {
        return true;
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.locationFinder = new LocationFinder(this);
        this.location = this.locationFinder.getLocation();
    }

    private void templeSearch() {
        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterTag, Uri.encode(this.searchView.getQuery().toString()));
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierTempleSearch, parameters), this).execute(new Void[0]);
    }

    public void onBeginRequest(HKRequest request) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("loading");
        progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    public void onRequestCompleted(HKRequest request) {
        HashMap response;
        if (request.identifier == HKIdentifier.HKIdentifierTempleSearch) {
            response = (HashMap) request.getResponseObject();
            if (response == null || response.getClass() != HashMap.class) {
                if (checkInternetConnection()) {
                    Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                this.progressDialog.dismiss();
            } else if (response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                ArrayList templeList = new ArrayList();
                if (((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get("temples").getClass() == HashMap.class) {
                    templeList.add(((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get("temples"));
                } else {
                    templeList = (ArrayList) ((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get("temples");
                }
                showTempleList(templeList);
            } else {
                Toast.makeText(this, (String) response.get(HKRequestIdentifier.kParameterMessage), 0).show();
                ((ListView) findViewById(R.id.listview)).setAdapter(null);
                this.progressDialog.dismiss();
            }
        } else if (request.identifier == HKIdentifier.HKIdentifierTempleDetail) {
            response = (HashMap) request.getResponseObject();
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

    public void onRequestFailed(HKRequest request) {
        finish();
    }

    private void showTempleList(ArrayList templeList) {
        ListView listView = (ListView) findViewById(R.id.listview);
        TempleListAdapter adapter = new TempleListAdapter(this, templeList);
        adapter.templeListListener = this;
        adapter.location = this.location;
        adapter.sortedListWithDistance();
        listView.setAdapter(adapter);
        this.progressDialog.dismiss();
    }

    public void templeItemSelectedAtPosition(TempleListAdapter templeListAdapter, int position) {
        HashMap temple = (HashMap) templeListAdapter.getItem(position);
        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterUserId, temple.get(HKRequestIdentifier.kParameterUserId).toString());
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierTempleDetail, parameters), this).execute(new Void[0]);
    }

    public void templeItemToggleFavouriteAtPosition(TempleDetailAdapter templeListAdapter, int position) {
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }
}
