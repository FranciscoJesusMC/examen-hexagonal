package com.spring.examenhexagonal.infrastructure.retrofit;

import com.spring.examenhexagonal.infrastructure.response.ResponseSunat;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ClientSunatService {

    @GET("v2/sunat/ruc/full")
    Call<ResponseSunat> findSunat(@Header("Authorization")String token,
                                  @Query("numero")String numeroDocumento);
}
