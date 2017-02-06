package com.ninja.data.rest;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.ninja.data.entities.Product;
import com.ninja.data.repositories.ProductRepository;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.Observable;

/**
 * Created by niranjanb on 05/02/17.
 */

public class RestDataSource implements ProductRepository {

    private static final String URL = "https://api.zappos.com/";
    private EndPoints mEndPoints;
    private Map<String, String> queryMap;

    public RestDataSource() {

        Retrofit retrofitInstance = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mEndPoints = retrofitInstance.create(EndPoints.class);
        queryMap = new HashMap<>();
    }

    @Override
    public Observable<Product> getSearchedProducts(String product) {
        queryMap.put("term", product);
        queryMap.put("key", "b743e26728e16b81da139182bb2094357c31d331");
        return mEndPoints.getSearchResults(queryMap);
    }
}
