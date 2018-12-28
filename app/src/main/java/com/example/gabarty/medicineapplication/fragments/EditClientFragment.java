package com.example.gabarty.medicineapplication.fragments;

import android.content.Intent;
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
import com.example.gabarty.medicineapplication.Interfaces.EditClientInterface;
import com.example.gabarty.medicineapplication.MyApplication;
import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Client;

import java.util.HashMap;
import java.util.Map;

public class EditClientFragment extends DialogFragment {

    public static final String TAG=EditClientFragment.class.getSimpleName();
    private EditClientInterface mListener;

    private EditText user_name;
    private EditText mail;
    private EditText mobile;
    private EditText address;
    private Button edit_user;
    private Button cancel;

    private Client client;

    public EditClientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_edit_client, container, false);

        user_name=view.findViewById(R.id.txt_client_name);
        mail=view.findViewById(R.id.txt_client_mail);
        mobile =view.findViewById(R.id.txt_client_mobile);
        address =view.findViewById(R.id.txt_client_address);
        edit_user=view.findViewById(R.id.edit_client);
        cancel=view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserDB();
            }
        });

        setClient();
        return view;
    }

    public void setListener(EditClientInterface listener) {
        mListener = listener;
    }

    public void setClient(){
        Intent i = getActivity().getIntent();
        client = (Client) i.getSerializableExtra("Client");
        user_name.setText(client.getName());
        mail.setText(client.getEmail());
        mobile.setText(client.getMobile());
        address.setText(client.getAddress());
    }

    public void editUserDB(){
        String name = user_name.getText().toString().trim();
        String email = mail.getText().toString().trim();
        String phone = mobile.getText().toString().trim();
        String myaddress = address.getText().toString().trim();

        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !myaddress.isEmpty()) {
            editUser(name, email, phone, myaddress);
        } else {
            Toast.makeText(getContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void editUser(final String name, final String email,
                          final String phone,final String address) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_EDITCLIENT_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //check for success first
                Client c=new Client(name, email, phone, address);
                mListener.onEditClick(c);
                dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                Map<String, String> params = new HashMap<String, String>();
                params.put("newname", name);
                params.put("oldname", client.getName());
                params.put("email", email);
                params.put("mobile", phone);
                params.put("address", address);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
