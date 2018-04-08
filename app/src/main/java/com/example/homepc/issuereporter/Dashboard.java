package com.example.homepc.issuereporter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

public class Dashboard extends AppCompatActivity {
    TextView t1;
    ScrollView sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_clear_all_black_48dp);
        final String s = getIntent().getStringExtra("Token");
        t1 = (TextView) findViewById(R.id.txt1);
        t1.setMovementMethod(new ScrollingMovementMethod());
        System.out.println(s);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(Dashboard.this,Issue.class);
                intent.putExtra("Token",s);
                startActivity(intent);
            }
        });
        final String LOGIN_URL="http://192.168.43.198:8000/api/issues";
        StringRequest strreq = new StringRequest(Request.Method.GET, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.contains("true")) {
                    try {
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0; i<jsonArray.length();i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String title = jsonObject1.getString("title");
                            String status = jsonObject1.getString("status");
                            t1.append(title);
                            t1.append("\n");
                            t1.append(status);
                            t1.append("\n");
                            t1.append("\n");


                        }

                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                }

                else{
                    Toast.makeText(Dashboard.this,"User does not exist.",Toast.LENGTH_LONG).show();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Dashboard.this,error.toString(),Toast.LENGTH_LONG ).show();;
                        System.out.println(error.toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
                map.put("x-access-token",s);
                return map;
            }
        };

        int socketTimeout = 10000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strreq.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        requestQueue.add(strreq);



    }
}

