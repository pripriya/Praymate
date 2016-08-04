package com.geval6.praymate.Core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.geval6.praymate.BuildConfig;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKRequest;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import com.geval6.praymate.RequestManager.HKRequestIdentifier.HKIdentifier;
import com.geval6.praymate.RequestManager.HKRequestListener;
import com.geval6.praymate.RequestManager.HKRequestTask;
import java.util.HashMap;

public class ForgotPasswordActivity extends Activity implements HKRequestListener {
    EditText confirmpasswordEditText;
    EditText passwordEditText;
    Button submitButton;
    EditText tokenEditText;

    class C02011 implements OnClickListener {
        C02011() {
        }

        public void onClick(View arg0) {
            if (ForgotPasswordActivity.this.validateInput()) {
                ForgotPasswordActivity.this.executeForgotPassowrd();
            }
        }
    }

    class C02022 implements DialogInterface.OnClickListener {
        C02022() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        this.tokenEditText = (EditText) findViewById(R.id.tokenEditText);
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.confirmpasswordEditText = (EditText) findViewById(R.id.confirmpasswordEditText);
        this.submitButton = (Button) findViewById(R.id.submitButton);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        addSubmitButtonListeners();
    }

    public void addSubmitButtonListeners() {
        this.submitButton.setOnClickListener(new C02011());
    }

    private boolean validateInput() {
        boolean token = true;
        boolean passwordFlag = true;
        boolean confirmpasswordFlag = true;
        if (this.tokenEditText.getText().toString().length() == 0) {
            this.tokenEditText.setError("Enter Name");
            token = false;
        } else if (this.passwordEditText.getText().toString().length() == 0) {
            this.passwordEditText.setError("Enter Password");
            passwordFlag = false;
        } else if (this.confirmpasswordEditText.getText().toString().length() == 0) {
            this.confirmpasswordEditText.setError("Enter Confirm Password");
            confirmpasswordFlag = false;
        } else if (!this.passwordEditText.getText().toString().equals(this.confirmpasswordEditText.getText().toString())) {
            showAlertDialog(this, BuildConfig.FLAVOR, "Passwords do not match", Boolean.valueOf(false));
            passwordFlag = false;
        }
        if (token && passwordFlag && confirmpasswordFlag) {
            return true;
        }
        return false;
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.fail);
        alertDialog.setButton("OK", new C02022());
        alertDialog.show();
    }

    private void executeForgotPassowrd() {
        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterReset, HKRequestIdentifier.kParameterToken);
        parameters.put(HKRequestIdentifier.kParameterToken, this.tokenEditText.getText().toString());
        parameters.put(HKRequestIdentifier.kParameterChangePassword, this.passwordEditText.getText().toString());
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierForgotResetPassword, parameters), this).execute(new Void[0]);
    }

    public void onBeginRequest(HKRequest request) {
    }

    public void onRequestCompleted(HKRequest request) {
        if (request.identifier == HKIdentifier.HKIdentifierForgotResetPassword) {
            HashMap response = (HashMap) request.getResponseObject();
            if (response == null || response.getClass() != HashMap.class) {
                Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show();
                finish();
            } else if (response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                Toast.makeText(this, ((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get(HKRequestIdentifier.kParameterMessage).toString(), 0).show();
                showHomeActivity();
            } else {
                Toast.makeText(this, ((HashMap) response.get(HKRequestIdentifier.kParameterError)).get(HKRequestIdentifier.kParameterMessage).toString(), 0).show();
            }
        }
    }

    public void onRequestFailed(HKRequest request) {
        finish();
    }

    private void showHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(268468224);
        startActivity(intent);
        finish();
    }
}
