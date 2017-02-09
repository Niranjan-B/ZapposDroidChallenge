package com.ninja.ilovezappos.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ninja.ilovezappos.mvp.presenters.ProductDisplayPresenter;

/**
 * Created by niranjanb on 07/02/17.
 */

public class ProductDisplayActivityRetainFragment extends Fragment {

    private ProductDisplayPresenter mProductDisplayActivityPresenter;
    private String mSearchTerm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void retainPresenter(ProductDisplayPresenter productDisplayPresenter) {
        mProductDisplayActivityPresenter = productDisplayPresenter;
    }

    public ProductDisplayPresenter getProductDisplayActivityPresenter() {
        return mProductDisplayActivityPresenter;
    }

    public void retainSearchTerm(String searchTerm) {
        mSearchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return mSearchTerm;
    }
}
