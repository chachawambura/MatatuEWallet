package com.example.mataturoutesystem;

import com.example.mataturoutesystem.Remote.RetrofitClient;
import com.example.mataturoutesystem.Remote.iGoogleApi;

public class Common {
    public static final String baseURl = "https://googleapis.com";

    public static iGoogleApi getGoogleApi(){
        return RetrofitClient.getClient(baseURl).create(iGoogleApi.class);
    }
}
