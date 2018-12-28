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
import com.example.gabarty.medicineapplication.Interfaces.IdialogOrdersDismissFragment;
import com.example.gabarty.medicineapplication.Interfaces.IdialogSuppliersDismissFragment;
import com.example.gabarty.medicineapplication.MyApplication;
import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Client;
import com.example.gabarty.medicineapplication.classes.Inventory;
import com.example.gabarty.medicineapplication.classes.Order;
import com.example.gabarty.medicineapplication.classes.Supplier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddOrderFragment extends DialogFragment {

    public static final String TAG=AddOrderFragment.class.getSimpleName();
    private IdialogOrdersDismissFragment mIReminderAdded;

    private EditText user_name;
    private EditText inpMail;
    private EditText inpMobile;
    private EditText inpQuantity;
    private Spinner inventory_spinner;
    private Spinner clients_spinner;
    private Button make_order;
    private Button cancel;
    private ArrayList<Inventory> inventories=new ArrayList<>();
    private ArrayList<Client> clients=new ArrayList<>();

    public AddOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_order, container, false);

        initViews(view);
        getAllInventories();
        getAllClients();
        return view;
    }

    private void initViews(View v) {

        user_name=v.findViewById(R.id.user_name);
        inpMail=v.findViewById(R.id.email);
        inpMobile =v.findViewById(R.id.mobile);
        inpQuantity=v.findViewById(R.id.quantity);
        inventory_spinner =v.findViewById(R.id.list_products);
        clients_spinner =v.findViewById(R.id.list_clients);
        make_order=v.findViewById(R.id.btn_add_order);
        cancel=v.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIReminderAdded.onDismissClick();
                getDialog().dismiss();
            }
        });

        make_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserDB();
            }
        });

    }

    public void setListener(IdialogOrdersDismissFragment listener) {
        mIReminderAdded = listener;
    }

    public void getAllClients(){
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_CLIENTS_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("test", "GetCards Response: " + response.toString());
                try {
                    //JSONObject jObji = new JSONObject(response);
                    JSONArray a=new JSONArray(response);
                    Log.d("loginres",a.toString());
                    ArrayList<String> myClients=new ArrayList<String>();
                    int sizeofarray=a.length();
                    for(int i=0;i<sizeofarray;i++){
                        JSONObject jObj = a.getJSONObject(i);//all the users in the database
                        Client c=new Client(jObj.get("name").toString(), jObj.get("email").toString(),
                                jObj.get("phone").toString(), jObj.get("address").toString());
                        clients.add(c);
                        myClients.add(jObj.get("name").toString());

                    }

                    ArrayAdapter<String> clients_adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, myClients);
                    clients_spinner.setAdapter(clients_adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("test", "Registration Error: " + error.getMessage());
            }
        }) {

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
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
                        String state= (String)jObj.get("minimum_quantity");
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

        String quantity = inpQuantity.getText().toString().trim();
        int inventory_index = inventory_spinner.getSelectedItemPosition();
        Inventory selected_inventory=inventories.get(inventory_index);
        int client_index = clients_spinner.getSelectedItemPosition();
        Client selected_client=clients.get(client_index);
        String name = selected_client.getName();
        String product_name=selected_inventory.getName();
        String email = selected_client.getEmail();
        String phone = selected_client.getMobile();
        String address=selected_client.getAddress();
        double price=Double.parseDouble(selected_inventory.getPrice());
        int qntty=Integer.parseInt(quantity);
        double tot_price=price*qntty;
        //String compID= ""+MainActivity.COMPANY_ID;

        if (!quantity.isEmpty()) {
            registerUser(name, product_name, email, phone, quantity, tot_price+"", address);
        } else {
            Toast.makeText(getContext(),
                    "Please enter the quantity!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void registerUser(final String name,final String product, final String email,
                              final String phone,final String quantity,final String price, final String address) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_ADDORDER_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                Order r=new Order(name, product, quantity, price);
                mIReminderAdded.onOkClick(r);
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
                params.put("product", product);
                params.put("email", email);
                params.put("phone", phone);
                params.put("quantity", quantity);
                params.put("price", price);
                params.put("address", address);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
