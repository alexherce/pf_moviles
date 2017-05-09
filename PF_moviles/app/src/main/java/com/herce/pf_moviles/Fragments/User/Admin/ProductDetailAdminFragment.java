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
import com.herce.pf_moviles.Fragments.User.User.UserOrdersFragment;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.Objects.ShoppingCart;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Services.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.herce.pf_moviles.Services.Services.PRODUCTS_API;
import static com.herce.pf_moviles.Services.Services.PRODUCT_DELETE_API;

public class ProductDetailAdminFragment extends Fragment {

    private String productID;
    private EditText nameTxt, brandTxt, descriptionTxt, priceTxt, categoryTxt, imageURLTxt;
    private String name, brand, description, imageURL, category;
    private Double price;
    private Float size = 0.0f;
    private Integer id;
    private Button updateBtn, deleteBtn;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    NetworkImageView image;
    SeekBar sizeBar;
    ProgressDialog progress_bar;

    public static ProductDetailAdminFragment newInstance() {
        ProductDetailAdminFragment fragment = new ProductDetailAdminFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail_admin, container, false);
        view.setBackgroundResource(R.color.white);

        progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        nameTxt = (EditText) view.findViewById(R.id.nameProdAdminDetailField);
        brandTxt = (EditText) view.findViewById(R.id.brandProdAdminDetailField);
        descriptionTxt = (EditText) view.findViewById(R.id.descriptionProdAdminDetailField);
        priceTxt = (EditText) view.findViewById(R.id.priceProdAdminDetailField);
        categoryTxt = (EditText) view.findViewById(R.id.categoryProdAdminDetailField);
        imageURLTxt = (EditText) view.findViewById(R.id.imageurlProdAdminDetailField);

        updateBtn = (Button) view.findViewById(R.id.editProdAdminDetailButton);
        deleteBtn = (Button) view.findViewById(R.id.deleteProdAdminDetailButton);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        image = (NetworkImageView) view.findViewById(R.id.thumbnailAdminDetailField);

        Bundle myIntent = this.getArguments();

        if(myIntent != null) {
            productID = myIntent.getString("productID");
        }

        StringRequest productsReq = new StringRequest(Request.Method.GET, PRODUCTS_API + "?id=" + productID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01"))
                            {
                                JSONArray arrayProducts = res.getJSONArray("product_data");
                                JSONObject product = (JSONObject) arrayProducts.get(0);

                                name = product.getString("name");
                                brand = product.getString("brand");
                                id = product.getInt("id");
                                description = product.getString("description");
                                imageURL = product.getString("image_url");
                                price = product.getDouble("price");
                                category = product.getString("id_category");

                                nameTxt.setText(name, TextView.BufferType.EDITABLE);
                                brandTxt.setText(brand, TextView.BufferType.EDITABLE);
                                descriptionTxt.setText(description, TextView.BufferType.EDITABLE);
                                priceTxt.setText("" + price, TextView.BufferType.EDITABLE);
                                image.setImageUrl(imageURL, imageLoader);
                                categoryTxt.setText(category, TextView.BufferType.EDITABLE);
                                imageURLTxt.setText(imageURL, TextView.BufferType.EDITABLE);


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
                .setTitle("Update Product?")
                .setMessage("Are you sure you want to update this product? This action can't be undone")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        doProductUpdate();
                    }
                })
                .create()
                .show();
    }

    void confirmDeleteProduct() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Product?")
                .setMessage("Are you sure you want to delete this product? This action can't be undone")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        doProductDelete();
                    }
                })
                .create()
                .show();
    }

    void doProductUpdate() {

        progress_bar.show();

        StringRequest updateProduct = new StringRequest(Request.Method.PUT, PRODUCTS_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if(res.getString("code").equals("01"))
                            {
                                Toast.makeText(getContext(), "Product Updated" , Toast.LENGTH_SHORT).show();

                                Fragment newFragment = new ProductsFragment();
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
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",productID);
                params.put("name",nameTxt.getText().toString());
                params.put("description",descriptionTxt.getText().toString());
                params.put("image_url",imageURLTxt.getText().toString());
                params.put("price",priceTxt.getText().toString());
                params.put("brand",brandTxt.getText().toString());
                params.put("id_category",categoryTxt.getText().toString());

                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(updateProduct);

    }

    void doProductDelete() {

        progress_bar.show();

        StringRequest deleteProduct = new StringRequest(Request.Method.POST, PRODUCT_DELETE_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if(res.getString("code").equals("01"))
                            {
                                Toast.makeText(getContext(), "Product Deleted" , Toast.LENGTH_SHORT).show();

                                Fragment newFragment = new ProductsFragment();
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
                params.put("id", productID);

                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(deleteProduct);

    }
}
