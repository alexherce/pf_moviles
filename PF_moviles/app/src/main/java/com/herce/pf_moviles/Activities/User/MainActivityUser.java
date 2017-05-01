package com.herce.pf_moviles.Activities.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Activities.Admin.MainActivityAdmin;
import com.herce.pf_moviles.Activities.LoginActivity;
import com.herce.pf_moviles.Activities.MainActivityVentas;
import com.herce.pf_moviles.Adapters.ProductsAdapter;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Utilities.ParserJSONProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.herce.pf_moviles.Services.Services.LOGIN_API;
import static com.herce.pf_moviles.Services.Services.PRODUCTS_API;

public class MainActivityUser extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Product> productList;
    ProductsAdapter adaptador;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        View view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.grey);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.products_recycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        productList = new ArrayList<>();
        adaptador = new ProductsAdapter(this,productList);
        recyclerView.setAdapter(adaptador);

        final ProgressDialog progress_bar = new ProgressDialog(MainActivityUser.this);
        progress_bar.setMessage(MainActivityUser.this.getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        StringRequest productsReq = new StringRequest(Request.Method.GET, PRODUCTS_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01"))
                            {
                                JSONArray products = res.getJSONArray("product_data");
                                productList = ParserJSONProducts.parseaArreglo(products);

                                adaptador = new ProductsAdapter(getApplicationContext(), productList);
                                recyclerView.setAdapter(adaptador);
                                adaptador.notifyDataSetChanged();

                            } else if (res.getString("code").equals("04"))
                            {
                                Toast.makeText(MainActivityUser.this, R.string.queryErrorText , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivityUser.this, R.string.unknownResponseText , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivityUser.this, "Error! " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.cancel();
                        Toast.makeText(MainActivityUser.this, R.string.commsErrorText + " " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(productsReq);

        adaptador.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Toast.makeText(this, "Cart selected", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
