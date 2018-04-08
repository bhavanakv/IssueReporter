package com.example.homepc.issuereporter;

import android.content.Intent;
import android.icu.text.IDNA;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    EditText e1, e2;
    Button b3;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_face_black_48dp);
        e1 = (EditText) findViewById(R.id.et1);
        e2 = (EditText) findViewById(R.id.et2);
        b3 = (Button) findViewById(R.id.button3);

        final String LOGIN_URL="http://192.168.43.198:8000/api/authenticate/user";
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = e1.getText().toString().trim();
                password = e2.getText().toString().trim();

                StringRequest strreq = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.contains("Authenticated successfully.")) {
                            try {
                                JSONObject tokenize = new JSONObject(response);
                                String token = tokenize.getString("token");
                                System.out.println(token);
                                Intent in1 = new Intent(Login.this, Dashboard.class);
                                in1.putExtra("Token",token);
                                startActivity(in1);
                            }catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        else{
                            Toast.makeText(Login.this,"User does not exist.",Toast.LENGTH_LONG).show();
                            Intent in2 = new Intent(Login.this, MainActivity.class);
                            startActivity(in2);
                        }


                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                                Intent in2 = new Intent(Login.this,MainActivity.class);
                                startActivity(in2);
                                System.out.println(error.toString());
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("username",username);
                        map.put("password",password);
                        map.put("type","user");
                        return map;
                    }
                };

                int socketTimeout = 10000; // 30 seconds. You can change it
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                strreq.setRetryPolicy(policy);

                RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                requestQueue.add(strreq);



            }
        });
    }

}


