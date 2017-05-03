package com.herce.pf_moviles.Utilities;

import android.util.Log;

import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Services.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParserJSONProducts {

    public static ArrayList<Product> _array_products = new ArrayList<>();

    public static Product paseaObjeto(JSONObject obj) {

        try {
            Product product = new Product();

            product.setProductID(obj.getInt("id"));
            product.setName(obj.getString("name"));
            product.setDescription(obj.getString("description"));
            product.setBrand(obj.getString("brand"));
            product.setImageURL(obj.getString("image_url"));
            product.setPrice(obj.getDouble("price"));
            product.setCategory(obj.getInt("id_category"));
            return product;

        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Product> parseaArreglo(JSONArray arr) {

        JSONObject obj = null;
        Product product = null;
        _array_products.clear();

        try {
            for(int i = 0;i<arr.length();i++) {

                obj = arr.getJSONObject(i);
                product = new Product();

                product.setProductID(obj.getInt("id"));
                product.setName(obj.getString("name"));
                product.setDescription(obj.getString("description"));
                product.setBrand(obj.getString("brand"));
                product.setImageURL(obj.getString("image_url"));
                product.setPrice(obj.getDouble("price"));
                product.setCategory(obj.getInt("id_category"));

                _array_products.add(product);
            }
            return _array_products;

        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Product> parseaArregloOrderProducts(JSONArray arr) {

        JSONObject obj = null;
        Product product = null;
        _array_products.clear();

        try {
            for(int i = 0;i<arr.length();i++) {

                obj = arr.getJSONObject(i);
                product = new Product();

                product.setProductID(obj.getInt("id"));
                product.setName(obj.getString("name"));
                if(obj.getInt("status") == 1) {
                    product.setDescription(AppController.getInstance().getApplicationContext().getResources().getString(R.string.orderPendingText));
                } else if (obj.getInt("status") == 2) {
                    product.setDescription(AppController.getInstance().getApplicationContext().getResources().getString(R.string.orderShippedText));
                } else {
                    product.setDescription(AppController.getInstance().getApplicationContext().getResources().getString(R.string.orderCompletedText));
                }
                product.setBrand(obj.getString("brand"));
                product.setImageURL(obj.getString("image_url"));
                product.setPrice(obj.getDouble("subtotal"));
                product.setSize((float) obj.getDouble("size"));

                _array_products.add(product);
            }
            return _array_products;

        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> stringArray(JSONArray arr){

        JSONObject obj=null;
        ArrayList<String> res = new ArrayList<>();

        try {
            for(int i = 0;i<arr.length();i++) {
                obj = arr.getJSONObject(i);

                res.add(obj.getString("nombre"));
            }
            Log.i("Debug note: ", "returned " + res.toString());
            return res;

        } catch (JSONException e1) {
            Log.e("Error: ", "returned null");
            e1.printStackTrace();
            return null;
        }

    }
}
