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
import com.example.gabarty.medicineapplication.Interfaces.IdialogInventoriesDismissFragment;
import com.example.gabarty.medicineapplication.MyApplication;
import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddInventoryFragment extends DialogFragment {

    public static final String TAG=AddUserFragment.class.getSimpleName();
    private IdialogInventoriesDismissFragment mIReminderAdded;

    private EditText user_name;
    private EditText inpQuantity;
    private EditText inpPrice;
    private EditText inpCustomerPrice;
    private EditText min_quantity;
    private Button add_inventory;
    private Button cancel;

    public AddInventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_inventory, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {

        user_name=v.findViewById(R.id.product_name);
        inpQuantity=v.findViewById(R.id.quantity);
        inpPrice =v.findViewById(R.id.price);
        inpCustomerPrice =v.findViewById(R.id.customer_price);
        min_quantity =v.findViewById(R.id.min_quantity);
        add_inventory=v.findViewById(R.id.btn_add_inventory);
        cancel=v.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIReminderAdded.onDismissClick();
                getDialog().dismiss();
            }
        });

        add_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInventoryDB();
            }
        });

    }

    public void setListener(IdialogInventoriesDismissFragment listener) {
        mIReminderAdded = listener;
    }

    public void addInventoryDB(){

        String name = user_name.getText().toString().trim();
        String quantity = inpQuantity.getText().toString().trim();
        String price = inpPrice.getText().toString().trim();
        String cP = inpCustomerPrice.getText().toString().trim();
        String minimum_quantity = min_quantity.getText().toString().trim();
        //String compID= ""+MainActivity.COMPANY_ID;

        if (!name.isEmpty() && !quantity.isEmpty() && !price.isEmpty() && !cP.isEmpty() && !minimum_quantity.isEmpty()) {
            registerUser(name, quantity, price, cP, minimum_quantity);
        } else {
            Toast.makeText(getContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void registerUser(final String name, final String quantity,
                              final String price, final String cP,final String minQuantity) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_ADDINVENTORY_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //check for success first
                Inventory v=new Inventory(name, quantity, price, cP, minQuantity);
                mIReminderAdded.onOkClick(v);
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
                params.put("quantity", quantity);
                params.put("price", price);
                params.put("customerPrice", cP);
                params.put("minimumQuantity", minQuantity);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}