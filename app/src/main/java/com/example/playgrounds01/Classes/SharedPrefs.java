package com.example.playgrounds01.Classes;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    SharedPreferences sp;

    // jméno souboru s sharedPreferences
    private final String nameSharePrefs = "basicOptions";

    // jména pro hodnoty uložené v souboru
    private final String nameRemember = "remember";

    public SharedPrefs(Context context){
        sp = context.getSharedPreferences(nameSharePrefs, Context.MODE_PRIVATE);
    }

    public boolean getRememberMe(){
        return sp.getBoolean(nameRemember, false);
    }

    public void putRememberMe(boolean value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(nameRemember, value);
        editor.apply();
    }
}
