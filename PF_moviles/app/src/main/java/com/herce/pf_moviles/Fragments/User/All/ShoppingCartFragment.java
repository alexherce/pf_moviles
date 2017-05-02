package com.herce.pf_moviles.Fragments.User.All;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Activities.Employee.CheckoutEmployeeActivity;
import com.herce.pf_moviles.Activities.ShoppingCartActivity;
import com.herce.pf_moviles.Activities.User.CheckoutUserActivity;
import com.herce.pf_moviles.Adapters.ShoppingCartAdapter;
import com.herce.pf_moviles.Fragments.User.Admin.CheckoutAdminFragment;
import com.herce.pf_moviles.Fragments.User.User.CheckoutUserFragment;
import com.herce.pf_moviles.Objects.ShoppingCart;
import com.herce.pf_moviles.Objects.User;
import com.herce.pf_moviles.R;
import com.herce.pf_moviles.Services.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.herce.pf_moviles.Services.Services.PRODUCTS_API;

public class ShoppingCartFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShoppingCartAdapter adapter;
    private Button checkoutBtn;
    private TextView totalTxt;

    public static ShoppingCartFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        checkoutBtn = (Button)view.findViewById(R.id.checkoutButton);
        totalTxt = (TextView)view.findViewById(R.id.totalCartText);

        totalTxt.setText(getContext().getString(R.string.shoppingCartTotalText) + " $" + ShoppingCart.getInstance().getShoppingCartTotal());

        recyclerView = (RecyclerView) view.findViewById(R.id.shoppingcart_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ShoppingCartAdapter(getContext(), ShoppingCart.getInstance().shoppingCartArray);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShoppingCart.getInstance().getShoppingCartTotal() > 0) {
                    doCheckout();
                } else {
                    Toast.makeText(getContext(), R.string.cartEmptyText , Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void doCheckout() {
        if(User.getInstance().getRole() == 3) {
            Fragment newFragment = new CheckoutUserFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, newFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        } else if (User.getInstance().getRole() < 3 && User.getInstance().getRole() > 0)
        {
            Fragment newFragment = new CheckoutAdminFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, newFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        } else {
            Toast.makeText(getContext(), R.string.credentialsErrorText , Toast.LENGTH_SHORT).show();
        }
    }
}
