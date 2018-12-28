package com.example.gabarty.medicineapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {
    private List<Product> productsList;

    public interface ItemClickListener{
        void onItemClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView name, price;
        public ImageButton remove;

        public MyViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.name);
            price=view.findViewById(R.id.price);
        }

    }

    public ProductsAdapter(List<Product> productsList) {
        this.productsList = productsList;
    }

    @Override
    public ProductsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_list_item, parent, false);

        return new ProductsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.MyViewHolder holder, int position) {
        Product v=productsList.get(position);
        holder.name.setText("Name: "+v.getName());
        holder.price.setText("price: "+v.getPrice());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

}