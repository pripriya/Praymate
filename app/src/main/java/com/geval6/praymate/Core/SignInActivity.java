package com.geval6.praymate.Core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.geval6.praymate.BuildConfig;
import com.geval6.praymate.R;
import com.geval6.praymate.RegularExpression.utlis;
import com.geval6.praymate.RequestManager.HKRequest;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import com.geval6.praymate.RequestManager.HKRequestIdentifier.HKIdentifier;
import com.geval6.praymate.RequestManager.HKRequestListener;
import com.geval6.praymate.RequestManager.HKRequestTask;
import com.google.android.gms.common.Scopes;
import java.util.HashMap;

public class SignInActivity extends Activity implements HKRequestListener {
    TextView forgotpasswordtxt;
    EditText passwordEditText;
    LinearLayout progressCircle;
    CheckBox rememberUsernameCheckBox;
    Button signInButton;
    EditText usernameEditText;

    /* renamed from: com.geval6.praymate.Core.SignInActivity.1 */
    class C02081 implements OnClickListener {
        C02081() {
        }

        public void onClick(View arg0) {
            if (SignInActivity.this.validateInput()) {
                SignInActivity.this.executeSignIn();
            } else {
                SignInActivity.this.showAlertDialog(SignInActivity.this, BuildConfig.FLAVOR, "Please don't leave the fields blank", Boolean.valueOf(false));
            }
        }
    }

    /* renamed from: com.geval6.praymate.Core.SignInActivity.2 */
    class C02092 implements DialogInterface.OnClickListener {
        C02092() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.signInButton = (Button) findViewById(R.id.signInButton);
        this.rememberUsernameCheckBox = (CheckBox) findViewById(R.id.rememberUsernameCheckBox);
        this.progressCircle = (LinearLayout) findViewById(R.id.progressCircle);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        addSubmitButtonListeners();
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences", 0);
        if (sharedPreferences.getBoolean("IsUsernameRemember", false)) {
            this.usernameEditText.setText(sharedPreferences.getString("Username", BuildConfig.FLAVOR));
            this.rememberUsernameCheckBox.setChecked(sharedPreferences.getBoolean("IsUsernameRemember", false));
        }
    }

    private boolean validateInput() {
        return utlis.isEmail(this.usernameEditText.getText().toString()) && this.passwordEditText.getText().length() > 0;
    }

    public void addSubmitButtonListeners() {
        this.signInButton.setOnClickListener(new C02081());
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.fail);
        alertDialog.setButton("OK", new C02092());
        alertDialog.show();
    }

    public void onBeginRequest(HKRequest request) {
        this.progressCircle.setVisibility(View.VISIBLE);
    }

    public void onRequestCompleted(HKRequest request) {
        HashMap response;
        if (request.identifier == HKIdentifier.HKIdentifierSignIn) {
            response = (HashMap) request.getResponseObject();
            if (response == null || response.getClass() != HashMap.class) {
                Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show();
                finish();
            } else if (response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                response = (HashMap) response.get(HKRequestIdentifier.kParameterResponse);

                Editor editor = getSharedPreferences("LoginPreferences", 0).edit();
                editor.putBoolean("IsUserLoggedIn", true);
                editor.putBoolean("IsUsernameRemember", this.rememberUsernameCheckBox.isChecked());
                editor.putString("Username", this.usernameEditText.getText().toString());
                editor.commit();

                showHomeActivity();
            } else {
                Toast.makeText(this, (String) response.get(HKRequestIdentifier.kParameterMessage), 0).show();
            }
        } else if (request.identifier == HKIdentifier.HKIdentifierForgotPassword) {
            response = (HashMap) request.getResponseObject();
            if (response == null || response.getClass() != HashMap.class) {
                Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show();
                finish();
            } else if (response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                Toast.makeText(this, ((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get(HKRequestIdentifier.kParameterMessage).toString(), 0).show();
                startActivity(new Intent(this, ForgotPasswordActivity.class));
            } else {
                Toast.makeText(this, ((HashMap) response.get(HKRequestIdentifier.kParameterError)).get(HKRequestIdentifier.kParameterMessage).toString(), 0).show();
            }
        }
    }

    public void onRequestFailed(HKRequest request) {
        finish();
    }

    private void executeSignIn() {
        HashMap<String, String> parameters = new HashMap();
        parameters.put(Scopes.EMAIL, this.usernameEditText.getText().toString());
        parameters.put(HKRequestIdentifier.kParameterPassword, this.passwordEditText.getText().toString());
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierSignIn, parameters), this).execute(new Void[0]);
    }

    public void onForgotActionButtonTapped() {
        if (utlis.isEmail(this.usernameEditText.getText().toString())) {
            HashMap<String, String> parameters = new HashMap();
            parameters.put(Scopes.EMAIL, this.usernameEditText.getText().toString());
            new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierForgotPassword, parameters), this).execute();
            return;
        }
        showAlertDialog(this, BuildConfig.FLAVOR, "Please enter Email", Boolean.valueOf(false));
    }

    private void showHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(268468224);
        startActivity(intent);
        this.progressCircle.setVisibility(View.INVISIBLE);
        finish();
    }
}
