package com.ninja.ilovezappos.mvp.presenters;


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
        mProductDisplayView.showProgress();

        mGetProductsUseCase.execute().subscribe(new Observer<Product>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(Product value) {
                if (!value.getStatusCode().contains("200")) {
                    // if the status is not OK, display no internet container
                    mProductDisplayView.internetUnavailableError();
                } else if (value.getResults().size() == 0) {
                    // if results are empty, display no search container
                    hideStateChangeViews();
                    mProductDisplayView.displayNoSearchResultContainer();
                    mProductDisplayView.showEmptySearchToast("Please refine your search");
                } else {
                    // if the results are got successfully, pass them on the activity to display and hide state change views
                    hideStateChangeViews();
                    mProductDisplayView.displayProductView();
                    mProductDisplayView.showSearchResults(value.getResults());
                }

            }

            @Override
            public void onError(Throwable e) {
                // show no internet container here
                mProductDisplayView.hideProgress();
                hideStateChangeViews();
                mProductDisplayView.internetUnavailableError();
            }

            @Override
            public void onComplete() {
                mProductDisplayView.hideProgress();
            }
        });
    }

    public void setSearchParam(String searchParam) {
        mGetProductsUseCase.passSearchParameter(searchParam);
    }

    public void displayNoInternetScreen() {
        hideStateChangeViews();
        mProductDisplayView.internetUnavailableError();
    }

    public void displayEmptySearchToast() {
        mProductDisplayView.showEmptySearchToast("Please enter a product!");
    }

    private void hideStateChangeViews() {
        mProductDisplayView.hideStateChangeViews();
    }

}
