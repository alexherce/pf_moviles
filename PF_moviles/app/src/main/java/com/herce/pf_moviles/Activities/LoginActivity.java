package com.herce.pf_moviles.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herce.pf_moviles.Activities.Admin.MainActivityAdmin;
import com.herce.pf_moviles.Activities.User.MainActivityUser;
import com.herce.pf_moviles.Objects.User;
import com.herce.pf_moviles.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.herce.pf_moviles.Services.Services.LOGIN_API;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn, signupBtn;
    EditText emailTxt, passwordTxt;
    ProgressDialog progress_bar;
    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.green);

        progress_bar = new ProgressDialog(LoginActivity.this);

        loginBtn = (Button)findViewById(R.id.loginButton);
        signupBtn = (Button)findViewById(R.id.signupButtonLogin);
        emailTxt = (EditText)findViewById(R.id.emailTextField);
        passwordTxt = (EditText)findViewById(R.id.passwordTextField);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent actividad = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(actividad);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login()
    {
        progress_bar.setMessage(LoginActivity.this.getString(R.string.logginginText));
        progress_bar.setCancelable(false);
        progress_bar.show();

        StringRequest loginReq = new StringRequest(Request.Method.POST, LOGIN_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_bar.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("01"))
                            {
                                JSONObject userInfo = new JSONObject(res.getString("user_data"));
                                Toast.makeText(LoginActivity.this, R.string.welcomeText , Toast.LENGTH_SHORT).show();
                                userData.getInstance().setUserID(userInfo.getInt("id"));
                                userData.getInstance().setRole(userInfo.getInt("rol"));
                                userData.getInstance().setEmail(userInfo.getString("email"));
                                userData.getInstance().setFirstName(userInfo.getString("first_name"));
                                userData.getInstance().setLastName(userInfo.getString("last_name"));
                                userData.getInstance().setAddress(userInfo.getString("address"));
                                userData.getInstance().setZipCode(userInfo.getString("zip_code"));
                                userData.getInstance().setState(userInfo.getString("state"));

                                if(userData.getInstance().getRole() == 1) {
                                    Intent actividad = new Intent(LoginActivity.this, MainActivityAdmin.class);
                                    startActivity(actividad);
                                } else if (userData.getInstance().getRole() == 2) {

                                }else {
                                    Intent actividad = new Intent(LoginActivity.this, MainActivityUser.class);
                                    startActivity(actividad);
                                }
                            } else if (res.getString("code").equals("02"))
                            {
                                Toast.makeText(LoginActivity.this, R.string.missingValuesText , Toast.LENGTH_SHORT).show();
                            } else if (res.getString("code").equals("03"))
                            {
                                Toast.makeText(LoginActivity.this, R.string.incorrectCredentialsText , Toast.LENGTH_SHORT).show();
                            } else if (res.getString("code").equals("04"))
                            {
                                Toast.makeText(LoginActivity.this, R.string.queryErrorText , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, R.string.unknownResponseText , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error! " + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar.cancel();
                        Toast.makeText(LoginActivity.this, R.string.commsErrorText + " " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("email",emailTxt.getText().toString());
                map.put("password",passwordTxt.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(loginReq);
    }
}
