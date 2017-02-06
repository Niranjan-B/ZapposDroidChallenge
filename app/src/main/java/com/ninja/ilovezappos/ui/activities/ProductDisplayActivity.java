package com.ninja.ilovezappos.ui.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ninja.data.entities.Product;
import com.ninja.ilovezappos.R;
import com.ninja.ilovezappos.Utils;
import com.ninja.ilovezappos.mvp.presenters.ProductDisplayPresenter;
import com.ninja.ilovezappos.mvp.views.ProductDisplayView;

import java.util.List;

public class ProductDisplayActivity extends AppCompatActivity implements ProductDisplayView {

    ProductDisplayPresenter mProductDisplayPresenter;

    EditText mSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBar = (EditText) findViewById(R.id.edit_text_toolbar_mainactivity);

        initToolbar();
        initPresenter();
        initSearchBar();
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
        TextView mToolbarTitle = (TextView) findViewById(R.id.toolbar_activity_main_text_view);

        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(getResources().getString(R.string.app_name));
    }

    private void initPresenter() {
        mProductDisplayPresenter = new ProductDisplayPresenter();
        mProductDisplayPresenter.attachView(this);
    }

    private void initSearchBar() {
        mSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (!mSearchBar.getText().toString().isEmpty()) {
                        // hiding keyboard after the text is entered
                        Utils.hideKeyBoard(ProductDisplayActivity.this);
                        mProductDisplayPresenter.setSearchParam(mSearchBar.getText().toString());
                        // checking for network connectivity
                        if (Utils.isNetworkAvailable(ProductDisplayActivity.this)) {
                            // calling onCreate method of presenter which creates the network connection
                            mProductDisplayPresenter.onCreate();
                        } else {
                            // showing internet connection failure screen
                            // presenter should call this
                            Utils.showToast(getContext(), "internet failed");
                        }
                    } else {
                        // presenter should call this
                        Utils.showToast(getContext(), "Please enter a product!");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void showSearchResults(List<Product> productList) {

    }

    @Override
    public Context getContext() {
        return ProductDisplayActivity.this;
    }

    @Override
    public void internetTimeOutError() {

    }

    @Override
    public void internetUnavailableError() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }
}
