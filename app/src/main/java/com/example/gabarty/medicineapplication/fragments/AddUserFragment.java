package com.example.gabarty.medicineapplication.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.gabarty.medicineapplication.Config;
import com.example.gabarty.medicineapplication.Interfaces.IdialogClientsDismissFragment;
import com.example.gabarty.medicineapplication.MyApplication;
import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Client;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserFragment extends DialogFragment {

    public static final String TAG=AddUserFragment.class.getSimpleName();
    private IdialogClientsDismissFragment mIReminderAdded;

    private EditText user_name;
    private EditText inpMail;
    private EditText inpMobile;
    private EditText address;
    private Button add_user;
    private Button cancel;

    public AddUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_user, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {

        user_name=v.findViewById(R.id.user_name);
        inpMail=v.findViewById(R.id.email);
        inpMobile =v.findViewById(R.id.mobile);
        address =v.findViewById(R.id.address);
        add_user=v.findViewById(R.id.btn_add_user);
        cancel=v.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIReminderAdded.onDismissClick();
                getDialog().dismiss();
            }
        });

        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserDB();
            }
        });

    }

    public void setListener(IdialogClientsDismissFragment listener) {
        mIReminderAdded = listener;
    }

    public void addUserDB(){
        String name = user_name.getText().toString().trim();
        String email = inpMail.getText().toString().trim();
        String phone = inpMobile.getText().toString().trim();
        String myAddress = address.getText().toString().trim();
        //String compID= ""+MainActivity.COMPANY_ID;

        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !myAddress.isEmpty()) {
            registerUser(name, email, phone, myAddress);
        } else {
            Toast.makeText(getContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void registerUser(final String name, final String email,
                              final String phone,final String address) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_ADDCLIENT_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //check for success first
                Client c=new Client(name, email, phone, address);
                mIReminderAdded.onOkClick(c);
                dismiss();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                mIReminderAdded.onDismissClick();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("address", address);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
