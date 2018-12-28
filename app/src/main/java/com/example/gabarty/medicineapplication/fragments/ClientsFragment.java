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
import com.example.gabarty.medicineapplication.Interfaces.EditClientInterface;
import com.example.gabarty.medicineapplication.Interfaces.IdialogClientsDismissFragment;
import com.example.gabarty.medicineapplication.MyApplication;
import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.adapters.ClientsAdapter;
import com.example.gabarty.medicineapplication.classes.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientsFragment extends Fragment implements ClientsAdapter.ItemClickListener, IdialogClientsDismissFragment, EditClientInterface {

    private RecyclerView recyclerView;
    private List<Client> clients_list;
    private ClientsAdapter mAdapter;
    private int edit_position;
    private Button add;

    public ClientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_clients, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        clients_list = new ArrayList<>();
        mAdapter = new ClientsAdapter(getContext(), clients_list,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getalldata();

        return view;
    }

    @Override
    public void onItemClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition) {
        //remove the selected user
        Client c = clients_list.get(viewHolder.getAdapterPosition());
        removeUser(c.getName(), viewHolder.getAdapterPosition());

    }

    @Override
    public void onEditClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition) {
        Client c=clients_list.get(viewHolder.getAdapterPosition());
        edit_position=viewHolder.getAdapterPosition();
        EditClientFragment fr=new EditClientFragment();
        fr.setListener(this);
        getActivity().getIntent().putExtra("Client", c);
        fr.show(getFragmentManager(), "Edit Client");
    }

    public void getalldata() {
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
                    ArrayList<JSONObject> myClients=new ArrayList<JSONObject>();
                    int sizeofarray=a.length();
                    for(int i=0;i<sizeofarray;i++){
                        JSONObject jObj = a.getJSONObject(i);//all the users in the database
                        Client c=new Client(jObj.get("name").toString(), jObj.get("email").toString(),
                                jObj.get("mobile").toString(), jObj.get("address").toString());
                        clients_list.add(c);

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

    public void removeUser(final String name, final int position) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String url = Config.TEST_REMOVECLIENT_URL;

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
    public void onOkClick(Client c) {
        mAdapter.addItem(c);
    }

    @Override
    public void onDismissClick() {

    }

    @Override
    public void onEditClick(Client s) {
        mAdapter.editItem(s, edit_position);
    }

    /*public void getalldata() {
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
                    ArrayList<JSONObject> myClients=new ArrayList<JSONObject>();
                    int sizeofarray=a.length();
                    for(int i=0;i<sizeofarray;i++){
                        JSONObject jObj = a.getJSONObject(i);//all the users in the database

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", "value");

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }*/
}
