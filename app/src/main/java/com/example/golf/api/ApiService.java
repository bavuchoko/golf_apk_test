package com.example.golf.api;

import com.example.golf.dto.Account;
import com.example.golf.dto.AccountResponse;
import com.example.golf.dto.PagedResponse;
import com.example.golf.dto.PracticeGame;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/user/authentication")
    Call<AccountResponse> login(@Body Account account);

    @GET("api/warmup")
    Call<PagedResponse<PracticeGame>> getPractices(
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("page") int page,
            @Query("size") int size,
            @Header("Authorization") String authorizationHeader);

    @GET("api/warmup")
    Call<PagedResponse<PracticeGame>> getPractices(
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("page") int page,
            @Query("size") int size);
}
