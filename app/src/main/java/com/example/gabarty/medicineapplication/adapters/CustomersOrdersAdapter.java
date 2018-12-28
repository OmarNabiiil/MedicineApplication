package com.example.gabarty.medicineapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Client;

import java.util.List;

public class CustomersOrdersAdapter extends RecyclerView.Adapter<CustomersOrdersAdapter.MyViewHolder> {

    private List<Client> customersOrders;
    private ItemClickListener monClickListener;

    public interface ItemClickListener{
        void onItemClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            monClickListener.onItemClickListener(this, getAdapterPosition());
        }
    }

    public CustomersOrdersAdapter( List<Client> customersOrders, CustomersOrdersAdapter.ItemClickListener listener) {
        this.customersOrders = customersOrders;
        monClickListener=listener;
    }

    @Override
    public CustomersOrdersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clients_orders_list_item, parent, false);

        return new CustomersOrdersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomersOrdersAdapter.MyViewHolder holder, int position) {
        Client r=customersOrders.get(position);
        holder.name.setText("Customer: "+r.getName());
    }

    @Override
    public int getItemCount() {
        return customersOrders.size();
    }

    public void removeItem(int position) {
        customersOrders.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,customersOrders.size());
    }

    public void addItem(Client r){
        customersOrders.add(r);
        notifyItemInserted(customersOrders.size()-1);
    }
}
