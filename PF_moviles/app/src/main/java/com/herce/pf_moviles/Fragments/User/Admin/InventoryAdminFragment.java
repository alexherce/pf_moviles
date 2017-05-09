package com.herce.pf_moviles.Fragments.User.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Adapters.InventoryAdapter;
import com.herce.pf_moviles.Adapters.ProductsAdapter;
import com.herce.pf_moviles.Objects.Inventory;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Utilities.ParserJSONInventory;
import com.herce.pf_moviles.Utilities.ParserJSONProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.herce.pf_moviles.Services.Services.INVENTORY_API;
import static com.herce.pf_moviles.Services.Services.PRODUCTS_API;

public class InventoryAdminFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Inventory> inventoryList;
    private InventoryAdapter adaptador;

    public static InventoryAdminFragment newInstance() {
        InventoryAdminFragment fragment = new InventoryAdminFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_admin, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.inventory_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        inventoryList = new ArrayList<>();
        adaptador = new InventoryAdapter(getContext(), inventoryList);
        recyclerView.setAdapter(adaptador);

        final ProgressDialog progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        StringRequest productsReq = new StringRequest(Request.Method.GET, INVENTORY_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01"))
                            {
                                JSONArray products = res.getJSONArray("inventory_data");
                                inventoryList = ParserJSONInventory.parseaArreglo(products);

                                adaptador = new InventoryAdapter(getContext(), inventoryList);
                                recyclerView.setAdapter(adaptador);
                                adaptador.notifyDataSetChanged();

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

        adaptador.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
