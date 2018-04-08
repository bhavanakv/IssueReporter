package com.example.homepc.issuereporter;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Issue extends AppCompatActivity {
EditText et1;
    Button bt;
    String title,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        setTitle(R.string.title_activity_issue);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_create_black_48dp);
        et1 =  (EditText) findViewById(R.id.iet);
        bt = (Button) findViewById(R.id.button5);
        final String s1 = getIntent().getStringExtra("Token");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = et1.getText().toString();

                final String LOGIN_URL="http://192.168.43.198:8000/api/issues/new";
                StringRequest strreq = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.contains("Success"))
                        {
                            Toast.makeText(Issue.this, "You are now registered here!!", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(Issue.this,response,Toast.LENGTH_LONG).show();
                        }


                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Issue.this,error.toString(),Toast.LENGTH_LONG ).show();
                                System.out.println(error.toString());
                            }
                        }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
                        map.put("token",s1);
                        return map;
                    }
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("title",title);
                        map.put("org","gugu");
                        map.put("location","haha");
                        map.put("token",s1);
                        return map;
                    }
                };

                int socketTimeout = 10000; // 30 seconds. You can change it
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                strreq.setRetryPolicy(policy);

                RequestQueue requestQueue = Volley.newRequestQueue(Issue.this);
                requestQueue.add(strreq);

                Intent intent = new Intent(Issue.this,Dashboard.class);
                startActivity(intent);
            }


        });


    }
}
