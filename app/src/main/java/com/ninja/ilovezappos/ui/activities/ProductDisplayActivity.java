package com.ninja.ilovezappos.ui.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ninja.data.entities.Result;
import com.ninja.ilovezappos.R;
import com.ninja.ilovezappos.Utils;
import com.ninja.ilovezappos.mvp.presenters.ProductDisplayPresenter;
import com.ninja.ilovezappos.mvp.views.ProductDisplayView;

import java.util.List;

public class ProductDisplayActivity extends AppCompatActivity implements ProductDisplayView {

    ProductDisplayPresenter mProductDisplayPresenter;

    private EditText mSearchBar;
    private RelativeLayout mNoSearchContainer, mNoInternetContainer;
    private Button mRetryButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBar = (EditText) findViewById(R.id.edit_text_toolbar_mainactivity);
        mNoInternetContainer = (RelativeLayout) findViewById(R.id.container_no_internet);
        mNoSearchContainer = (RelativeLayout) findViewById(R.id.container_no_search);
        mRetryButton = (Button) findViewById(R.id.button_retry_main_activity);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_main_activity);

        initUi();
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
                            mProductDisplayPresenter.displayNoInternetScreen();
                        }
                    } else {
                        // displaying empty search screen
                        mProductDisplayPresenter.displayEmptySearchToast();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void initUi() {
        initToolbar();

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductDisplayPresenter.onCreate();
            }
        });
    }

    @Override
    public void showSearchResults(List<Result> productList) {
        Log.d("ninja", productList.size() + "");
    }

    @Override
    public void showEmptySearchToast(String message) {
        Utils.showToast(getContext(), message);
    }

    @Override
    public void hideStateChangeViews() {
        if (mNoInternetContainer.getVisibility() == View.VISIBLE || mNoSearchContainer.getVisibility() == View.VISIBLE) {
            mNoInternetContainer.setVisibility(View.GONE);
            mNoSearchContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void displayNoSearchResultContainer() {
        if (!(mNoSearchContainer.getVisibility() == View.VISIBLE)) {
            mNoSearchContainer.setVisibility(View.VISIBLE);
        }
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
        if (! (mNoInternetContainer.getVisibility() == View.VISIBLE)) {
            mNoInternetContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress() {
        if (mProgressBar.getVisibility() == View.GONE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
