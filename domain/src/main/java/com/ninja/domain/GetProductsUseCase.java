package com.ninja.domain;

import com.ninja.data.entities.Product;
import com.ninja.data.repositories.ProductRepository;

import io.reactivex.Observable;
import io.reactivex.Scheduler;


/**
 * Created by niranjanb on 05/02/17.
 */

public class GetProductsUseCase extends UseCase<Product> {

    private ProductRepository mProductRepository;
    private Scheduler mExecutorThread, mMainThread;
    private String mSearchedParameter;

    public GetProductsUseCase(ProductRepository productRepository,
                              Scheduler executorThread,
                              Scheduler mainThread) {
        mProductRepository = productRepository;
        mExecutorThread = executorThread;
        mMainThread = mainThread;
    }

    public void passSearchParameter(String searchParam) {
        mSearchedParameter = searchParam;
    }

    @Override
    public Observable<Product> buildObservable() {
        return mProductRepository.getSearchedProducts(mSearchedParameter)
                .subscribeOn(mExecutorThread)
                .observeOn(mMainThread);
    }
}
