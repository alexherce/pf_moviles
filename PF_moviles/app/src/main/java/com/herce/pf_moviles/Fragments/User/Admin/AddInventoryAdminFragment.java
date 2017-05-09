package com.herce.pf_moviles.Fragments.User.Admin;

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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Fragments.User.User.ProductsFragment;
import com.herce.pf_moviles.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.herce.pf_moviles.Services.Services.INVENTORY_API;
import static com.herce.pf_moviles.Services.Services.PRODUCTS_API;

public class AddInventoryAdminFragment extends Fragment {

    private EditText storeTxt, productTxt, quantityTxt, sizeTxt;
    private Button addBtn;
    ProgressDialog progress_bar;

    public static AddInventoryAdminFragment newInstance() {
        AddInventoryAdminFragment fragment = new AddInventoryAdminFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_inventory_admin, container, false);
        view.setBackgroundResource(R.color.white);

        progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);

        storeTxt = (EditText) view.findViewById(R.id.storeInventoryAddAdminText);
        productTxt = (EditText) view.findViewById(R.id.productInventoryAddAdminText);
        quantityTxt = (EditText) view.findViewById(R.id.quantityInventoryAddAdminText);
        sizeTxt = (EditText) view.findViewById(R.id.sizeInventoryAddAdminText);

        addBtn = (Button) view.findViewById(R.id.addInventoryAddAdminButton);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInsertProduct();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void confirmInsertProduct() {
        new AlertDialog.Builder(getContext())
                .setTitle("Insert Product in Inventory?")
                .setMessage("Are you sure you want to insert this product to the inventory?")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        doInventoryInsert();
                    }
                })
                .create()
                .show();
    }

    void doInventoryInsert() {

        progress_bar.show();

        StringRequest addInventory = new StringRequest(Request.Method.POST, INVENTORY_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if(res.getString("code").equals("01"))
                            {
                                Toast.makeText(getContext(), "Inventory Inserted" , Toast.LENGTH_SHORT).show();

                                Fragment newFragment = new InventoryAdminFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout_admin, newFragment);
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
                        Toast.makeText(getContext(), "Error: " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_product",productTxt.getText().toString());
                params.put("id_store",storeTxt.getText().toString());
                params.put("quantity",quantityTxt.getText().toString());
                params.put("size",sizeTxt.getText().toString());

                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(addInventory);
    }
}
