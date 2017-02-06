package com.ninja.ilovezappos.mvp.views;

import com.ninja.data.entities.Result;
import com.ninja.ilovezappos.mvp.views.base.BaseView;
import com.ninja.ilovezappos.mvp.views.base.InternetView;
import com.ninja.ilovezappos.mvp.views.base.ProgressableView;
import com.ninja.ilovezappos.mvp.views.base.View;

import java.util.List;

/**
 * Created by niranjanb on 05/02/17.
 */

public interface ProductDisplayView extends View, BaseView, ProgressableView, InternetView {
    void showSearchResults(List<Result> productList);
    void showEmptySearchToast(String message);
    void hideStateChangeViews();
    void displayNoSearchResultContainer();
}
