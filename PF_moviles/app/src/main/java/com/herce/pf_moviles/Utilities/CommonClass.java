package com.herce.pf_moviles.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.herce.pf_moviles.Activities.LoginActivity;
import com.herce.pf_moviles.Fragments.User.Admin.AddInventoryAdminFragment;
import com.herce.pf_moviles.Fragments.User.Admin.AddProductAdminFragment;
import com.herce.pf_moviles.Fragments.User.All.ShoppingCartFragment;
import com.herce.pf_moviles.Objects.ShoppingCart;
import com.herce.pf_moviles.Objects.User;
import com.herce.pf_moviles.R;

/**
 * Created by Herce on 01/05/2017.
 */

public class CommonClass {

    public static boolean HandleMenu(Context c, int MenuEntry) {
        Intent actividad;
        Fragment newFragment;
        FragmentTransaction transaction;

        switch (MenuEntry) {
            case R.id.action_cart:
                newFragment = new ShoppingCartFragment();
                transaction = ((AppCompatActivity)c).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.logoutMenuBtn:
                ShoppingCart.getInstance().shoppingCartArray.clear();
                ShoppingCart.getInstance().setShoppingCartTotal(0.0);
                actividad = new Intent(c, LoginActivity.class);
                actividad.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(actividad);
                break;
            case R.id.addProductMenuBtn:
                newFragment = new AddProductAdminFragment();
                transaction = ((AppCompatActivity)c).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout_admin, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.addInventoryMenuBtn:
                newFragment = new AddInventoryAdminFragment();
                transaction = ((AppCompatActivity)c).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout_admin, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
        return true;
    }
}
