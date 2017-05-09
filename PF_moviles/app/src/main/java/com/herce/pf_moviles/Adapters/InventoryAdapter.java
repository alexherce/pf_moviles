package com.herce.pf_moviles.Adapters;

import android.content.Context;
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
import com.herce.pf_moviles.Fragments.User.Admin.InventoryDetailAdminFragment;
import com.herce.pf_moviles.Fragments.User.Admin.ProductDetailAdminFragment;
import com.herce.pf_moviles.Fragments.User.All.ProductDetailFragment;
import com.herce.pf_moviles.Objects.Inventory;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.Objects.User;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Services.AppController;

import java.util.ArrayList;

/**
 * Created by herce on 5/9/17.
 */

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder>{

    ArrayList<Inventory> list = null;
    Context context = null;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public InventoryAdapter(Context context, ArrayList<Inventory> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public InventoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View childView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_card, parent, false);
        return new ViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(InventoryAdapter.ViewHolder holder,int position) {
        String name = list.get(position).getProduct_name();
        String imageURL = list.get(position).getProduct_image();
        String brand = list.get(position).getProduct_brand();
        String price = list.get(position).getProduct_price();
        Double size = list.get(position).getSize();
        Integer quantity = list.get(position).getQuantity();
        String store = list.get(position).getStore_name();

        holder.nameTxt.setText(name);
        holder.brandTxt.setText(brand);
        holder.priceTxt.setText("$" + price.toString());
        holder.image.setImageUrl(imageURL, imageLoader);
        holder.sizeTxt.setText("" + size);
        holder.quantityTxt.setText("" + quantity);
        holder.storeTxt.setText(store);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTxt, brandTxt, priceTxt, sizeTxt, storeTxt, quantityTxt;
        NetworkImageView image;
        CardView card;

        public ViewHolder(final View viewItem) {
            super(viewItem);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            image = (NetworkImageView) viewItem.findViewById(R.id.thumbnailInventory);
            card = (CardView)  viewItem.findViewById(R.id.inventory_card);
            nameTxt = (TextView) viewItem.findViewById(R.id.nameInventoryText);
            brandTxt = (TextView) viewItem.findViewById(R.id.brandInventoryText);
            priceTxt = (TextView) viewItem.findViewById(R.id.priceInventoryText);
            quantityTxt = (TextView) viewItem.findViewById(R.id.quantityInventoryText);
            storeTxt = (TextView) viewItem.findViewById(R.id.storeInventoryText);
            sizeTxt = (TextView) viewItem.findViewById(R.id.sizeInventoryText);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle id = new Bundle();
                    id.putString("inventoryID", String.valueOf(list.get(getAdapterPosition()).getInventoryID()));

                    Fragment newFragment = new InventoryDetailAdminFragment();
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                    newFragment.setArguments(id);
                    transaction.replace(R.id.frame_layout_admin, newFragment);
                    transaction.addToBackStack(null);
                    // Commit the transaction
                    transaction.commit();
                }
            });

        }

    }
}
