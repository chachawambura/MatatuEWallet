package com.example.mataturoutesystem.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface iGoogleApi {
    @GET
    Call<String> getDataFromGoogleApi(@Url String url);
}
