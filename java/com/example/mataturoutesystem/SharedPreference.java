package com.example.mataturoutesystem;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreference(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        this.sharedPreferences = sharedPreferences;
        this.editor = editor;
    }

    public SharedPreference(Context context){
        sharedPreferences = context.getSharedPreferences("Phone Number", 0);
        editor = sharedPreferences.edit();
    }

    public void storePhoneNumber(String phonenumeber){
        editor.putString("PhoneNumber", phonenumeber);
        editor.commit();
    }

    public String getPhoneNumber(){
        String phonenumber = sharedPreferences.getString("PhoneNumber", "");
        return phonenumber;
    }
}
