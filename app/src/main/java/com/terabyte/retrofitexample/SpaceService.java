package com.terabyte.retrofitexample;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpaceService {
    @GET(value = "planetary/apod")
    //@Query("api_key") there is api_key word is from nasa api. It's certain query word from http request of Nasa api.
    Call<SpaceResponse> getSpaceInfo(@Query("api_key") String apiKey);
}
