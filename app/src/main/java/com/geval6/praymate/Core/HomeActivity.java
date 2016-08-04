package com.geval6.praymate.Core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.geval6.praymate.Adapter.HomeAdapter;
import com.geval6.praymate.R;
import com.geval6.praymate.Utlis.LocationFinder;

public class HomeActivity extends Activity {

    /* renamed from: com.geval6.praymate.Core.HomeActivity.1 */
    class C02041 implements OnClickListener {
        final /* synthetic */ ListView val$listView;

        /* renamed from: com.geval6.praymate.Core.HomeActivity.1.1 */
        class C02031 implements DialogInterface.OnClickListener {
            C02031() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }

        C02041(ListView listView) {
            this.val$listView = listView;
        }

        public void onClick(View view) {
            for (int i = 0; i < this.val$listView.getChildCount(); i++) {
                if (view == this.val$listView.getChildAt(i).findViewById(R.id.signOutButton)) {
                    if (HomeActivity.this.checkInternetConnection()) {
                        Editor editor = HomeActivity.this.getSharedPreferences("LoginPreferences", 0).edit();
                        editor.putBoolean("IsUserLoggedIn", false);
                        editor.commit();
                        Toast.makeText(HomeActivity.this.getApplicationContext(), "Log Out Successful", Toast.LENGTH_SHORT).show();
                        HomeActivity.this.finish();
                        HomeActivity.this.startActivity(new Intent(HomeActivity.this, LaunchActivity.class));
                    } else {
                        Toast.makeText(HomeActivity.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                if (view == this.val$listView.getChildAt(i).findViewById(R.id.searchTextView)) {
                    HomeActivity.this.startActivity(new Intent(HomeActivity.this, TempleSearchActivity.class));
                }
                if (view == this.val$listView.getChildAt(i).findViewById(R.id.nearByTemples)) {
                    boolean bCheckInternetConnectivity = HomeActivity.this.checkInternetConnection();
                    LocationFinder locationFinder = new LocationFinder(HomeActivity.this);
                    if (!bCheckInternetConnectivity) {
                        Toast.makeText(HomeActivity.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    } else if (!locationFinder.canGetLocation() || locationFinder.getLatitude() == 0.0d || locationFinder.getLongitude() == 0.0d) {
                        AlertDialog alertDialog = new Builder(HomeActivity.this).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("Current location not available");
                        alertDialog.setButton(-3, "OK", new C02031());
                        alertDialog.show();
                    } else {
                        HomeActivity.this.startActivity(new Intent(HomeActivity.this, TempleListActivity.class));
                    }
                }
                if (view == this.val$listView.getChildAt(i).findViewById(R.id.favourites)) {
                    HomeActivity.this.startActivity(new Intent(HomeActivity.this, TempleFavouriteActivity.class));
                }
            }
        }
    }

    /* renamed from: com.geval6.praymate.Core.HomeActivity.2 */
    class C02052 implements DialogInterface.OnClickListener {
        C02052() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.setFlags(268435456);
            HomeActivity.this.startActivity(intent);
            System.exit(0);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ListView listView = (ListView) findViewById(R.id.listview);
        HomeAdapter homeAdapter = new HomeAdapter(this, null);
        homeAdapter.setButtonListener(new C02041(listView));
        listView.setAdapter(homeAdapter);
    }

    public void onBackPressed() {
        new Builder(this).setIcon(R.drawable.warningimage).setTitle("Exit").setMessage("Do you want to exit application?").setPositiveButton("Yes", new C02052()).setNegativeButton("No", null).show();
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }
}
