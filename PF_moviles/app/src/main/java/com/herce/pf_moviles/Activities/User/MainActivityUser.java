package com.herce.pf_moviles.Activities.User;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.herce.pf_moviles.Fragments.User.User.ProductsFragment;
import com.herce.pf_moviles.Fragments.User.User.UserOrdersFragment;
import com.herce.pf_moviles.Adapters.ProductsAdapter;
import com.herce.pf_moviles.Fragments.User.User.UserProfileFragment;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Utilities.CommonClass;

import java.util.ArrayList;

public class MainActivityUser extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = ProductsFragment.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = UserOrdersFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                selectedFragment = UserProfileFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ProductsFragment.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonClass.HandleMenu(this, item.getItemId());
    }
}
