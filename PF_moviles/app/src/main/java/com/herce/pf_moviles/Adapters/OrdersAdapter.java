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
import com.herce.pf_moviles.Fragments.User.All.ProductDetailFragment;
import com.herce.pf_moviles.Fragments.User.User.UserOrderDetailFragment;
import com.herce.pf_moviles.Objects.Order;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Services.AppController;

import java.util.ArrayList;

/**
 * Created by Herce on 02/05/2017.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder>{

    ArrayList<Order> list = null;
    Context context = null;

    public OrdersAdapter(Context context, ArrayList<Order> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View childView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
        return new ViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(OrdersAdapter.ViewHolder holder,int position) {
        Integer id = list.get(position).getOrderID();
        String date = list.get(position).getDateCreated();
        Double total = list.get(position).getTotal();
        Integer status = list.get(position).getStatus();

        holder.orderIDTxt.setText(AppController.getInstance().getApplicationContext().getResources().getString(R.string.orderText) + " #" + id);
        holder.dateTxt.setText(date);
        holder.totalTxt.setText("$" + total.toString());
        if(status == 1) {
            holder.statusTxt.setText(R.string.orderPendingText);
        } else if (status == 2) {
            holder.statusTxt.setText(R.string.orderShippingText);
        } else if (status == 3) {
            holder.statusTxt.setText(R.string.orderShippedText);
        } else {
            holder.statusTxt.setText(R.string.orderCompletedText);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderIDTxt, dateTxt, totalTxt, statusTxt;
        CardView card;

        public ViewHolder(final View viewItem) {
            super(viewItem);
            card = (CardView)  viewItem.findViewById(R.id.order_card);
            orderIDTxt = (TextView) viewItem.findViewById(R.id.orderIDText);
            dateTxt = (TextView) viewItem.findViewById(R.id.orderDateText);
            totalTxt = (TextView) viewItem.findViewById(R.id.orderTotalText);
            statusTxt = (TextView) viewItem.findViewById(R.id.orderStatusText);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle id = new Bundle();
                    id.putString("orderID", String.valueOf(list.get(getAdapterPosition()).getOrderID()));

                    Fragment newFragment = new UserOrderDetailFragment();
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
