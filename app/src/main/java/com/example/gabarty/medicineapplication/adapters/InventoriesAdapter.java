package com.example.gabarty.medicineapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Inventory;

import java.util.List;

public class InventoriesAdapter extends RecyclerView.Adapter<InventoriesAdapter.MyViewHolder> {
    private Context context;
    private List<Inventory> inventoriesList;
    private ItemClickListener monClickListener;

    public interface ItemClickListener{
        void onItemClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition);
        void onEditClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, quantity, customer_price, price, status;
        public ImageButton remove, edit;

        public MyViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.name);
            quantity=view.findViewById(R.id.quantity);
            customer_price=view.findViewById(R.id.customer_price);
            price=view.findViewById(R.id.price);
            status=view.findViewById(R.id.status);
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

    public InventoriesAdapter(Context context, List<Inventory> inventoriesList, ItemClickListener listener) {
        this.context = context;
        this.inventoriesList = inventoriesList;
        monClickListener=listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventories_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Inventory v=inventoriesList.get(position);
        holder.name.setText("Name: "+v.getName());
        holder.quantity.setText("quantity: "+v.getQuantity());
        holder.price.setText("suppliers price: "+v.getPrice());
        holder.customer_price.setText("customer price: "+v.getCustomer_price());
        holder.status.setText("status: "+v.getState());
    }

    @Override
    public int getItemCount() {
        return inventoriesList.size();
    }

    public void removeItem(int position) {
        inventoriesList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,inventoriesList.size());
    }

    public void addItem(Inventory v){
        inventoriesList.add(v);
        notifyItemInserted(inventoriesList.size()-1);
    }

    public void editItem(Inventory s, int position) {
        inventoriesList.set(position, s);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, inventoriesList.size());
    }

}
