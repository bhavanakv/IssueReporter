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

public class SignUp extends AppCompatActivity {
    EditText e1,e2,e3;
    Button b1;
    String name;
    String uname;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle(R.string.title_activity_signin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_assignment_ind_black_48dp);
        e1 = (EditText) findViewById(R.id.et3);
        e2 = (EditText) findViewById(R.id.et4);
        e3 = (EditText) findViewById(R.id.et5);
        b1 = (Button) findViewById(R.id.button4);
        final String LOGIN_URL="http://192.168.43.198:8000/api/register";
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = e1.getText().toString().trim();
                uname = e2.getText().toString().trim();
                password = e3.getText().toString().trim();

                StringRequest strreq = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.contains("Success"))
                        {
                            Toast.makeText(SignUp.this, "You are now registered here!!", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(SignUp.this,response,Toast.LENGTH_LONG).show();
                        }


                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(SignUp.this,error.toString(),Toast.LENGTH_LONG ).show();
                                System.out.println(error.toString());
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("name",name);
                        map.put("username",uname);
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

                RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                requestQueue.add(strreq);



            }
        });
    }
}
