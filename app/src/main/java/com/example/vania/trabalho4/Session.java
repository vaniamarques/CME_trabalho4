package com.example.vania.trabalho4;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setIdUser(Integer id) {
        prefs.edit().putInt("_idUser", id).commit();
    }

    public Integer getIdUser() {
        Integer id = prefs.getInt("_idUser",0);
        return id;
    }
}