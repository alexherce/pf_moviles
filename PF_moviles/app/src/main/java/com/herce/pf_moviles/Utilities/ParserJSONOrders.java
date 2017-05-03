package com.herce.pf_moviles.Utilities;

import android.util.Log;

import com.herce.pf_moviles.Objects.Order;
import com.herce.pf_moviles.Objects.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Herce on 03/05/2017.
 */

public class ParserJSONOrders {

    public static ArrayList<Order> _array_orders = new ArrayList<>();

    public static Order paseaObjeto(JSONObject obj) {

        try {
            Order order = new Order();

            order.setOrderID(obj.getInt("id"));
            order.setCustomerID(obj.getInt("id_customer"));
            order.setCustomerID(obj.getInt("id_seller"));
            order.setDateCreated(obj.getString("date_created"));
            order.setTotal(obj.getDouble("total"));
            order.setStatus(obj.getInt("status"));
            return order;

        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Order> parseaArreglo(JSONArray arr) {

        JSONObject obj = null;
        Order order = null;
        _array_orders.clear();

        try {
            for(int i = 0;i<arr.length();i++) {

                obj = arr.getJSONObject(i);
                order = new Order();

                order.setOrderID(obj.getInt("id"));
                order.setCustomerID(obj.getInt("id_customer"));
                order.setCustomerID(obj.getInt("id_seller"));
                order.setDateCreated(obj.getString("date_created"));
                order.setTotal(obj.getDouble("total"));
                order.setStatus(obj.getInt("status"));

                _array_orders.add(order);
            }
            return _array_orders;

        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
