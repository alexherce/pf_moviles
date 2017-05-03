package com.herce.pf_moviles.Fragments.User.All;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.Objects.ShoppingCart;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Services.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.herce.pf_moviles.Services.Services.PRODUCTS_API;

public class ProductDetailFragment extends Fragment {

    private String productID;
    private TextView nameTxt, brandTxt, descriptionTxt, priceTxt, sizeTxt;
    private String name, brand, description, imageURL;
    private Double price;
    private Float size = 0.0f;
    private Integer id;
    private Button addToCartBtn;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    NetworkImageView image;
    SeekBar sizeBar;

    public static ProductDetailFragment newInstance() {
        ProductDetailFragment fragment = new ProductDetailFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        view.setBackgroundResource(R.color.white);

        final ProgressDialog progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        nameTxt = (TextView) view.findViewById(R.id.nameDetailText);
        brandTxt = (TextView) view.findViewById(R.id.brandDetailText);
        descriptionTxt = (TextView) view.findViewById(R.id.descriptionDetailText);
        priceTxt = (TextView) view.findViewById(R.id.priceDetailText);
        sizeTxt = (TextView) view.findViewById(R.id.sizeText);
        addToCartBtn = (Button) view.findViewById(R.id.addToCartButton);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        image = (NetworkImageView) view.findViewById(R.id.thumbnailDetail);
        sizeBar = (SeekBar)view.findViewById(R.id.sizeBar);

        Bundle myIntent = this.getArguments();

        sizeBar.setProgress(0);
        sizeBar.incrementProgressBy(1);
        sizeBar.setMax(25);
        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                size = (float)progress / 2;
                sizeTxt.setText(getContext().getString(R.string.pickSizeText) + " " + size);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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

                                nameTxt.setText(name);
                                brandTxt.setText(brand);
                                descriptionTxt.setText("ID: " + id + " | " + description);
                                priceTxt.setText("$" + price);
                                image.setImageUrl(imageURL, imageLoader);


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

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size > 5) {
                    addToCart();
                } else {
                    Toast.makeText(getContext(), R.string.sizeErrorText, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addToCart() {
        Product thisProduct = new Product();
        thisProduct.setProductID(id);
        thisProduct.setName(name);
        thisProduct.setPrice(price);
        thisProduct.setBrand(brand);
        thisProduct.setDescription(description);
        thisProduct.setImageURL(imageURL);
        thisProduct.setSize(size);
        ShoppingCart.getInstance().setShoppingCartTotal(ShoppingCart.getInstance().getShoppingCartTotal() + price);
        ShoppingCart.getInstance().shoppingCartArray.add(thisProduct);
        Toast.makeText(getContext(), getContext().getString(R.string.productAddedToCartText) + " " + size , Toast.LENGTH_SHORT).show();
    }

}
