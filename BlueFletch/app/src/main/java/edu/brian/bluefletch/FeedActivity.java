package edu.brian.bluefletch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private SharedPreferenceConfig preferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        getUserInfo();
        getFeed();
    }

    public void logout(View view) {
        preferenceConfig.writeLoginStatus(false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void getUserInfo(){
        try{
            // READ USER INFO
            String user_info = preferenceConfig.readUserInfo();
            JSONObject user_json = new JSONObject(user_info);

            // SET USERNAME WELCOME
            String username = user_json.getString("username");
            TextView textView = findViewById(R.id.feedText);
            textView.setText(username);

            // SET USER PICTURE
            String image_url = user_json.getString("imageUrl");
            ImageView imageView = findViewById(R.id.user_picture);
            String url = "https://bfsharingapp.bluefletch.com" + image_url;
            Picasso.with(this).load(url).into(imageView);
        }
        catch (Exception e){
            // DO SOMETHING WITH ERROR
        }
    }

    public void getFeed(){
        String url = "https://bfsharingapp.bluefletch.com/feed";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        setFeed(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logout(null);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("withCredentials","true");
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonRequest);

    }

    public void setFeed(JSONArray feed){
        JSONArray ok = feed;





    }
}
