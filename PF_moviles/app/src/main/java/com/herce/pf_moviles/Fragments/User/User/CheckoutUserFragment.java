package com.herce.pf_moviles.Fragments.User.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Adapters.ShoppingCartAdapter;
import com.herce.pf_moviles.Objects.ShoppingCart;
import com.herce.pf_moviles.Objects.User;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Utilities.CommonClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.herce.pf_moviles.Services.Services.ORDERS_API;
import static com.herce.pf_moviles.Services.Services.ORDER_PRODUCT_API;

public class CheckoutUserFragment extends Fragment {

    ProgressDialog progress_bar;
    boolean resultCreateOrder, productOrderError = false;
    private RecyclerView recyclerView;
    private ShoppingCartAdapter adapter;

    public static CheckoutUserFragment newInstance() {
        CheckoutUserFragment fragment = new CheckoutUserFragment();
        return fragment;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_user, container, false);

        progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        recyclerView = (RecyclerView) view.findViewById(R.id.orderreview_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ShoppingCartAdapter(getContext(), ShoppingCart.getInstance().shoppingCartArray);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        createOrder();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.orderDetailsText);
    }

    void createOrder() {
        StringRequest orderReq = new StringRequest(Request.Method.POST, ORDERS_API,
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
                                Toast.makeText(getContext(), R.string.missingValuesText , Toast.LENGTH_SHORT).show();
                                resultCreateOrder = false;
                            } else if (res.getString("code").equals("04"))
                            {
                                Toast.makeText(getContext(), R.string.queryErrorText , Toast.LENGTH_SHORT).show();
                                resultCreateOrder = false;
                            } else {
                                Toast.makeText(getContext(), R.string.unknownResponseText , Toast.LENGTH_SHORT).show();
                                resultCreateOrder = false;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error! " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                            resultCreateOrder = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), R.string.commsErrorText + " " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

        Volley.newRequestQueue(getContext()).add(orderReq);
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
                                tempMessage = getContext().getString(R.string.orderedText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            } else if (res.getString("code").equals("02"))
                            {
                                tempMessage = getContext().getString(R.string.notOrderedText) + ": " + getContext().getString(R.string.errorText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            }else if (res.getString("code").equals("03"))
                            {
                                tempMessage = getContext().getString(R.string.notOrderedText) + ": " + getContext().getString(R.string.outOfStockText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            } else if (res.getString("code").equals("04"))
                            {
                                tempMessage = getContext().getString(R.string.notOrderedText) + ": " + getContext().getString(R.string.errorText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            } else if (res.getString("code").equals("05"))
                            {
                                tempMessage = getContext().getString(R.string.notOrderedText) + ": " + getContext().getString(R.string.errorText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            } else {
                                tempMessage = getContext().getString(R.string.notOrderedText) + ": " + getContext().getString(R.string.errorText);
                                ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            String tempMessage;
                            tempMessage = getContext().getString(R.string.notOrderedText) + ": " + getContext().getString(R.string.errorText);
                            ShoppingCart.getInstance().shoppingCartArray.get(arrayIndex).setDescription(tempMessage);
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String tempMessage;
                        tempMessage = getContext().getString(R.string.notOrderedText) + ": " + getContext().getString(R.string.errorText);
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
        Volley.newRequestQueue(getContext()).add(productOrderReq);
    }

    public void onStop () {
        super.onStop();
        getActivity().setTitle(R.string.app_name);
        ShoppingCart.getInstance().shoppingCartArray.clear();
        ShoppingCart.getInstance().setOrderID(0);
        ShoppingCart.getInstance().setShoppingCartTotal(0.0);
    }

    public void onDestroy () {
        super.onDestroy();
        getActivity().setTitle(R.string.app_name);
        ShoppingCart.getInstance().shoppingCartArray.clear();
        ShoppingCart.getInstance().setOrderID(0);
        ShoppingCart.getInstance().setShoppingCartTotal(0.0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonClass.HandleMenu(getContext(), item.getItemId());
    }

}
