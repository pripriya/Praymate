package com.geval6.praymate.RequestManager;

import android.os.AsyncTask;
import android.util.Log;
import com.geval6.praymate.RequestManager.HKRequestIdentifier.HKIdentifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HKRequestTask extends AsyncTask<Void, Void, HKRequest> {
    private HKRequestListener listener;
    private HKRequest request;

    public HKRequestTask(HKRequest request, HKRequestListener listener) {
        this.request = request;
        this.listener = listener;
    }

    protected HKRequest doInBackground(Void... voids) {
        try {

            HttpResponse response = new DefaultHttpClient().execute(getHttpRequestBase(this.request));
            this.request.contentType = response.getEntity().getContentType().getValue();
            this.request.responseString = stringFromInputStream(response.getEntity().getContent());
            Log.i("PARAMETERS:", this.request.parameters.toString());
            Log.i("RESPONSE:", this.request.responseString);

        } catch (Exception ex) {
            Log.i("Exception:", ex.toString());
        }
        return this.request;
    }

    protected void onPreExecute() {
        this.listener.onBeginRequest(this.request);
        super.onPreExecute();
    }

    protected void onPostExecute(HKRequest request) {
        this.listener.onRequestCompleted(request);
        super.onPostExecute(request);
    }

    protected void onCancelled(HKRequest request) {
        this.listener.onRequestFailed(request);
        super.onCancelled(request);
    }

    private HttpRequestBase getHttpRequestBase(HKRequest request) {
        if (HKRequestIdentifier.httpMethodForIdentifier(request.identifier).equalsIgnoreCase("GET")) {
            return new HttpGet("http://52.32.11.69/praymate/" + HKRequestIdentifier.pageForIdentifier(request.identifier, request.parameters) + parameterStringForIdentifier(request.identifier, request.parameters));
        }
        if (!HKRequestIdentifier.httpMethodForIdentifier(request.identifier).equalsIgnoreCase("POST")) {
            return null;
        }
        HttpPost httpPost = new HttpPost("http://52.32.11.69/praymate/" + HKRequestIdentifier.pageForIdentifier(request.identifier, request.parameters));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairsListForIdentifier(request.identifier, request.parameters)));
            return httpPost;
        } catch (Exception e) {
            return httpPost;
        }
    }

    private String parameterStringForIdentifier(HKIdentifier identifier, HashMap parameters) {
        String parameterString = "?";
        for (String parameter : HKRequestIdentifier.parametersForIdentifier(identifier)) {
            parameterString = parameterString + parameter + "=" + parameters.get(parameter) + "&";
        }
        return parameterString.substring(0, parameterString.length() - 1);
    }

    private List<NameValuePair> nameValuePairsListForIdentifier(HKIdentifier identifier, HashMap parameters) throws IOException {
        List<NameValuePair> nameValuePairs = new ArrayList();
        for (String parameter : HKRequestIdentifier.parametersForIdentifier(identifier)) {
            nameValuePairs.add(new BasicNameValuePair(parameter, parameters.get(parameter).toString()));
        }
        return nameValuePairs;
    }

    private String stringFromInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            while (true) {
                String nextLine = reader.readLine();
                if (nextLine == null) {
                    break;
                }
                stringBuilder.append(nextLine);
            }
        } catch (Exception e) {
        }
        return stringBuilder.toString();
    }
}
