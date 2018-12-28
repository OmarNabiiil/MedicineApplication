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
import com.example.gabarty.medicineapplication.Interfaces.EditSupplierInterface;
import com.example.gabarty.medicineapplication.MyApplication;
import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Supplier;

import java.util.HashMap;
import java.util.Map;

public class EditSupplierFragment extends DialogFragment {

    public static final String TAG=EditSupplierFragment.class.getSimpleName();
    private EditSupplierInterface mListener;

    private EditText user_name;
    private EditText mail;
    private EditText mobile;
    private EditText quantity;
    private EditText price;
    private Button edit_user;
    private Button cancel;

    private Supplier supplier;

    public EditSupplierFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_edit_supplier, container, false);

        user_name=view.findViewById(R.id.txt_supplier_name);
        mail=view.findViewById(R.id.txt_supplier_mail);
        mobile =view.findViewById(R.id.txt_supplier_mobile);
        quantity =view.findViewById(R.id.txt_supplier_quantity);
        price =view.findViewById(R.id.txt_supplier_price);
        edit_user=view.findViewById(R.id.edit_supplier);
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

        setSupplier();
        return view;
    }

    public void setListener(EditSupplierInterface listener) {
        mListener = listener;
    }

    public void setSupplier(){
        Intent i = getActivity().getIntent();
        supplier = (Supplier) i.getSerializableExtra("Supplier");
        user_name.setText(supplier.getName());
        mail.setText(supplier.getEmail());
        mobile.setText(supplier.getMobile());
        quantity.setText(supplier.getQuantity());
        price.setText(supplier.getPrice());
    }

    public void editUserDB(){
        String name = user_name.getText().toString().trim();
        String email = mail.getText().toString().trim();
        String phone = mobile.getText().toString().trim();
        String myquantity = quantity.getText().toString().trim();
        String myprice = price.getText().toString().trim();
        //String compID= ""+MainActivity.COMPANY_ID;

        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !myprice.isEmpty()) {
            editUser(name, email, phone, myquantity, myprice);
        } else {
            Toast.makeText(getContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void editUser(final String name, final String email,
                          final String phone,final String quantity,final String price) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_EDITSUPPLIER_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //check for success first
                Supplier c=new Supplier(name, email, phone, quantity, price);
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
                params.put("oldname", supplier.getName());
                params.put("email", email);
                params.put("mobile", phone);
                params.put("quantity", quantity);
                params.put("price", price);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}