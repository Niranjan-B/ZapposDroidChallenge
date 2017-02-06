package com.ninja.data.rest;

import com.ninja.data.entities.Product;

import java.util.Map;

import retrofit2.http.GET;
import io.reactivex.Observable;
import retrofit2.http.QueryMap;

/**
 * Created by niranjanb on 05/02/17.
 */

public interface EndPoints {

    @GET("Search")
    Observable<Product> getSearchResults(@QueryMap Map<String, String> queryParams);

}
