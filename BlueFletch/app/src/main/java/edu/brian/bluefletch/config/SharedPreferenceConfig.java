package edu.brian.bluefletch.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.json.JSONObject;

import edu.brian.bluefletch.R;

public class SharedPreferenceConfig {

    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferenceConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference), Context.MODE_PRIVATE);

    }

    public void writeLoginStatus (boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status_preference), status);
        editor.commit();
    }

    public void writeUserInfo (JSONObject user){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.user_info_preference), user.toString());
        editor.commit();
    }

    public boolean readLoginStatus(){
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_preference), false);
        return status;
    }

    public String readUserInfo(){
        String user = "";
        user = sharedPreferences.getString(context.getResources().getString(R.string.user_info_preference), "");
        return user;
    }
}
