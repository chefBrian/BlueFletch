package edu.brian.bluefletch.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brian.bluefletch.adapters.RecyclerViewAdapter;
import edu.brian.bluefletch.models.Post;
import edu.brian.bluefletch.R;
import edu.brian.bluefletch.config.SharedPreferenceConfig;

public class FeedActivity extends AppCompatActivity {

    private SharedPreferenceConfig preferenceConfig;
    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        recyclerView = findViewById(R.id.recyclerView);
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
        // CLEAR POST TEXT
        TextView text_post_text = findViewById(R.id.postText);
        text_post_text.setText(null);

        // URL
        String url = "https://bfsharingapp.bluefletch.com/feed";

        // JSON REQUEST
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
        postList = new ArrayList<>();
        for (int i = 0; i < feed.length(); i++) {
            try{
                //PARSE JSON
                JSONObject feed_post = feed.getJSONObject(i);
                String post_text = feed_post.getString("postText");
                String createdDate = feed_post.getString("createdDate");

                JSONObject postUser = feed_post.getJSONObject("postUser");
                String username = postUser.getString("username");
                String imageUrl = postUser.getString("imageUrl");
                String finalImage = "https://bfsharingapp.bluefletch.com" + imageUrl;

                // TODO ADD COMMENTS
                //JSONArray comments = post.getJSONArray("comments");

                // CREATE POST OBJECT
                Post post = new Post(post_text, createdDate, username, finalImage);
                postList.add(post);


            }catch(Exception e){
                Toast.makeText(this, "Error creating recycler view",Toast.LENGTH_SHORT).show();
            }
        }
        setRvadapter(postList);
    }

    public void setRvadapter (List<Post> postl) {
        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(this,postl);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myadapter);
    }

    public void post(View view){
        // DISMISS KEYBOARD
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        // GET USERNAME
        TextView text_username = findViewById(R.id.feedText);
        String username = text_username.getText().toString();

        // GET POST TEXT
        EditText text_postText = findViewById(R.id.postText);
        String post_text = text_postText.getText().toString();

        // MAKE SURE POST HAS TEXT
        if (!post_text.isEmpty()){
            // BUILD JSON
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("postText", post_text);

            JSONObject parameters = new JSONObject(params);

            // URL
            String url = "https://bfsharingapp.bluefletch.com/post";

            // JSON REQUEST
            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    parameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            getFeed();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    postError();
                }
            });

            Volley.newRequestQueue(this).add(jsonRequest);
        }
        else{
            Toast.makeText(this, "Please provide text for your post",Toast.LENGTH_SHORT).show();
        }
    }

    public void postError(){
        // MAKE TOAST FOR POST ERROR
            Toast.makeText(this, "Post Failed - Try again later",Toast.LENGTH_SHORT).show();
    }
}
