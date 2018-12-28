package com.example.gabarty.medicineapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<Product> productsList;
    private ItemClickListener monClickListener;

    public interface ItemClickListener{
        void onRemoveClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView name, quantity, price;
        public ImageButton remove;

        public MyViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.name);
            price=view.findViewById(R.id.price);
            quantity=view.findViewById(R.id.quantity);
            remove=view.findViewById(R.id.btn_remove);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedItemPosition=getAdapterPosition();
            monClickListener.onRemoveClickListener(this, clickedItemPosition);
        }
    }

    public CartAdapter(List<Product> productsList, ItemClickListener listener) {
        this.productsList = productsList;
        this.monClickListener = listener;
    }

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);

        return new CartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.MyViewHolder holder, int position) {
        Product v=productsList.get(position);
        holder.name.setText("Name: "+v.getName());
        holder.price.setText("price: "+v.getPrice());
        holder.quantity.setText("quantity: "+v.getQuantity());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

}