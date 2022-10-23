package com.example.a7atyourservice.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IPlaceholder {

    @GET("email")
    Call<PostModel>  getEmailsWithQuery(@Query("email") String email);

}
