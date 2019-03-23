package com.aschimmichanga.readingjsonfromtba_;


import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.extras.Base64;

public class MainActivity extends AppCompatActivity {

    private static String EVENT_KEY = "2019njfla";
    public String url_ = "https://www.thebluealliance.com/api/v3/event/" + EVENT_KEY + "/matches";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        Log.d("URL", "The url has been generated: " + url_);

        HashMap<String, String> params = new HashMap<>();
        params.put("X-TBA-Auth-Key", getString(R.string.TBA_AUTH_KEY));
        //must make your own API Key xml file
        new UpdateTask().execute();
        // create request
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //
                            String jsonResponse = response.toString();

                            Moshi moshi = new Moshi.Builder().build();

                            Type type = Types.newParameterizedType(List.class, Match.class);

                            JsonAdapter<List<Match>> adapter = moshi.adapter(type);

                            Log.d("Status", "The JSON initialization steps are completed");

                            try {
                                List<Match> Matches = adapter.fromJson(jsonResponse);
                                Log.d("Size", Matches.size() + " size");
                                String string = "";
                                for (int i = 0; i < Matches.size(); i++) {
                                    Log.d("Match", Matches.get(i).toString());
                                    string += "Match: " + Matches.get(i).toString();
                                }
                                TextView textView2 = findViewById(R.id.textView2);
                                textView2.setText(string);
                                Log.d("Attempt", "Tried to set text in app");

                            } catch (IOException e) {
                                Log.d("Exception", "The IO Exception has been generated");
                                e.printStackTrace();
                            }
                            VolleyLog.v("Response:%n %s", response.toString());

                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //parsing and logging error message
                    NetworkResponse errorRes = error.networkResponse;
                    String stringData = "";
                    if (errorRes != null && errorRes.data != null) {
                        stringData = new String(errorRes.data, StandardCharsets.UTF_8);
                    }
                    Log.e("Error", stringData);
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    //set headers
                    HashMap<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                String creds = String.format("%s:%s", "username", "password");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("X-TBA-Auth-Key", getString(R.string.TBA_AUTH_KEY));
                    Log.d("HEADERS", "headers have been set lmao");
                    return params;

                }
            };
            requestQueue.add(request);


        }
        private class UpdateTask extends AsyncTask<String, String, String> {
            protected String doInBackground(String... urls) {
                try {
                    URL url = new URL(url_);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            /*String publicKey = "test_key:pass";
            String encodedString = Base64.encodeToString(publicKey.getBytes(), Base64.NO_WRAP);
            String basicAuth = "Basic ".concat(encodedString);
            //setting header for authentication purpose, should use authorization as keyword if you change this it won't work anymore
            urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setRequestProperty("X-TBA-Auth-Key", getString(R.string.TBA_AUTH_KEY));*/

                    //setting other url options
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setConnectTimeout(15000);
                    urlConnection.setReadTimeout(15000);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    String jsonString = sb.toString();
                    Log.d("Output", "JSON: " + jsonString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }