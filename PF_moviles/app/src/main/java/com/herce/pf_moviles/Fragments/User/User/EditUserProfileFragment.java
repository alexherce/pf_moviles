package com.herce.pf_moviles.Fragments.User.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Activities.LoginActivity;
import com.herce.pf_moviles.Activities.SignupActivity;
import com.herce.pf_moviles.Objects.ShoppingCart;
import com.herce.pf_moviles.Objects.User;
import com.herce.pf_moviles.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.herce.pf_moviles.Services.Services.SIGNUP_API;
import static com.herce.pf_moviles.Services.Services.USER_PROFILE;

public class EditUserProfileFragment extends Fragment {

    ProgressDialog progress_bar;
    EditText firstnameField, lastnameField, emailField, addressField, stateField, zipcodeField;
    Button editProfileDoBtn;

    public static EditUserProfileFragment newInstance() {
        EditUserProfileFragment fragment = new EditUserProfileFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);

        firstnameField = (EditText) view.findViewById(R.id.firstNameEditProfileTextField);
        lastnameField  = (EditText) view.findViewById(R.id.lastnameEditProfileTextField);
        emailField  = (EditText) view.findViewById(R.id.emailEditProfileTextField);
        addressField = (EditText) view.findViewById(R.id.addressEditProfileTextField);
        zipcodeField  = (EditText) view.findViewById(R.id.postalcodeEditProfileTextField);
        stateField = (EditText) view.findViewById(R.id.stateEditProfileTextField);
        editProfileDoBtn = (Button)view.findViewById(R.id.editProfileDoButton);

        editProfileDoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdate();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void doUpdate() {

        progress_bar = new ProgressDialog(getContext());
        progress_bar.setMessage(getContext().getString(R.string.loadingDataText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        StringRequest editProfileReq = new StringRequest(Request.Method.POST, USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01"))
                            {

                                User.getInstance().setFirstName(firstnameField.getText().toString());
                                User.getInstance().setLastName(lastnameField.getText().toString());
                                User.getInstance().setEmail(emailField.getText().toString());
                                User.getInstance().setAddress(addressField.getText().toString());
                                User.getInstance().setZipCode(zipcodeField.getText().toString());
                                User.getInstance().setState(stateField.getText().toString());

                                Toast.makeText(getContext(), R.string.profileEditedSuccessText , Toast.LENGTH_SHORT).show();

                                Fragment newFragment = new UserProfileFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();

                            } else if (res.getString("code").equals("02"))
                            {
                                Toast.makeText(getContext(), R.string.missingValuesText , Toast.LENGTH_SHORT).show();
                            } else if (res.getString("code").equals("04"))
                            {
                                Toast.makeText(getContext(), R.string.queryErrorText , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), R.string.unknownResponseText , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error! " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.cancel();
                        Toast.makeText(getContext(), R.string.commsErrorText + " " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id", "" + User.getInstance().getUserID());
                params.put("first_name",firstnameField.getText().toString());
                params.put("last_name",lastnameField.getText().toString());
                params.put("email",emailField.getText().toString());
                params.put("address",addressField.getText().toString());
                params.put("zip_code",zipcodeField.getText().toString());
                params.put("state",stateField.getText().toString());

                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(editProfileReq);
    }

}
