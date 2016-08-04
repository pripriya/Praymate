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
import android.widget.LinearLayout;
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

public class SignUpActivity extends Activity implements HKRequestListener {
    EditText confirmpasswordEditText;
    EditText emailEditText;
    EditText mobileEditText;
    EditText nameEditText;
    EditText passwordEditText;
    LinearLayout progressCircle;
    Button signUpButton;

    /* renamed from: com.geval6.praymate.Core.SignUpActivity.1 */
    class C02101 implements OnClickListener {
        C02101() {
        }

        public void onClick(View arg0) {
            if (SignUpActivity.this.validateInput()) {
                SignUpActivity.this.executeSignUp();
            }
        }
    }

    /* renamed from: com.geval6.praymate.Core.SignUpActivity.2 */
    class C02112 implements DialogInterface.OnClickListener {
        C02112() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.emailEditText = (EditText) findViewById(R.id.emailEditText);
        this.mobileEditText = (EditText) findViewById(R.id.mobileEditText);
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.confirmpasswordEditText = (EditText) findViewById(R.id.confirmpasswordEditText);
        this.progressCircle = (LinearLayout) findViewById(R.id.progressCircle);
        this.signUpButton = (Button) findViewById(R.id.signUpButton);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        addSubmitButtonListeners();
    }

    private boolean validateInput() {
        boolean firstnameFlag = true;
        boolean mobileFlag = true;
        boolean emailFlag = true;
        boolean passwordFlag = true;
        boolean confirmpasswordFlag = true;
        if (this.nameEditText.getText().toString().length() == 0) {
            this.nameEditText.setError("Enter Name");
            firstnameFlag = false;
        } else if (!utlis.isEmail(this.emailEditText.getText().toString())) {
            this.emailEditText.setError("Enter Valid Email");
            emailFlag = false;
        } else if (this.mobileEditText.getText().length() == 0) {
            this.mobileEditText.setError("Enter Mobile");
            mobileFlag = false;
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
        if (firstnameFlag && mobileFlag && emailFlag && passwordFlag && confirmpasswordFlag) {
            return true;
        }
        return false;
    }

    public void addSubmitButtonListeners() {
        this.signUpButton.setOnClickListener(new C02101());
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.fail);
        alertDialog.setButton("OK", new C02112());
        alertDialog.show();
    }

    public void onBeginRequest(HKRequest request) {
        this.progressCircle.setVisibility(View.VISIBLE);
    }

    private void executeSignUp() {
        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterTempleName, this.nameEditText.getText().toString());
        parameters.put(Scopes.EMAIL, this.emailEditText.getText().toString());
        parameters.put(HKRequestIdentifier.kParameterMobile, this.mobileEditText.getText().toString());
        parameters.put(HKRequestIdentifier.kParameterPassword, this.passwordEditText.getText().toString());
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierSignUp, parameters), this).execute(new Void[0]);
    }

    public void onRequestCompleted(HKRequest request) {
        if (request.identifier == HKIdentifier.HKIdentifierSignUp) {
            HashMap response = (HashMap) request.getResponseObject();
            if (response == null || response.getClass() != HashMap.class) {
                Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show();
                finish();
            } else if (response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                response = (HashMap) response.get(HKRequestIdentifier.kParameterResponse);
                showHomeActivity();
            } else {
                Toast.makeText(this, (String) response.get(HKRequestIdentifier.kParameterMessage), 0).show();
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
        this.progressCircle.setVisibility(View.INVISIBLE);
        finish();
    }
}
