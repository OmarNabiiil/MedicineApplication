package com.example.gabarty.medicineapplication.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.gabarty.medicineapplication.Config;
import com.example.gabarty.medicineapplication.Interfaces.EditInventoryInterface;
import com.example.gabarty.medicineapplication.Interfaces.IdialogInventoriesDismissFragment;
import com.example.gabarty.medicineapplication.Interfaces.IdialogSuppliersDismissFragment;
import com.example.gabarty.medicineapplication.MyApplication;
import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.adapters.InventoriesAdapter;
import com.example.gabarty.medicineapplication.adapters.SuppliersAdapter;
import com.example.gabarty.medicineapplication.classes.Inventory;
import com.example.gabarty.medicineapplication.classes.Supplier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment implements InventoriesAdapter.ItemClickListener, IdialogInventoriesDismissFragment,
                                                            EditInventoryInterface {

    private RecyclerView recyclerView;
    private List<Inventory> inventory_list;
    private InventoriesAdapter mAdapter;
    private int edit_position;
    private Button add;

    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        add= view.findViewById(R.id.btn_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddInventory();
            }
        });

        inventory_list = new ArrayList<>();
        mAdapter = new InventoriesAdapter(getContext(), inventory_list,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getalldata();

        return view;
    }

    public void openAddInventory(){
        final AddInventoryFragment fr=new AddInventoryFragment();
        fr.setListener(this);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fr.show(getFragmentManager(), "Add Supplier");
            }
        });

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
                        Inventory v=new Inventory(jObj.get("name").toString(), jObj.get("quantity").toString(),
                                jObj.get("original_price").toString(),  jObj.get("client_price").toString(), jObj.get("minimum_quantity").toString());
                        inventory_list.add(v);
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

    public void removeInventory(final String name, final int position) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_REMOVEINVENTORY_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("test", "GetCards Response: " + response.toString());
                if(response.equals("success")){
                    mAdapter.removeItem(position);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onOkClick(Inventory v) {
        mAdapter.addItem(v);
    }

    @Override
    public void onDismissClick() {

    }

    @Override
    public void onItemClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition) {
        //remove the selected user
        Inventory v = inventory_list.get(viewHolder.getAdapterPosition());
        removeInventory(v.getName(), viewHolder.getAdapterPosition());
    }

    @Override
    public void onEditClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition) {
        Inventory c=inventory_list.get(viewHolder.getAdapterPosition());
        edit_position=viewHolder.getAdapterPosition();
        EditInventoryFragment fr=new EditInventoryFragment();
        fr.setListener(this);
        getActivity().getIntent().putExtra("Inventory", c);
        fr.show(getFragmentManager(), "Edit Inventory");
    }

    @Override
    public void onEditClick(Inventory i) {
        mAdapter.editItem(i, edit_position);
    }
}
