package com.herce.pf_moviles.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Activities.ProductDetailActivity;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Services.AppController;
import com.herce.pf_moviles.Services.VolleyRequest;

import java.util.ArrayList;

/**
 * Created by Herce on 30/04/2017.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>{

    ArrayList<Product> list = null;
    Context context = null;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ProductsAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View childView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(ProductsAdapter.ViewHolder holder,int position) {
        String name = list.get(position).getName();
        String imageURL = list.get(position).getImageURL();
        String brand = list.get(position).getBrand();
        Double price = list.get(position).getPrice();

        holder.nameTxt.setText(name);
        holder.brandTxt.setText(brand);
        holder.priceTxt.setText("$" + price.toString());
        holder.image.setImageUrl(imageURL, imageLoader);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTxt, brandTxt, priceTxt;
        NetworkImageView image;
        CardView card;

        public ViewHolder(final View viewItem) {
            super(viewItem);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            image = (NetworkImageView) viewItem.findViewById(R.id.thumbnail);
            card = (CardView)  viewItem.findViewById(R.id.product_card);
            nameTxt = (TextView) viewItem.findViewById(R.id.nameText);
            brandTxt = (TextView) viewItem.findViewById(R.id.brandText);
            priceTxt = (TextView) viewItem.findViewById(R.id.priceText);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent mainIntent = new Intent(viewItem.getContext(), ProductDetailActivity.class);
                    Bundle id = new Bundle();
                    id.putString("productID", String.valueOf(list.get(getAdapterPosition()).getProductID()));
                    mainIntent.putExtras(id);
                    viewItem.getContext().startActivity(mainIntent);
                }
            });

        }

    }
}
