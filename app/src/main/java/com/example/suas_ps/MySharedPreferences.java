package com.example.suas_ps;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class MySharedPreferences {

    private Context context;
    private SharedPreferences mySharedPreferences;

    private int session;
    private int refreshSession;
    private boolean isLoggedIn;

    public MySharedPreferences(Context context) {
        this.context = context;
        this.mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getSession() {
        return mySharedPreferences.getInt("session",0);
    }

    public void setSession(int session) {
        mySharedPreferences.edit().putInt("session",session).apply();
    }

    public void clearSession(){
        mySharedPreferences.edit().remove("session").apply();
    }

    public int getRefreshSession() {
        return mySharedPreferences.getInt("refreshSession",0);
    }

    public void setRefreshSession(int refreshSession) {
        mySharedPreferences.edit().putInt("refreshSession",refreshSession).apply();
    }

    public void clearRefreshSession(){
        mySharedPreferences.edit().remove("refreshSession").apply();
    }

    public boolean isLoggedIn() {
        return mySharedPreferences.getBoolean("isLoggedIn",false);
    }

    public void setLoggedIn(boolean loggedIn) {
        mySharedPreferences.edit().putBoolean("isLoggedIn",loggedIn).apply();
    }

    public void clearLoggedIn(){
        mySharedPreferences.edit().remove("isLoggedIn").apply();
    }
}
