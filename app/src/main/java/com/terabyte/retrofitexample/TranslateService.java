package com.terabyte.retrofitexample;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TranslateService {
    //Maria Sokolskayas' key for microsoft azure translator
    String api_key = "891c1008d9a84e9c90fbe6e59fd1d62b";

    //we can add a body to request only in POST request type
    @POST("/translate?api-version=3.0")
    //there is a string array in params of annotation
    @Headers({
            "Ocp-Apim-Subscription-Key: "+api_key,
            "Content-Type: application/json; charset=UTF-8",
            "Ocp-Apim-Subscription-Region: westeurope"
    })
    Call<TranslateResponse[]> getTranslate(@Query("to") String to, @Body TranslateBody[] translateBody);
}
