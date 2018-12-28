package com.example.gabarty.medicineapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.gabarty.medicineapplication.adapters.BuyProductsAdapter;
import com.example.gabarty.medicineapplication.adapters.ProductsAdapter;
import com.example.gabarty.medicineapplication.classes.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyProductsActivity extends AppCompatActivity implements BuyProductsAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private List<Product> products_list;
    private BuyProductsAdapter mAdapter;
    private Product p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_products);

        products_list = new ArrayList<>();
        mAdapter = new BuyProductsAdapter(products_list, this);

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getalldata();
    }

    public void getalldata() {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_INVENTORY_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("test", "GetCards Response: " + response.toString());
                try {
                    //JSONObject jObji = new JSONObject(response);
                    JSONArray a=new JSONArray(response);
                    Log.d("loginres",a.toString());
                    int sizeofarray=a.length();
                    for(int i=0;i<sizeofarray;i++){
                        JSONObject jObj = a.getJSONObject(i);//all the users in the database
                        Product p=new Product(jObj.get("name").toString(), jObj.get("client_price").toString());
                        products_list.add(p);
                    }

                    mAdapter.notifyDataSetChanged();

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

    @Override
    public void onItemClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition) {
        p = products_list.get(viewHolder.getAdapterPosition());
        showDialog();
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter the required quantity");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.text_inpu_password, (ViewGroup) findViewById(android.R.id.content), false);
        // Set up the input
        final EditText input = viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                if(!m_Text.isEmpty() && !m_Text.equals(" ")){
                    p.setQuantity(m_Text);
                    addToCartDB();
                    dialog.dismiss();
                }else{
                    Toast.makeText(BuyProductsActivity.this,"please enter a quantity", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void addToCartDB(){
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_ADDTOCART_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("test", "GetCards Response: " + response.toString());
                if(response.equals("success")){
                    Toast.makeText(BuyProductsActivity.this,"successfully added", Toast.LENGTH_LONG).show();
                }
                if(response.equals("exceeded")){
                    Toast.makeText(BuyProductsActivity.this,"quantity exceeded the available!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("test", "Registration Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                Map<String, String> params = new HashMap<>();
                params.put("userID", ""+LoginUserActivity.USER_ID);
                params.put("productName", ""+p.getName());
                params.put("productQuantity", ""+p.getQuantity());
                params.put("price", ""+p.getPrice());

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void ViewCart(View view) {
        Intent i=new Intent(BuyProductsActivity.this,ViewCartActivity.class);
        startActivity(i);
    }
}
