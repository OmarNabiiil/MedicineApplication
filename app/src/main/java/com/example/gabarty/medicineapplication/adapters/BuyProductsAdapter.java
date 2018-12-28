package com.example.gabarty.medicineapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gabarty.medicineapplication.R;
import com.example.gabarty.medicineapplication.classes.Product;

import java.util.List;

public class BuyProductsAdapter extends RecyclerView.Adapter<BuyProductsAdapter.MyViewHolder> {

    private List<Product> productsList;
    private BuyProductsAdapter.ItemClickListener monClickListener;

    public interface ItemClickListener{
        void onItemClickListener(RecyclerView.ViewHolder viewHolder, int itemPosition);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, price;
        public Button addToCart;

        public MyViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.name);
            price=view.findViewById(R.id.price);
            addToCart=view.findViewById(R.id.btn_add_cart);
            addToCart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedItemPosition=getAdapterPosition();
            Log.d("clicked",""+clickedItemPosition);
            monClickListener.onItemClickListener(this, clickedItemPosition);
        }
    }

    public BuyProductsAdapter( List<Product> productsList, BuyProductsAdapter.ItemClickListener listener) {
        this.productsList = productsList;
        monClickListener=listener;
    }

    @Override
    public BuyProductsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buy_product_list_item, parent, false);

        return new BuyProductsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BuyProductsAdapter.MyViewHolder holder, int position) {
        Product v=productsList.get(position);
        holder.name.setText("Name: "+v.getName());
        holder.price.setText("price: "+v.getPrice());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public void removeItem(int position) {
        productsList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,productsList.size());
    }

    public void addItem(Product v){
        productsList.add(v);
        notifyItemInserted(productsList.size()-1);
    }

}