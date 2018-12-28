package com.example.gabarty.medicineapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginAdminActivity extends AppCompatActivity {

    EditText name;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.userName);
        password = findViewById(R.id.password);
    }

    public void Login(View view) {
        if(name.getText().toString().equals(" ") || name.getText().toString().isEmpty() || password.getText().toString().equals(" ") || password.getText().toString().isEmpty()){
            Toast.makeText(LoginAdminActivity.this,"please enter your full details!", Toast.LENGTH_LONG).show();
        }else{
            loginUser(name.getText().toString().trim(), password.getText().toString().trim());
        }

    }

    public void loginUser(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_LOGIN_ADMIN_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("testres",""+response);
                if(response.equals("success")){
                    Intent i=new Intent(LoginAdminActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();

                }else{
                    Toast.makeText(LoginAdminActivity.this,"failed to login", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                Map<String, String> params = new HashMap<>();
                params.put("username", ""+username);
                params.put("password", ""+password);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
