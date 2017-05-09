package com.herce.pf_moviles.Utilities;

import com.herce.pf_moviles.Objects.Inventory;
import com.herce.pf_moviles.Objects.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by herce on 5/9/17.
 */

public class ParserJSONInventory {

    public static ArrayList<Inventory> _array_products = new ArrayList<>();

    public static ArrayList<Inventory> parseaArreglo(JSONArray arr) {

        JSONObject obj = null;
        Inventory product = null;
        _array_products.clear();

        try {
            for(int i = 0;i<arr.length();i++) {

                obj = arr.getJSONObject(i);
                product = new Inventory();

                product.setInventoryID(obj.getInt("id"));
                product.setProductID(obj.getInt("id_product"));
                product.setStoreID(obj.getInt("id_store"));
                product.setProduct_name(obj.getString("name"));
                product.setProduct_brand(obj.getString("brand"));
                product.setProduct_image(obj.getString("image_url"));
                product.setProduct_price(obj.getString("price"));
                product.setQuantity(obj.getInt("quantity"));
                product.setSize(obj.getDouble("size"));
                product.setStore_name("store");

                _array_products.add(product);
            }
            return _array_products;

        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
