package com.geval6.praymate.Core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.geval6.praymate.Adapter.TemplepackageAdapter;
import com.geval6.praymate.R;
import com.geval6.praymate.RequestManager.HKFunctions;
import com.geval6.praymate.RequestManager.HKRequest;
import com.geval6.praymate.RequestManager.HKRequestIdentifier;
import com.geval6.praymate.RequestManager.HKRequestIdentifier.HKIdentifier;
import com.geval6.praymate.RequestManager.HKRequestListener;
import com.geval6.praymate.RequestManager.HKRequestTask;
import com.geval6.praymate.Utlis.ImageLoaderForTempleDetail;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;

public class _TempleActivity extends Activity implements HKRequestListener {
    public static String kTempleResponse = "response";
    public static String kTempleResponseContent = "content";
    public static String kTempleResponseContentImages = "images";
    public static String kTempleResponseOrder = "order";

    public HashMap content;
    public ArrayList images;
    public ArrayList order;
    Button plantrip;
    ProgressDialog progressDialog;
    String temple_id;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temple_detail_listview);
        parseBundle();
        ListView listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(templeDetailAdapter(listview));
        plantrip = (Button) findViewById(R.id.planTrip);
        plantrip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                executeTemplePackage();
            }
        });
    }

    private BaseAdapter templeDetailAdapter(ListView listView) {
        return new BaseAdapter() {
            @Override
            public int getCount() {
                return _TempleActivity.this.order.size() + 2;
            }

            @Override
            public Object getItem(int position) {
                if (position >= 2) {
                    return _TempleActivity.this.content.get(_TempleActivity.this.order.get(position - 2));
                }
                return null;            }

            @Override
            public long getItemId(int i) {
                return 1;
            }
            @Override
            public View getView(int position, View itemView, ViewGroup parent) {
                if (position == 0) {
                    itemView = ((LayoutInflater) _TempleActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.templeimageadapter, parent, false);
                    _TempleActivity.this.setTempleDetailImageAdapter((ViewPager) itemView.findViewById(R.id.viewPager));
                    return itemView;
                }
                else if (position == 1) {
                    itemView = ((LayoutInflater) _TempleActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.temple_location_adapter, parent, false);
                    TextView templearea = (TextView) itemView.findViewById(R.id.temple_area);
                    ImageView imageView = (ImageView) itemView.findViewById(R.id.locationImage);
                    ((TextView) itemView.findViewById(R.id.temple_name)).setText((String) _TempleActivity.this.content.get(HKRequestIdentifier.kParameterTempleName));
                    templearea.setText((String) _TempleActivity.this.content.get(HKRequestIdentifier.kParameterTempleArea));
                    imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String latitude = _TempleActivity.this.content.get(HKRequestIdentifier.kParameterLatitude).toString();
                            String longitude = _TempleActivity.this.content.get(HKRequestIdentifier.kParameterLongitude).toString();
                            String name = _TempleActivity.this.content.get(HKRequestIdentifier.kParameterTempleName).toString();
                            _TempleActivity.this.startActivity(new Intent("android.intent.action.VIEW",Uri.parse("geo:<lat><long>?q=<" + latitude + ">,<" + longitude + ">(" + name + "),(" + _TempleActivity.this.content.get(HKRequestIdentifier.kParameterTempleArea).toString() + ")")));

                        }
                    });
                    return itemView;
                } else {
                    itemView = ((LayoutInflater) _TempleActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.temple_detail_activity, parent, false);
                    TextView descriptionTextView = (TextView) itemView.findViewById(R.id.description);
                    HashMap item = (HashMap) getItem(position);
                    ((TextView) itemView.findViewById(R.id.deities)).setText((String) item.get("label"));
                    descriptionTextView.setText((String) item.get("content"));
                    return itemView;
                }
            }
        };
    }

    private void setTempleDetailImageAdapter(ViewPager viewPager) {
        viewPager.setAdapter(new PagerAdapter() {
            Context context;

            public Object getItem(int position) {

                return images.get(position);
            }

            public int getCount() {
                return images.size();
            }

            public boolean isViewFromObject(View view, Object object) {
                return view == ((View) object);
            }

            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            public Object instantiateItem(ViewGroup container, int position) {
                View viewItem = _TempleActivity.this.getLayoutInflater().inflate(R.layout.image_view_for_temple_detail, container, false);
                ImageView imageView = (ImageView) viewItem.findViewById(R.id.imageView);
                images = (ArrayList) HKFunctions.objectFromJson(_TempleActivity.this.content.get("images").toString());
                new ImageLoaderForTempleDetail(this.context).DisplayImage("https://s3-us-west-2.amazonaws.com/praymatebucket/images/" + _TempleActivity.this.content.get(HKRequestIdentifier.kParameterUserId) + "/" + getItem(position), imageView);
                ((ViewPager) container).addView(viewItem);
                String allImage = "https://s3-us-west-2.amazonaws.com/praymatebucket/images/" + _TempleActivity.this.content.get(HKRequestIdentifier.kParameterUserId) + "/" + getItem(position);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                return viewItem;
            }
        });
    }

    private void parseBundle() {
        HashMap response = (HashMap) ((HashMap) HKFunctions.objectFromJson(getIntent().getStringExtra(HKRequestIdentifier.kParameterResponse))).get(HKRequestIdentifier.kParameterResponse);
        order = (ArrayList) response.get("order");
        content = (HashMap) response.get("content");
        this.images = (ArrayList)(new Gson()).fromJson(content.get(kTempleResponseContentImages).toString(),ArrayList.class);

    }

    private void executeTemplePackage() {
        this.temple_id = getIntent().getStringExtra(HKRequestIdentifier.kParameterUserId);
        HashMap<String, String> parameters = new HashMap();
        parameters.put(HKRequestIdentifier.kParameterTempleID, this.temple_id.toString());
        new HKRequestTask(new HKRequest(HKIdentifier.HKIdentifierTemplePackage, parameters), this).execute();
    }

    public void onBeginRequest(HKRequest request) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("loading");
        progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    public void onRequestCompleted(HKRequest request) {
        if (request.identifier == HKIdentifier.HKIdentifierTemplePackage) {
            HashMap response = (HashMap) request.getResponseObject();
            if (response != null && response.getClass() == HashMap.class) {
                if (response.containsKey(HKRequestIdentifier.kParameterStatus) && Boolean.parseBoolean(response.get(HKRequestIdentifier.kParameterStatus).toString())) {
                    ArrayList packages = (ArrayList) ((HashMap) response.get(HKRequestIdentifier.kParameterResponse)).get("packages");
                    Log.i("%%%%%%", packages.toString());
                    showPackage(packages);
                    return;
                }
                Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void showPackage(ArrayList packages) {
        ListView listView = (ListView) findViewById(R.id.listview);
        this.plantrip.setVisibility(View.INVISIBLE);
        TemplepackageAdapter templepackageAdapter = new TemplepackageAdapter(this, packages);
        new Intent().putExtra(HKRequestIdentifier.kParameterUserId, this.temple_id);
        listView.setAdapter(templepackageAdapter);
        this.progressDialog.dismiss();
    }

    public void onRequestFailed(HKRequest request) {
    }
}
