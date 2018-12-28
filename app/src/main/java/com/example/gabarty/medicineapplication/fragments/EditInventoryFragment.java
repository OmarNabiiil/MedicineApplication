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
import com.example.gabarty.medicineapplication.Interfaces.EditInventoryInterface;
import com.example.gabarty.medicineapplication.MyApplication;
import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Inventory;

import java.util.HashMap;
import java.util.Map;

public class EditInventoryFragment extends DialogFragment {

    public static final String TAG=EditInventoryFragment.class.getSimpleName();
    private EditInventoryInterface mListener;

    private EditText name;
    private EditText minQ;
    private EditText customerPrice;
    private EditText price;
    private EditText quantity;
    private Button edit_inventory;
    private Button cancel;

    private Inventory inventory;

    public EditInventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_edit_inventory, container, false);

        name=view.findViewById(R.id.txt_inventory_name);
        minQ=view.findViewById(R.id.txt_inventory_min_quantity);
        customerPrice =view.findViewById(R.id.txt_inventory_customer_price);
        price =view.findViewById(R.id.txt_inventory_price);
        quantity =view.findViewById(R.id.txt_inventory_quantity);
        edit_inventory=view.findViewById(R.id.edit_inventory);
        cancel=view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        edit_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserDB();
            }
        });

        setinventory();
        return view;
    }

    public void setListener(EditInventoryInterface listener) {
        mListener = listener;
    }

    public void setinventory(){
        Intent i = getActivity().getIntent();
        inventory = (Inventory) i.getSerializableExtra("Inventory");
        name.setText(inventory.getName());
        minQ.setText(inventory.getMinimumQuantity());
        customerPrice.setText(inventory.getCustomer_price());
        price.setText(inventory.getPrice());
        quantity.setText(inventory.getQuantity());
    }

    public void editUserDB(){
        String inv_name = name.getText().toString().trim();
        String eminQ = minQ.getText().toString().trim();
        String cP = customerPrice.getText().toString().trim();
        String myprice = price.getText().toString().trim();
        String myQuantity = quantity.getText().toString().trim();

        if (!inv_name.isEmpty() && !eminQ.isEmpty() && !cP.isEmpty() && !myprice.isEmpty()) {
            editUser(inv_name, myQuantity, eminQ, cP, myprice);
        } else {
            Toast.makeText(getContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void editUser(final String name, final String q, final String eminQ,
                          final String cP,final String price) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_EDITINVENTORY_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //check for success first
                Inventory c=new Inventory(name, q, price, cP, eminQ);
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
                params.put("oldname", inventory.getName());
                params.put("eminQ", eminQ);
                params.put("customerPrice", cP);
                params.put("price", price);
                params.put("quantity", q);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}