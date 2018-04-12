package edu.brian.bluefletch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private SharedPreferenceConfig preferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(this), CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(cookieManager);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        if (preferenceConfig.readLoginStatus()){
            startActivity(new Intent(this,FeedActivity.class));
            finish();
        }


    }

    public void login(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        EditText text_name = findViewById(R.id.user_name);
        EditText text_password = findViewById(R.id.user_password);
        String user_name = text_name.getText().toString();
        String user_password = text_password.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("username", user_name);
        params.put("password", user_password);

        JSONObject parameters = new JSONObject(params);

        String url = "https://bfsharingapp.bluefletch.com/login";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                parameters,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject user = response;
                goToFeed(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginError();
            }
        });


        Volley.newRequestQueue(this).add(jsonRequest);
    }

    public void goToFeed(JSONObject user){
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
        preferenceConfig.writeUserInfo(user);
        preferenceConfig.writeLoginStatus(true);
        finish();
    }

    public void loginError(){
        Toast.makeText(this, "Login Failed - Try again",Toast.LENGTH_SHORT).show();
    }
}