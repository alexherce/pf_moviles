package com.herce.pf_moviles.Activities.User;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Activities.Employee.CheckoutEmployeeActivity;
import com.herce.pf_moviles.Activities.LoginActivity;
import com.herce.pf_moviles.Activities.ShoppingCartActivity;
import com.herce.pf_moviles.Activities.SignupActivity;
import com.herce.pf_moviles.Adapters.ShoppingCartAdapter;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.Objects.ShoppingCart;
import com.herce.pf_moviles.Objects.User;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Utilities.CommonClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.herce.pf_moviles.Services.Services.CREATE_ORDER_API;
import static com.herce.pf_moviles.Services.Services.ORDER_PRODUCT_API;
import static com.herce.pf_moviles.Services.Services.SIGNUP_API;

public class CheckoutUserActivity extends AppCompatActivity {

    ProgressDialog progress_bar;
    boolean resultCreateOrder, productOrderError = false;
    String productOrderErrorMessage;
    private RecyclerView recyclerView;
    private ShoppingCartAdapter adapter;
    ActionBar actionBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_user);
        setTitle(R.string.orderDetailsText);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress_bar = new ProgressDialog(CheckoutUserActivity.this);
        progress_bar.setMessage(CheckoutUserActivity.this.getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        recyclerView = (RecyclerView) findViewById(R.id.orderreview_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ShoppingCartAdapter(this, ShoppingCart.getInstance().shoppingCartArray);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        createOrder();
    }

    public void onStop () {
        super.onStop();
        ShoppingCart.getInstance().shoppingCartArray.clear();
        ShoppingCart.getInstance().setOrderID(0);
        ShoppingCart.getInstance().setShoppingCartTotal(0.0);
    }

    public void onDestroy () {
        super.onDestroy();
        ShoppingCart.getInstance().shoppingCartArray.clear();
        ShoppingCart.getInstance().setOrderID(0);
        ShoppingCart.getInstance().setShoppingCartTotal(0.0);
    }

    void createOrder() {
        StringRequest orderReq = new StringRequest(Request.Method.POST, CREATE_ORDER_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            if(res.getString("code").equals("01"))
                            {
                                ShoppingCart.getInstance().setOrderID(res.getInt("order_id"));
                                orderItems();
                                resultCreateOrder = true;
                            } else if (res.getString("code").equals("02"))
                            {
                                Toast.makeText(CheckoutUserActivity.this, R.string.missingValuesText , Toast.LENGTH_SHORT).show();
                                resultCreateOrder = false;
                            } else if (res.getString("code").equals("04"))
                            {
                                Toast.makeText(CheckoutUserActivity.this, R.string.queryErrorText , Toast.LENGTH_SHORT).show();
                                resultCreateOrder = false;
                            } else {
                                Toast.makeText(CheckoutUserActivity.this, R.string.unknownResponseText , Toast.LENGTH_SHORT).show();
                                resultCreateOrder = false;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CheckoutUserActivity.this, "Error! " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                            resultCreateOrder = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CheckoutUserActivity.this, R.string.commsErrorText + " " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        resultCreateOrder = false;
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_customer", "" + User.getInstance().getUserID());
                params.put("id_seller","5");
                params.put("shipping_address",User.getInstance().getAddress());
                params.put("zip_code",User.getInstance().getZipCode());
                params.put("state",User.getInstance().getState());
                params.put("total","" + ShoppingCart.getInstance().getShoppingCartTotal());
                params.put("online_purchase","1");

                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(orderReq);
    }

    void orderItems() {
        for(int i = 0; i < ShoppingCart.getInstance().shoppingCartArray.size(); i++)
        {
            orderProduct(i);
        }
        progress_bar.cancel();
        adapter.notifyDataSetChanged();
    }

    void orderProduct(final int arrayIndex) {
        StringRequest productOrderReq = new StringRequest(Request.Method.POST, ORDER_PRODUCT_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String tempMessage;
                            JSONObject res = new JSONObject(response);
                            if(res.getString("code").equals("01"))
                            {
                                tempMessage = CheckoutUserActivity.this.getString(R.string.orderedText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            } else if (res.getString("code").equals("02"))
                            {
                                tempMessage = CheckoutUserActivity.this.getString(R.string.notOrderedText) + ": " + CheckoutUserActivity.this.getString(R.string.errorText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            }else if (res.getString("code").equals("03"))
                            {
                                tempMessage = CheckoutUserActivity.this.getString(R.string.notOrderedText) + ": " + CheckoutUserActivity.this.getString(R.string.outOfStockText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            } else if (res.getString("code").equals("04"))
                            {
                                tempMessage = CheckoutUserActivity.this.getString(R.string.notOrderedText) + ": " + CheckoutUserActivity.this.getString(R.string.errorText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            } else if (res.getString("code").equals("05"))
                            {
                                tempMessage = CheckoutUserActivity.this.getString(R.string.notOrderedText) + ": " + CheckoutUserActivity.this.getString(R.string.errorText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            } else {
                                tempMessage = CheckoutUserActivity.this.getString(R.string.notOrderedText) + ": " + CheckoutUserActivity.this.getString(R.string.errorText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            String tempMessage;
                            tempMessage = CheckoutUserActivity.this.getString(R.string.notOrderedText) + ": " + CheckoutUserActivity.this.getString(R.string.errorText);
                            ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String tempMessage;
                        tempMessage = CheckoutUserActivity.this.getString(R.string.notOrderedText) + ": " + CheckoutUserActivity.this.getString(R.string.errorText);
                        ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                        adapter.notifyDataSetChanged();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_order", "" + ShoppingCart.getInstance().getOrderID());
                params.put("id_product",ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).getProductID().toString());
                params.put("quantity","1");
                params.put("size",ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).getSize().toString());

                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(productOrderReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonClass.HandleMenu(this, item.getItemId());
    }
}