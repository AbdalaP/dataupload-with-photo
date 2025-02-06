package com.example.sch_agro.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    // creating constant keys for shared preferences.
    public static final String PREF_NAME = "UserSession";
    // key for storing email.
    public static final String EMAIL_KEY = "email_key";
    // key for storing password.
    public static final String Is_LOGGED_IN = "IsLoggedIn";
    public static final String KEY_USER_ID = "UserId";
    // key for storing password.
    public static final String PASSWORD_KEY = "password_key";
   private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private Context context;


    public Session(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public  void createLoginSession(String userId){
        editor.putBoolean(Is_LOGGED_IN,true);
        editor.putString(KEY_USER_ID,userId);
        editor.apply();
    }
    public boolean isLoggedIn(){
      return sharedPreferences.getBoolean(Is_LOGGED_IN,false);
    }
    public  String getKeyUserId(){
        return  sharedPreferences.getString(KEY_USER_ID,null);
    }
    public  void logoutUser(){
        editor.clear();
        editor.apply();
    }

}


