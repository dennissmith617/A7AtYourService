package com.example.a7atyourservice.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IPlaceholder {

    // Get requests just need getter on Models
    @GET("activity")
    Call<PostModel>  getActivityModels();
}
