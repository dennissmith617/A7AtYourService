package com.example.a7atyourservice.model;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IPlaceholder {

    // Get requests just need getter on Models
    @GET("activity")
    Call<PostModel>  getActivityModels();
}
