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

public class LoginUserActivity extends AppCompatActivity {

    public static String USER_ID="";
    EditText name;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        name = findViewById(R.id.userName);
        password = findViewById(R.id.password);
    }

    public void Login(View view) {
        if(name.getText().toString().equals(" ") || name.getText().toString().isEmpty() || password.getText().toString().equals(" ") || password.getText().toString().isEmpty()){
            Toast.makeText(LoginUserActivity.this,"please enter your full details!", Toast.LENGTH_LONG).show();
        }else{
            loginUser(name.getText().toString().trim(), password.getText().toString().trim());
        }

    }

    public void loginUser(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_LOGIN_USER_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("testres",""+response);
                if(!response.isEmpty()){
                    try {
                        JSONObject userObject = new JSONObject(response);
                        USER_ID = userObject.get("id").toString();
                        Intent i=new Intent(LoginUserActivity.this,BuyProductsActivity.class);
                        startActivity(i);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(LoginUserActivity.this,"failed to login", Toast.LENGTH_LONG).show();
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
