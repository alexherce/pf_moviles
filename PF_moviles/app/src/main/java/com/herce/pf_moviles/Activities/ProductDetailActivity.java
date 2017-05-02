package com.herce.pf_moviles.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.herce.pf_moviles.Activities.User.MainActivityUser;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.Objects.ShoppingCart;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Services.AppController;
import com.herce.pf_moviles.Utilities.CommonClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.herce.pf_moviles.Services.Services.PRODUCTS_API;
import static com.herce.pf_moviles.Utilities.CommonClass.*;

public class ProductDetailActivity extends AppCompatActivity {

    private String productID;
    private TextView nameTxt, brandTxt, descriptionTxt, priceTxt, sizeTxt;
    private String name, brand, description, imageURL;
    private Double price;
    private Float size;
    private Integer id;
    private Button addToCartBtn;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    NetworkImageView image;
    SeekBar sizeBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        final ProgressDialog progress_bar = new ProgressDialog(ProductDetailActivity.this);
        progress_bar.setMessage(ProductDetailActivity.this.getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        nameTxt = (TextView) findViewById(R.id.nameDetailText);
        brandTxt = (TextView) findViewById(R.id.brandDetailText);
        descriptionTxt = (TextView) findViewById(R.id.descriptionDetailText);
        priceTxt = (TextView) findViewById(R.id.priceDetailText);
        sizeTxt = (TextView) findViewById(R.id.sizeText);
        addToCartBtn = (Button) findViewById(R.id.addToCartButton);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        image = (NetworkImageView) findViewById(R.id.thumbnailDetail);
        sizeBar = (SeekBar)findViewById(R.id.sizeBar);

        Bundle myIntent = getIntent().getExtras();

        sizeBar.setProgress(0);
        sizeBar.incrementProgressBy(1);
        sizeBar.setMax(25);
        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                size = (float)progress / 2;
                sizeTxt.setText(ProductDetailActivity.this.getString(R.string.pickSizeText) + " " + size);
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
                                Toast.makeText(ProductDetailActivity.this, R.string.queryErrorText , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProductDetailActivity.this, R.string.unknownResponseText , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProductDetailActivity.this, "Error! " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.cancel();
                        Toast.makeText(ProductDetailActivity.this, R.string.commsErrorText + " " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(productsReq);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size > 5) {
                    addToCart();
                } else {
                    Toast.makeText(ProductDetailActivity.this, R.string.sizeErrorText, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonClass.HandleMenu(this, item.getItemId());
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
        Toast.makeText(ProductDetailActivity.this, ProductDetailActivity.this.getString(R.string.productAddedToCartText) + " " + size , Toast.LENGTH_SHORT).show();
    }
}
