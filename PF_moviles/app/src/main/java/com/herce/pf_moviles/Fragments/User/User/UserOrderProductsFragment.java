package com.herce.pf_moviles.Fragments.User.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Adapters.OrdersAdapter;
import com.herce.pf_moviles.Adapters.ShoppingCartAdapter;
import com.herce.pf_moviles.Objects.Order;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.Objects.ShoppingCart;
import com.herce.pf_moviles.Objects.User;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Utilities.ParserJSONOrders;
import com.herce.pf_moviles.Utilities.ParserJSONProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.herce.pf_moviles.Services.Services.ORDERS_API;
import static com.herce.pf_moviles.Services.Services.ORDER_PRODUCT_API;

public class UserOrderProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Product> productList;
    private ShoppingCartAdapter adapter;
    String orderID;

    public static UserOrderProductsFragment newInstance() {
        UserOrderProductsFragment fragment = new UserOrderProductsFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_order_products, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.orderproducts_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();
        adapter = new ShoppingCartAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);

        Bundle myIntent = this.getArguments();

        if(myIntent != null) {
            orderID = myIntent.getString("orderID");
        }

        final ProgressDialog progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        StringRequest productsReq = new StringRequest(Request.Method.GET, ORDER_PRODUCT_API + "?id_order=" + orderID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01"))
                            {
                                JSONArray products = res.getJSONArray("order_products_data");
                                productList = ParserJSONProducts.parseaArregloOrderProducts(products);

                                adapter = new ShoppingCartAdapter(getContext(), productList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            } else if (res.getString("code").equals("04"))
                            {
                                Toast.makeText(getContext(), R.string.queryErrorText , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), R.string.unknownResponseText , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error! " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.cancel();
                        Toast.makeText(getContext(), R.string.commsErrorText + " " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        Volley.newRequestQueue(getContext()).add(productsReq);

        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.viewOrderProductsButton);
    }

    public void onStop () {
        super.onStop();
        getActivity().setTitle(R.string.app_name);
    }

    public void onDestroy () {
        super.onDestroy();
        getActivity().setTitle(R.string.app_name);
    }
}
