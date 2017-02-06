package com.ninja.data.repositories;

import com.ninja.data.entities.Product;

import io.reactivex.Observable;

/**
 * Created by niranjanb on 05/02/17.
 */

public interface ProductRepository {
    Observable<Product> getSearchedProducts(String product);
}
