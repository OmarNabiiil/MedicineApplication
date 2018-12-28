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
import com.example.gabarty.medicineapplication.classes.Supplier;

import java.util.List;

public class SuppliersAdapter extends RecyclerView.Adapter<SuppliersAdapter.MyViewHolder> {
    private Context context;
    private List<Supplier> suppliersList;
    private ItemClickListener monClickListener;

    public interface ItemClickListener{
        void onItemClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition);
        void onEditClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, mail, mobile, quantity, price, date;
        public ImageButton remove, edit;

        public MyViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.name);
            mail=view.findViewById(R.id.email);
            mobile=view.findViewById(R.id.mobile);
            quantity=view.findViewById(R.id.quantity);
            price=view.findViewById(R.id.price);
            date=view.findViewById(R.id.date);
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

    public SuppliersAdapter(Context context, List<Supplier> suppliersList, ItemClickListener listener) {
        this.context = context;
        this.suppliersList = suppliersList;
        monClickListener=listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suppliers_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Supplier s=suppliersList.get(position);
        holder.name.setText("Name: "+s.getName());
        holder.mail.setText("Email: "+s.getEmail());
        holder.mobile.setText("Mobile: "+s.getMobile());
        holder.quantity.setText("quantity: "+s.getQuantity());
        holder.price.setText("price: "+s.getPrice());
        holder.date.setText("date: "+s.getDate());
    }

    @Override
    public int getItemCount() {
        return suppliersList.size();
    }

    public void removeItem(int position) {
        suppliersList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,suppliersList.size());
    }

    public void addItem(Supplier s){
        suppliersList.add(s);
        notifyItemInserted(suppliersList.size()-1);
    }

    public void editItem(Supplier s, int position) {
        suppliersList.set(position, s);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, suppliersList.size());
    }

}