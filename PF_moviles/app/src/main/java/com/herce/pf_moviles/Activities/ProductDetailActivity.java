package com.herce.pf_moviles.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
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
import com.herce.pf_moviles.Adapters.ProductsAdapter;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Services.AppController;
import com.herce.pf_moviles.Utilities.ParserJSONProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.herce.pf_moviles.Services.Services.PRODUCTS_API;

public class ProductDetailActivity extends AppCompatActivity {

    private String productID;
    private TextView nameTxt, brandTxt, descriptionTxt, priceTxt;
    private Button addToCartBtn;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    NetworkImageView image;

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
        addToCartBtn = (Button) findViewById(R.id.addToCartButton);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        image = (NetworkImageView) findViewById(R.id.thumbnailDetail);

        Bundle myIntent = getIntent().getExtras();

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

                                nameTxt.setText(product.getString("name"));
                                brandTxt.setText(product.getString("brand"));
                                descriptionTxt.setText("ID: " + product.getString("id") + " | " + product.getString("description"));
                                priceTxt.setText("$" + product.getDouble("price"));
                                image.setImageUrl(product.getString("image_url"), imageLoader);


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
