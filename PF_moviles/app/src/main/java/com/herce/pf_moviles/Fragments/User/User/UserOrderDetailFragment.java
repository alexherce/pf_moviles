package com.herce.pf_moviles.Fragments.User.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Objects.User;
import com.herce.pf_moviles.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.herce.pf_moviles.Services.Services.CANCEL_ORDER_API;
import static com.herce.pf_moviles.Services.Services.ORDERS_API;
import static com.herce.pf_moviles.Services.Services.USER_PROFILE;


public class UserOrderDetailFragment extends Fragment {

    TextView idTxt, dateTxt, totalTxt, addressTxt, stateTxt, zipCodeTxt, statusTxt;
    String orderID;
    Button viewProductsBtn, cancelBtn;
    ProgressDialog progress_bar;

    public static UserOrderDetailFragment newInstance() {
        UserOrderDetailFragment fragment = new UserOrderDetailFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_order_detail, container, false);
        view.setBackgroundResource(R.color.white);

        idTxt = (TextView)view.findViewById(R.id.idOrderDetailText);
        dateTxt  = (TextView)view.findViewById(R.id.dateOrderDetailText);
        totalTxt  = (TextView)view.findViewById(R.id.totalOrderDetailText);
        statusTxt  = (TextView)view.findViewById(R.id.stateOrderDetailText);
        addressTxt = (TextView)view.findViewById(R.id.addressOrderDetailText);
        zipCodeTxt  = (TextView)view.findViewById(R.id.zipcodeOrderDetailText);
        stateTxt = (TextView)view.findViewById(R.id.stateOrderDetailText);
        viewProductsBtn = (Button)view.findViewById(R.id.orderedProductsButton);
        cancelBtn = (Button)view.findViewById(R.id.orderCancelButton);

        Bundle myIntent = this.getArguments();

        if(myIntent != null) {
            orderID = myIntent.getString("orderID");
        }

        progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        StringRequest userProfileReq = new StringRequest(Request.Method.GET, ORDERS_API + "?order_id=" + orderID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01"))
                            {
                                JSONObject orderDetails = res.getJSONObject("order_data");

                                idTxt.setText(orderDetails.getString("id"));
                                dateTxt.setText(orderDetails.getString("date_created"));
                                totalTxt.setText("$" + orderDetails.getDouble("total"));
                                if(orderDetails.getInt("status") == 1) {
                                    statusTxt.setText(R.string.orderPendingText);
                                    cancelBtn.setVisibility(View.VISIBLE);
                                } else if (orderDetails.getInt("status") == 2) {
                                    statusTxt.setText(R.string.orderShippingText);
                                } else if (orderDetails.getInt("status") == 3) {
                                    statusTxt.setText(R.string.orderShippedText);
                                } else {
                                    statusTxt.setText(R.string.orderCompletedText);
                                }
                                addressTxt.setText(orderDetails.getString("shipping_address"));
                                zipCodeTxt.setText(orderDetails.getString("zip_code"));
                                stateTxt.setText(orderDetails.getString("state"));

                            } else if (res.getString("code").equals("02"))
                            {
                                Toast.makeText(getContext(), R.string.missingValuesText , Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(getContext()).add(userProfileReq);


        viewProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle id = new Bundle();
                id.putString("orderID", orderID);
                Fragment newFragment = new UserOrderProductsFragment();
                newFragment.setArguments(id);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void cancelOrder() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.cancelOrderText)
                .setMessage(R.string.confirmCancelOrderText)
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        doCancelOrder();
                    }
                })
                .create()
                .show();
    }

    void doCancelOrder() {
        progress_bar.show();

        StringRequest editProfileReq = new StringRequest(Request.Method.POST, CANCEL_ORDER_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01"))
                            {

                                Toast.makeText(getContext(), R.string.orderCanceledText , Toast.LENGTH_SHORT).show();

                                Fragment newFragment = new UserOrdersFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();

                            } else if (res.getString("code").equals("02"))
                            {
                                Toast.makeText(getContext(), R.string.missingValuesText , Toast.LENGTH_SHORT).show();
                            } else if (res.getString("code").equals("04"))
                            {
                                Toast.makeText(getContext(), R.string.queryErrorText , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), R.string.unknownResponseText , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_order", "" + orderID);

                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(editProfileReq);
    }
}
