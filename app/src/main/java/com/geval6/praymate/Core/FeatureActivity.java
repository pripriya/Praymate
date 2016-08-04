package com.geval6.praymate.Core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.geval6.praymate.Adapter.FeatureAdapter;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKRequest;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import com.geval6.praymate.RequestManager.HKRequestIdentifier.HKIdentifier;
import com.geval6.praymate.RequestManager.HKRequestListener;
import com.geval6.praymate.RequestManager.HKRequestTask;
import java.util.ArrayList;
import java.util.HashMap;

public class FeatureActivity extends Activity implements HKRequestListener {
    Button button;
    ProgressDialog progressDialog;
    String temple_id;

    class C02001 implements OnClickListener {
        C02001() {
        }

        public void onClick(View v) {
            Intent intent = new Intent(FeatureActivity.this, AddPeopleActivity.class);
            intent.putExtra(HKRequestIdentifier.kParameterPackageId, FeatureActivity.this.getIntent().getStringExtra(HKRequestIdentifier.kParameterPackageId));
            intent.putExtra(HKRequestIdentifier.kParameterTempleID, FeatureActivity.this.temple_id);
            FeatureActivity.this.startActivity(intent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_feature);
        this.button = (Button) findViewById(R.id.bookTrip);
        this.button.setVisibility(View.VISIBLE);
        executeGetTemplePackage();
    }

    private void executeGetTemplePackage() {
        Intent intent = getIntent();
        this.temple_id = intent.getStringExtra(HKRequestIdentifier.kParameterUserId);
        String feature_id = intent.getStringExtra("featureId");
        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterPackageName, feature_id.toString());
        parameters.put(HKRequestIdentifier.kParameterTempleID, this.temple_id.toString());
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierTempleFeature, parameters), this).execute(new Void[0]);
    }

    public void onBeginRequest(HKRequest request) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("loading");
        this.progressDialog.show();
    }

    public void onRequestCompleted(HKRequest request) {
        if (request.identifier == HKIdentifier.HKIdentifierTempleFeature) {
            HashMap response = (HashMap) request.getResponseObject();
            if (response != null && response.getClass() == HashMap.class) {
                if (response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                    ArrayList features = (ArrayList) ((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get("features");
                    Log.i("%%%%%%", features.toString());
                    showPackage(features);
                    this.button.setVisibility(View.VISIBLE);
                    this.button.setOnClickListener(new C02001());
                    return;
                }
                Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void onRequestFailed(HKRequest request) {
    }

    private void showPackage(ArrayList features) {
        ((ListView) findViewById(R.id.listview)).setAdapter(new FeatureAdapter(this, features));
        this.progressDialog.dismiss();
    }
}
