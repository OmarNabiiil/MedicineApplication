package com.example.gabarty.medicineapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.gabarty.medicineapplication.adapters.BuyProductsAdapter;
import com.example.gabarty.medicineapplication.adapters.CartAdapter;
import com.example.gabarty.medicineapplication.classes.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewCartActivity extends AppCompatActivity implements CartAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private List<Product> products_list;
    private CartAdapter mAdapter;
    private TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        products_list = new ArrayList<>();
        mAdapter = new CartAdapter(products_list, this);

        totalPrice = findViewById(R.id.txt_total_price);
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

        String url = Config.TEST_GETPRODUCTSCART_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("test", "GetCards Response: " + response.toString());
                try {
                    //JSONObject jObji = new JSONObject(response);
                    JSONArray a=new JSONArray(response);
                    Log.d("cartTest",a.toString());
                    int sizeofarray=a.length();
                    for(int i=0;i<sizeofarray;i++){
                        JSONObject jObj = a.getJSONObject(i);//all the users in the database
                        if(i==sizeofarray-1){
                            totalPrice.setText(jObj.get("total_cost").toString());
                        }else{
                            Product p=new Product(jObj.get("product name").toString(), jObj.get("product price").toString());
                            p.setQuantity(jObj.get("quantity").toString());
                            products_list.add(p);
                        }

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

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                Map<String, String> params = new HashMap<>();
                params.put("userID", ""+LoginUserActivity.USER_ID);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void submitOrder(){
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_SUBMITORDER_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("test", "GetCards Response: " + response.toString());
                Toast.makeText(ViewCartActivity.this,"The order submitted successfully", Toast.LENGTH_LONG).show();
                products_list.clear();
                totalPrice.setText("");
                mAdapter.notifyDataSetChanged();
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

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void makeOrder(View view) {
        submitOrder();
    }

    @Override
    public void onRemoveClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition) {

    }
}
