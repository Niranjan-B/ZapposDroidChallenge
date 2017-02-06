package com.ninja.ilovezappos.mvp.presenters;

import android.util.Log;

import com.ninja.data.entities.Product;
import com.ninja.data.rest.RestDataSource;
import com.ninja.domain.GetProductsUseCase;
import com.ninja.ilovezappos.mvp.views.ProductDisplayView;
import com.ninja.ilovezappos.mvp.views.base.View;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by niranjanb on 05/02/17.
 */

public class ProductDisplayPresenter implements Presenter {

    private GetProductsUseCase mGetProductsUseCase;
    private ProductDisplayView mProductDisplayView;
    private Disposable mDisposable;

    public ProductDisplayPresenter() {
        mGetProductsUseCase = new GetProductsUseCase(new RestDataSource(), Schedulers.newThread(),
                AndroidSchedulers.mainThread());
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onCreate() {
        fetchProducts();
    }

    @Override
    public void onPause() {
        mDisposable.dispose();
    }

    @Override
    public void attachView(View view) {
        mProductDisplayView = (ProductDisplayView) view;
    }

    private void fetchProducts() {
        mGetProductsUseCase.execute().subscribe(new Observer<Product>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(Product value) {
                Log.d("ninja", value.getResults().toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ninja", e + "");
            }

            @Override
            public void onComplete() {
                Log.d("ninja", "completed!!");
            }
        });
    }

    public void setSearchParam(String searchParam) {
        mGetProductsUseCase.passSearchParameter(searchParam);
    }

}
