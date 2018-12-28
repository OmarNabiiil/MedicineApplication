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
import com.example.gabarty.medicineapplication.Interfaces.EditSupplierInterface;
import com.example.gabarty.medicineapplication.Interfaces.IdialogSuppliersDismissFragment;
import com.example.gabarty.medicineapplication.MyApplication;
import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.adapters.SuppliersAdapter;
import com.example.gabarty.medicineapplication.classes.Client;
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
public class SuppliersFragment extends Fragment implements SuppliersAdapter.ItemClickListener, IdialogSuppliersDismissFragment, EditSupplierInterface{

    private RecyclerView recyclerView;
    private List<Supplier> suppliers_list;
    private SuppliersAdapter mAdapter;
    private int edit_position;
    private Button add;

    public SuppliersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_suppliers, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        add= view.findViewById(R.id.btn_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddSupplier();
            }
        });

        suppliers_list = new ArrayList<>();
        mAdapter = new SuppliersAdapter(getContext(), suppliers_list,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getalldata();

        return view;
    }

    public void openAddSupplier(){
        final AddSupplierFragment fr=new AddSupplierFragment();
        fr.setListener(this);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fr.show(getFragmentManager(), "Add Supplier");
            }
        });

    }

    @Override
    public void onEditClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition) {
        Supplier c=suppliers_list.get(viewHolder.getAdapterPosition());
        edit_position=viewHolder.getAdapterPosition();
        EditSupplierFragment fr=new EditSupplierFragment();
        fr.setListener(this);
        getActivity().getIntent().putExtra("Supplier", c);
        fr.show(getFragmentManager(), "Edit Supplier");
    }

    public void getalldata() {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_SUPPLIERS_URL;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("test", "GetCards Response: " + response.toString());
                try {
                    //JSONObject jObji = new JSONObject(response);
                    JSONArray a=new JSONArray(response);
                    Log.d("loginres",a.toString());
                    ArrayList<JSONObject> mySuppliers=new ArrayList<JSONObject>();
                    int sizeofarray=a.length();
                    for(int i=0;i<sizeofarray;i++){
                        JSONObject jObj = a.getJSONObject(i);//all the users in the database
                        Supplier s=new Supplier(jObj.get("name").toString(), jObj.get("email").toString(),
                                jObj.get("phone").toString(), jObj.get("quantity").toString(), jObj.get("price").toString());
                        s.setDate(jObj.get("date").toString());
                        suppliers_list.add(s);

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

    public void removeSupplier(final String name, final int position) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_REMOVESUPPLIER_URL;

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
    public void onOkClick(Supplier s) {
        mAdapter.addItem(s);
    }

    @Override
    public void onDismissClick() {

    }

    @Override
    public void onItemClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition) {
        //remove the selected user
        Supplier s = suppliers_list.get(viewHolder.getAdapterPosition());
        removeSupplier(s.getName(), viewHolder.getAdapterPosition());
    }

    @Override
    public void onEditClick(Supplier s) {
        mAdapter.editItem(s, edit_position);
    }
}
