package com.herce.pf_moviles.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.herce.pf_moviles.Activities.ProductDetailActivity;
import com.herce.pf_moviles.Activities.ShoppingCartActivity;
import com.herce.pf_moviles.Fragments.User.All.ProductDetailFragment;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Services.AppController;

import java.util.ArrayList;

/**
 * Created by Herce on 01/05/2017.
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>{

    ArrayList<Product> list = null;
    Context context = null;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ShoppingCartAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ShoppingCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View childView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppingcart_card, parent, false);
        return new ViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(ShoppingCartAdapter.ViewHolder holder,int position) {
        String name = list.get(position).getName();
        String imageURL = list.get(position).getImageURL();
        String brand = list.get(position).getBrand();
        Double price = list.get(position).getPrice();
        Float size = list.get(position).getSize();
        String description = list.get(position).getDescription();

        holder.nameTxt.setText(name);
        holder.brandTxt.setText(brand);
        holder.priceTxt.setText("$" + price.toString());
        holder.image.setImageUrl(imageURL, imageLoader);
        holder.sizeTxt.setText(AppController.getInstance().getApplicationContext().getResources().getString(R.string.sizeText) + " " + size);
        holder.descriptionTxt.setText(description);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTxt, brandTxt, priceTxt, descriptionTxt, sizeTxt;
        NetworkImageView image;
        CardView card;

        public ViewHolder(final View viewItem) {
            super(viewItem);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            image = (NetworkImageView) viewItem.findViewById(R.id.thumbnailCart);
            card = (CardView)  viewItem.findViewById(R.id.shoppingcart_card);
            nameTxt = (TextView) viewItem.findViewById(R.id.nameCartText);
            brandTxt = (TextView) viewItem.findViewById(R.id.brandCartText);
            priceTxt = (TextView) viewItem.findViewById(R.id.priceCartText);
            descriptionTxt = (TextView) viewItem.findViewById(R.id.descriptionCartText);
            sizeTxt = (TextView) viewItem.findViewById(R.id.sizeCartText);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent mainIntent = new Intent(viewItem.getContext(), ProductDetailActivity.class);
                    Bundle id = new Bundle();
                    id.putString("productID", String.valueOf(list.get(getAdapterPosition()).getProductID()));
                    Fragment newFragment = new ProductDetailFragment();
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                    newFragment.setArguments(id);
                    transaction.replace(R.id.frame_layout, newFragment);
                    transaction.addToBackStack(null);
                    // Commit the transaction
                    transaction.commit();
                }
            });

        }

    }
}
