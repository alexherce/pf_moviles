package com.herce.pf_moviles.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.herce.pf_moviles.Activities.Employee.CheckoutEmployeeActivity;
import com.herce.pf_moviles.Activities.User.CheckoutUserActivity;
import com.herce.pf_moviles.Activities.User.MainActivityUser;
import com.herce.pf_moviles.Adapters.ProductsAdapter;
import com.herce.pf_moviles.Adapters.ShoppingCartAdapter;
import com.herce.pf_moviles.Objects.Product;
import com.herce.pf_moviles.Objects.ShoppingCart;
import com.herce.pf_moviles.Objects.User;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Utilities.CommonClass;

import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShoppingCartAdapter adapter;
    private Button checkoutBtn;
    private TextView totalTxt;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        View view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.grey);

        checkoutBtn = (Button)findViewById(R.id.checkoutButton);
        totalTxt = (TextView)findViewById(R.id.totalCartText);

        totalTxt.setText(ShoppingCartActivity.this.getString(R.string.shoppingCartTotalText) + " $" + ShoppingCart.getInstance().getShoppingCartTotal());

        recyclerView = (RecyclerView) findViewById(R.id.shoppingcart_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ShoppingCartAdapter(this, ShoppingCart.getInstance().shoppingCartArray);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShoppingCart.getInstance().getShoppingCartTotal() > 0) {
                    doCheckout();
                } else {
                    Toast.makeText(ShoppingCartActivity.this, R.string.cartEmptyText , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void doCheckout() {
        if(User.getInstance().getRole() == 3) {
            Intent actividad = new Intent(ShoppingCartActivity.this, CheckoutUserActivity.class);
            startActivity(actividad);
        } else if (User.getInstance().getRole() < 3 && User.getInstance().getRole() > 0)
        {
            Intent actividad = new Intent(ShoppingCartActivity.this, CheckoutEmployeeActivity.class);
            startActivity(actividad);
        } else {
            Toast.makeText(ShoppingCartActivity.this, R.string.credentialsErrorText , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return CommonClass.HandleMenu(this, item.getItemId());
    }

    public void onStop () {
        super.onStop();
        adapter.notifyDataSetChanged();
    }

    public void onDestroy () {
        super.onDestroy();
        adapter.notifyDataSetChanged();
    }
}
