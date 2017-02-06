package com.ninja.ilovezappos.utils;

/**
 * Created by niranjanb on 06/02/17.
 */

public interface ProductCardClickListener {
    void shareProduct(String productId);
    void getInfoAboutProduct(String productUrl);
    void addToCart();
}
