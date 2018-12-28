package com.example.gabarty.medicineapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Order;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private List<Order> ordersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView quantity, price, product_name;

        public MyViewHolder(View view) {
            super(view);
            quantity=view.findViewById(R.id.quantity);
            price=view.findViewById(R.id.price);
            product_name=view.findViewById(R.id.product);
        }

    }

    public OrdersAdapter(List<Order> ordersList) {
        this.ordersList = ordersList;
    }

    @Override
    public OrdersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list_item, parent, false);

        return new OrdersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OrdersAdapter.MyViewHolder holder, int position) {
        Order r=ordersList.get(position);
        holder.quantity.setText("quantity: "+r.getQuantity());
        holder.price.setText("price: "+r.getPrice());
        holder.product_name.setText("product name: "+r.getProduct());
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public void removeItem(int position) {
        ordersList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,ordersList.size());
    }

    public void addItem(Order r){
        ordersList.add(r);
        notifyItemInserted(ordersList.size()-1);
    }
}
