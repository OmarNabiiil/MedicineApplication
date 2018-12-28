package com.example.gabarty.medicineapplication.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.MyViewHolder> {
    private Context context;
    private List<Client> clientsList;
    private ItemClickListener monClickListener;

    public interface ItemClickListener{
        void onItemClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition);
        void onEditClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, mail, mobile, address;
        public ImageButton remove, edit;

        public MyViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.name);
            mail=view.findViewById(R.id.email);
            mobile=view.findViewById(R.id.mobile);
            address=view.findViewById(R.id.address);
            remove=view.findViewById(R.id.btn_remove);
            remove.setOnClickListener(this);
            edit=view.findViewById(R.id.btn_edit);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.equals(remove)){
                int clickedItemPosition=getAdapterPosition();
                Log.d("clicked",""+clickedItemPosition);
                monClickListener.onItemClickListener(this, clickedItemPosition);
            }
            if(view.equals(edit)){
                int clickedItemPosition=getAdapterPosition();
                Log.d("clicked",""+clickedItemPosition);
                monClickListener.onEditClickListener(this, clickedItemPosition);
            }
        }
    }

    public ClientsAdapter(Context context, List<Client> clientsList, ItemClickListener listener) {
        this.context = context;
        this.clientsList = clientsList;
        monClickListener=listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clients_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Client c=clientsList.get(position);
        holder.name.setText("Name: "+c.getName());
        holder.mail.setText("Email: "+c.getEmail());
        holder.mobile.setText("Mobile: "+c.getMobile());
        holder.address.setText("address: "+c.getAddress());
    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }

    public void removeItem(int position) {
        clientsList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,clientsList.size());
    }

    public void addItem(Client c){
        clientsList.add(c);
        notifyItemInserted(clientsList.size()-1);
    }

    public void editItem(Client s, int position) {
        clientsList.set(position, s);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, clientsList.size());
    }

}