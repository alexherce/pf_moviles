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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Fragments.User.User.ProductsFragment;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Services.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.herce.pf_moviles.Services.Services.INVENTORY_API;
import static com.herce.pf_moviles.Services.Services.INVENTORY_DELETE_API;
import static com.herce.pf_moviles.Services.Services.PRODUCTS_API;
import static com.herce.pf_moviles.Services.Services.PRODUCT_DELETE_API;

public class InventoryDetailAdminFragment extends Fragment {

    private String inventoryID;
    private EditText productTxt, storeTxt, quantityTxt, sizeTxt;
    private String productID, storeID, quantity, size, imageURL;
    private Button updateBtn, deleteBtn;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    NetworkImageView image;
    ProgressDialog progress_bar;

    public static InventoryDetailAdminFragment newInstance() {
        InventoryDetailAdminFragment fragment = new InventoryDetailAdminFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_detail_admin, container, false);
        view.setBackgroundResource(R.color.white);

        progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        productTxt = (EditText) view.findViewById(R.id.productInventoryDetailAdminText);
        storeTxt = (EditText) view.findViewById(R.id.storeInventoryDetailAdminText);
        quantityTxt = (EditText) view.findViewById(R.id.quantityInventoryDetailAdminText);
        sizeTxt = (EditText) view.findViewById(R.id.sizeInventoryDetailAdminText);

        updateBtn = (Button) view.findViewById(R.id.editInventoryDetailAdminText);
        deleteBtn = (Button) view.findViewById(R.id.deleteInventoryDetailAdminText);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        image = (NetworkImageView) view.findViewById(R.id.thumbnailInventoryDetailAdminText);

        Bundle myIntent = this.getArguments();

        if (myIntent != null) {
            inventoryID = myIntent.getString("inventoryID");
        }

        StringRequest productsReq = new StringRequest(Request.Method.GET, INVENTORY_API + "?id=" + inventoryID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01")) {
                                JSONObject product = res.getJSONObject("inventory_data");

                                productID = product.getString("id_product");
                                storeID = product.getString("id_store");
                                size = product.getString("size");
                                quantity = product.getString("quantity");
                                imageURL = product.getString("image_url");

                                productTxt.setText(productID, TextView.BufferType.EDITABLE);
                                storeTxt.setText(storeID, TextView.BufferType.EDITABLE);
                                sizeTxt.setText(size, TextView.BufferType.EDITABLE);
                                quantityTxt.setText(quantity, TextView.BufferType.EDITABLE);
                                image.setImageUrl(imageURL, imageLoader);


                            } else if (res.getString("code").equals("04")) {
                                Toast.makeText(getContext(), R.string.queryErrorText, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), R.string.unknownResponseText, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error! " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUpdateProduct();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteProduct();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void confirmUpdateProduct() {
        new AlertDialog.Builder(getContext())
                .setTitle("Update Inventory?")
                .setMessage("Are you sure you want to update this inventory item? This action can't be undone")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doInventoryUpdate();
                    }
                })
                .create()
                .show();
    }

    void confirmDeleteProduct() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Inventory Item?")
                .setMessage("Are you sure you want to delete this inventory item? This action can't be undone")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doInventoryDelete();
                    }
                })
                .create()
                .show();
    }

    void doInventoryUpdate() {

        progress_bar.show();

        StringRequest updateProduct = new StringRequest(Request.Method.PUT, INVENTORY_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01")) {
                                Toast.makeText(getContext(), "Inventory Updated", Toast.LENGTH_SHORT).show();

                                Fragment newFragment = new InventoryAdminFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout_admin, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();

                            } else if (res.getString("code").equals("02")) {
                                Toast.makeText(getContext(), R.string.missingValuesText, Toast.LENGTH_SHORT).show();
                            } else if (res.getString("code").equals("04")) {
                                Toast.makeText(getContext(), R.string.queryErrorText, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), R.string.unknownResponseText, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error! " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.cancel();
                        Toast.makeText(getContext(), "Error: " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", inventoryID);
                params.put("id_product", productTxt.getText().toString());
                params.put("id_store", storeTxt.getText().toString());
                params.put("size", sizeTxt.getText().toString());
                params.put("quantity", quantityTxt.getText().toString());

                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(updateProduct);

    }

    void doInventoryDelete() {

        progress_bar.show();

        StringRequest deleteProduct = new StringRequest(Request.Method.POST, INVENTORY_DELETE_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01")) {
                                Toast.makeText(getContext(), "Inventory Item Deleted", Toast.LENGTH_SHORT).show();

                                Fragment newFragment = new ProductsFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout_admin, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();

                            } else if (res.getString("code").equals("02")) {
                                Toast.makeText(getContext(), R.string.missingValuesText, Toast.LENGTH_SHORT).show();
                            } else if (res.getString("code").equals("04")) {
                                Toast.makeText(getContext(), R.string.queryErrorText, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), R.string.unknownResponseText, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error! " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.cancel();
                        Toast.makeText(getContext(), "Error: " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", inventoryID);

                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(deleteProduct);
    }
}