package com.example.gabarty.medicineapplication.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.gabarty.medicineapplication.Config;
import com.example.gabarty.medicineapplication.Interfaces.IdialogClientsDismissFragment;
import com.example.gabarty.medicineapplication.Interfaces.IdialogSuppliersDismissFragment;
import com.example.gabarty.medicineapplication.MyApplication;
import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Inventory;
import com.example.gabarty.medicineapplication.classes.Supplier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSupplierFragment extends DialogFragment {

    public static final String TAG=AddSupplierFragment.class.getSimpleName();
    private IdialogSuppliersDismissFragment mIReminderAdded;

    private EditText user_name;
    private EditText inpMail;
    private EditText inpMobile;
    private EditText inpQuantity;
    private Spinner inventory_spinner;
    private Button add_user;
    private Button cancel;
    private ArrayList<Inventory> inventories=new ArrayList<>();

    public AddSupplierFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_supplier, container, false);
        initViews(view);
        getAllInventories();
        return view;
    }

    private void initViews(View v) {

        user_name=v.findViewById(R.id.user_name);
        inpMail=v.findViewById(R.id.email);
        inpMobile =v.findViewById(R.id.mobile);
        inpQuantity=v.findViewById(R.id.quantity);
        inventory_spinner =v.findViewById(R.id.list_products);
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

    public void setListener(IdialogSuppliersDismissFragment listener) {
        mIReminderAdded = listener;
    }

    public void getAllInventories(){
        String tag_string_req = "req_register";

        String url = Config.TEST_INVENTORY_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                try {
                    JSONArray a = new JSONArray(response);
                    Log.d("loginres",a.toString());
                    ArrayList<String> myInventories=new ArrayList<String>();
                    int sizeofarray=a.length();
                    for(int i=0;i<sizeofarray;i++){
                        JSONObject jObj = a.getJSONObject(i);//all the users in the database
                        String name=(String) jObj.get("name");
                        String quantity= (String)jObj.get("quantity");
                        String price= (String)jObj.get("original_price");
                        String customer_price= (String)jObj.get("client_price");
                        String state= jObj.get("minimum_quantity").toString();
                        Inventory v=new Inventory(name, quantity, price, customer_price, state);
                        inventories.add(v);
                        myInventories.add(name);
                    }
                    ArrayAdapter<String> products_adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, myInventories);
                    inventory_spinner.setAdapter(products_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                mIReminderAdded.onDismissClick();
            }
        }) {

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void addUserDB(){ //handle the actions when the AddSupplier button clicked
        String name = user_name.getText().toString().trim();
        String email = inpMail.getText().toString().trim();
        String phone = inpMobile.getText().toString().trim();
        String quantity = inpQuantity.getText().toString().trim();
        int inventory_index = inventory_spinner.getSelectedItemPosition();
        Inventory selected_inventory=inventories.get(inventory_index);
        double price=Double.parseDouble(selected_inventory.getPrice());
        int qntty=Integer.parseInt(quantity);
        double tot_price=price*qntty;
        //String compID= ""+MainActivity.COMPANY_ID;

        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !quantity.isEmpty()) {
            registerUser(name, email, phone, quantity, tot_price+"", getDateTime());
        } else {
            Toast.makeText(getContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void registerUser(final String name, final String email,
                              final String phone,final String quantity,final String price, final String date) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_ADDSUPPLIER_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                Supplier s=new Supplier(name, email, phone, quantity, price);
                s.setDate(date);
                mIReminderAdded.onOkClick(s);
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
                params.put("quantity", quantity);
                params.put("price", price);
                params.put("date", date);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
