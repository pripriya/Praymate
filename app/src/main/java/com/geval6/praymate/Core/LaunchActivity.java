package com.geval6.praymate.Core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.geval6.praymate.R;

public class LaunchActivity extends Activity {
    Boolean isInternetPresent;
    Button signInButton;
    Button signUpButton;

    /* renamed from: com.geval6.praymate.Core.LaunchActivity.1 */
    class C02061 implements OnClickListener {
        final /* synthetic */ Context val$context;

        C02061(Context context) {
            this.val$context = context;
        }

        public void onClick(View arg0) {
            if (LaunchActivity.this.checkInternetConnection()) {
                LaunchActivity.this.startActivity(new Intent(this.val$context, SignInActivity.class));
                return;
            }
            Toast.makeText(LaunchActivity.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    /* renamed from: com.geval6.praymate.Core.LaunchActivity.2 */
    class C02072 implements OnClickListener {
        final /* synthetic */ Context val$context;

        C02072(Context context) {
            this.val$context = context;
        }

        public void onClick(View arg0) {
            if (LaunchActivity.this.checkInternetConnection()) {
                LaunchActivity.this.startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                return;
            }
            Toast.makeText(LaunchActivity.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public LaunchActivity() {
        this.isInternetPresent = Boolean.valueOf(false);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        this.signInButton = (Button) findViewById(R.id.signin_button);
        this.signInButton.setOnClickListener(new C02061(this));
        this.signUpButton = (Button) findViewById(R.id.signup_button);
        this.signUpButton.setOnClickListener(new C02072(this));
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }
}
