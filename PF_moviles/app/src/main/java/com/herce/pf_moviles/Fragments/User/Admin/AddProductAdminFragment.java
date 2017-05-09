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

import static com.herce.pf_moviles.Services.Services.PRODUCTS_API;

public class AddProductAdminFragment extends Fragment {

    private EditText nameTxt, brandTxt, descriptionTxt, priceTxt, categoryTxt, imageURLTxt;
    private Button addBtn;
    ProgressDialog progress_bar;

    public static AddProductAdminFragment newInstance() {
        AddProductAdminFragment fragment = new AddProductAdminFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product_admin, container, false);
        view.setBackgroundResource(R.color.white);

        progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);

        nameTxt = (EditText) view.findViewById(R.id.nameProdAdminAddField);
        brandTxt = (EditText) view.findViewById(R.id.brandProdAdminAddField);
        descriptionTxt = (EditText) view.findViewById(R.id.descriptionProdAdminAddField);
        priceTxt = (EditText) view.findViewById(R.id.priceProdAdminAddField);
        categoryTxt = (EditText) view.findViewById(R.id.categoryProdAdminAddField);
        imageURLTxt = (EditText) view.findViewById(R.id.imageurlProdAdminAddField);

        addBtn = (Button) view.findViewById(R.id.addProdAdminAddButton);

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
                .setTitle("Insert Product?")
                .setMessage("Are you sure you want to create this product?")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        doProductInsert();
                    }
                })
                .create()
                .show();
    }

    void doProductInsert() {

        progress_bar.show();

        StringRequest addProduct = new StringRequest(Request.Method.POST, PRODUCTS_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if(res.getString("code").equals("01"))
                            {
                                Toast.makeText(getContext(), "Product Inserted" , Toast.LENGTH_SHORT).show();

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
                params.put("name",nameTxt.getText().toString());
                params.put("description",descriptionTxt.getText().toString());
                params.put("image_url",imageURLTxt.getText().toString());
                params.put("price",priceTxt.getText().toString());
                params.put("brand",brandTxt.getText().toString());
                params.put("id_category",categoryTxt.getText().toString());

                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(addProduct);
    }
}
