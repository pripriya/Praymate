package com.geval6.praymate.Core;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.geval6.praymate.Adapter.ExtraItemsAdapter;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKFunctions;
import com.geval6.praymate.RequestManager.HKRequest;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import com.geval6.praymate.RequestManager.HKRequestIdentifier.HKIdentifier;
import com.geval6.praymate.RequestManager.HKRequestListener;
import com.geval6.praymate.RequestManager.HKRequestTask;
import com.geval6.praymate.Utlis.DatePickerFragment;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AddPeopleActivity extends Activity implements HKRequestListener {
    private OnClickListener ConfirmTripButton;
    private OnClickListener cancel_button_click_listener;
    Button confirmTrip;
    EditText enterDate;
    ExtraItemsAdapter extraItemsAdapter;
    OnDateSetListener ondate;
    PopupWindow popupWindow;
    Spinner spinner;

    class C01931 implements OnClickListener {
        C01931() {
        }

        public void onClick(View v) {
            AddPeopleActivity.this.showDatePicker();
        }
    }

    /* renamed from: com.geval6.praymate.Core.AddPeopleActivity.2 */
    class C01952 implements OnClickListener {
        final /* synthetic */ RecyclerView val$items;

        /* renamed from: com.geval6.praymate.Core.AddPeopleActivity.2.1 */
        class C01941 implements OnClickListener {
            final /* synthetic */ View val$popupView;

            C01941(View view) {
                this.val$popupView = view;
            }

            public void onClick(View v) {
                EditText name = (EditText) this.val$popupView.findViewById(R.id.nameEditText);
                EditText age = (EditText) this.val$popupView.findViewById(R.id.ageEditText);
                RadioGroup gender = (RadioGroup) this.val$popupView.findViewById(R.id.radio);
                RadioButton male = (RadioButton) this.val$popupView.findViewById(R.id.male);
                RadioButton female = (RadioButton) this.val$popupView.findViewById(R.id.female);
                AddPeopleActivity.this.popupWindow.dismiss();
                AddPeopleActivity.this.extraItemsAdapter = (ExtraItemsAdapter) C01952.this.val$items.getAdapter();
                HashMap item = new HashMap();
                item.put(HKRequestIdentifier.kParameterTempleName, name.getText());
                item.put("age", age.getText());
                if (male.isChecked()) {
                    item.put("gender", "M");
                } else if (female.isChecked()) {
                    item.put("gender", "F");
                }
                AddPeopleActivity.this.extraItemsAdapter.addPerson(item);
            }
        }

        C01952(RecyclerView recyclerView) {
            this.val$items = recyclerView;
        }

        public void onClick(View arg0) {
            View popupView = ((LayoutInflater) AddPeopleActivity.this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_addpeople, null);
            Button addPeopleButton = (Button) popupView.findViewById(R.id.addPeopleButton);
            ((ImageButton) popupView.findViewById(R.id.closeButton)).setOnClickListener(AddPeopleActivity.this.cancel_button_click_listener);
            AddPeopleActivity.this.popupWindow = new PopupWindow(popupView, -2, -2, true);
            AddPeopleActivity.this.popupWindow.showAtLocation(popupView, 17, 0, 0);
            addPeopleButton.setOnClickListener(new C01941(popupView));
        }
    }

    /* renamed from: com.geval6.praymate.Core.AddPeopleActivity.3 */
    class C01963 implements OnClickListener {
        C01963() {
        }

        public void onClick(View v) {
            AddPeopleActivity.this.popupWindow.dismiss();
        }
    }

    /* renamed from: com.geval6.praymate.Core.AddPeopleActivity.4 */
    class C01974 implements OnClickListener {
        C01974() {
        }

        public void onClick(View v) {
            AddPeopleActivity.this.executeCost();
        }
    }

    /* renamed from: com.geval6.praymate.Core.AddPeopleActivity.5 */
    class C01985 implements DialogInterface.OnClickListener {
        C01985() {
        }

        public void onClick(DialogInterface dialog, int which) {
            AddPeopleActivity.this.startActivity(new Intent(AddPeopleActivity.this, PaymentActivity.class));
        }
    }

    /* renamed from: com.geval6.praymate.Core.AddPeopleActivity.6 */
    class C01996 implements OnDateSetListener {
        C01996() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            AddPeopleActivity.this.enterDate.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year));
            AddPeopleActivity.this.executeTime();
        }
    }

    public AddPeopleActivity() {
        this.cancel_button_click_listener = new C01963();
        this.ConfirmTripButton = new C01974();
        this.ondate = new C01996();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpeople);
        this.confirmTrip = (Button) findViewById(R.id.confirmTrip);
        this.enterDate = (EditText) findViewById(R.id.enterDateEditText);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        this.enterDate.setOnClickListener(new C01931());
        this.confirmTrip.setOnClickListener(this.ConfirmTripButton);
        RecyclerView items = (RecyclerView) findViewById(R.id.listview);
        LinearLayoutManager shopItemslayoutManager = new LinearLayoutManager(getApplicationContext());
        shopItemslayoutManager.setOrientation(0);
        items.setLayoutManager(shopItemslayoutManager);
        this.extraItemsAdapter = new ExtraItemsAdapter(this, new ArrayList());
        items.setAdapter(this.extraItemsAdapter);
        ((ImageButton) findViewById(R.id.addPeopleImage)).setOnClickListener(new C01952(items));
    }

    private void executeCost() {
        String personCount = String.valueOf(this.extraItemsAdapter.getItemCount());
        Log.i("Count", personCount);
        Intent intent = getIntent();
        String pid = intent.getStringExtra(HKRequestIdentifier.kParameterPackageId);
        String tid = intent.getStringExtra(HKRequestIdentifier.kParameterTempleID);
        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterPackageId, pid.toString());
        parameters.put(HKRequestIdentifier.kParameterTempleID, tid.toString());
        parameters.put(HKRequestIdentifier.kParameterCount, personCount);
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierPackageCost, parameters), this).execute(new Void[0]);
    }

    public void onBeginRequest(HKRequest request) {
    }

    public void onRequestCompleted(HKRequest request) {
        HashMap response;
        if (request.identifier == HKIdentifier.HKIdentifierPackageCost) {
            response = (HashMap) request.getResponseObject();
            if (response != null && response.getClass() == HashMap.class) {
                if (response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                    alertDialog((Integer) ((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get("cost"));
                    return;
                }
                Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (request.identifier == HKIdentifier.HKIdentifierTime) {
            response = (HashMap) request.getResponseObject();
            if (response != null && response.getClass() == HashMap.class) {
                if (response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                    ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.activity_spinner_textview, (ArrayList) HKFunctions.objectFromJson(((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get("timings").toString()));
                    adapter.setDropDownViewResource(R.layout.layout_spinner);
                    this.spinner.setAdapter(adapter);
                    return;
                }
                Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void alertDialog(Integer cost) {
        new Builder(this).setIcon(R.drawable.warningimage).setTitle("Exit").setMessage("Estimated Cost will be: \u20b9" + cost + "\n" + "Do you want to continue..").setPositiveButton("Yes", new C01985()).setNegativeButton("No", null).show();
    }

    public void onRequestFailed(HKRequest request) {
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(this.ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    private void executeTime() {
        Intent intent = getIntent();
        String pid = intent.getStringExtra(HKRequestIdentifier.kParameterPackageId);
        String tid = intent.getStringExtra(HKRequestIdentifier.kParameterTempleID);
        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterPackageId, pid.toString());
        parameters.put(HKRequestIdentifier.kParameterTempleID, tid.toString());
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierTime, parameters), this).execute(new Void[0]);
    }
}
