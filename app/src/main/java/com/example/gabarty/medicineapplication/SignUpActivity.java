package com.example.gabarty.medicineapplication;

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
import com.example.gabarty.medicineapplication.classes.Client;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText username;
    EditText name;
    EditText password;
    EditText confirmPassword;
    EditText mail;
    EditText mobile;
    EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.userName);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        mail = findViewById(R.id.mail);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
    }

    public void SignUp(View view) {
        if(!username.getText().toString().isEmpty() && !name.getText().toString().isEmpty() &&
                !password.getText().toString().isEmpty() && !confirmPassword.getText().toString().isEmpty() &&
                !mail.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty() &&
                !address.getText().toString().isEmpty()){
            if(password.getText().toString().equals(confirmPassword.getText().toString())){
                registerUser();
            }else{
                Toast.makeText(SignUpActivity.this,"please write the correct password!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(SignUpActivity.this,"please enter your full details!", Toast.LENGTH_LONG).show();
        }
    }

    private void registerUser() {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_ADDCLIENT_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString());
                params.put("name", name.getText().toString());
                params.put("password", password.getText().toString());
                params.put("email", mail.getText().toString());
                params.put("phone", mobile.getText().toString());
                params.put("address", address.getText().toString());

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
