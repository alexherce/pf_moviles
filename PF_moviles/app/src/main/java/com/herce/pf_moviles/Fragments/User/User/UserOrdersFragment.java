package com.herce.pf_moviles.Fragments.User.User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.herce.pf_moviles.R;


public class UserOrdersFragment extends Fragment {
    public static UserOrdersFragment newInstance() {
        UserOrdersFragment fragment = new UserOrdersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_orders, container, false);
    }
}
