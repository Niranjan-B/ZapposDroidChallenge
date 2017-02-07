package com.ninja.ilovezappos.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ninja.ilovezappos.utils.ProductCardClickListener;
import com.ninja.ilovezappos.utils.ProductDisplayActivityRetainFragment;
import com.ninja.ilovezappos.utils.Utils;
import com.ninja.ilovezappos.mvp.presenters.ProductDisplayPresenter;
import com.ninja.ilovezappos.mvp.views.ProductDisplayView;
import com.ninja.ilovezappos.ui.adapters.ProductDisplayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductDisplayActivity extends AppCompatActivity implements ProductDisplayView,
        ProductCardClickListener {

    ProductDisplayPresenter mProductDisplayPresenter;

    private EditText mSearchBar;
    private RelativeLayout mNoSearchContainer, mNoInternetContainer;
    private Button mRetryButton;
    private ProgressBar mProgressBar;
    private RecyclerView mProductDisplayList;
    private ProductDisplayAdapter mProductDisplayAdapter;
    private FloatingActionButton mCartButton;

    private static final String SHARE_URI = "https://ninja-scf.usc.edu/?product_id=";
    private static final String RETAIN_FRAGMENT_TAG = "retain_fragment";

    private ProductDisplayActivityRetainFragment mRetainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBar = (EditText) findViewById(R.id.edit_text_toolbar_mainactivity);
        mNoInternetContainer = (RelativeLayout) findViewById(R.id.container_no_internet);
        mNoSearchContainer = (RelativeLayout) findViewById(R.id.container_no_search);
        mRetryButton = (Button) findViewById(R.id.button_retry_main_activity);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_main_activity);
        mProductDisplayList = (RecyclerView) findViewById(R.id.recyclerview_products_main_activity);
        mCartButton = (FloatingActionButton) findViewById(R.id.fab_cart_main_activity);

        initUi();
        initRetainFragment();
        initPresenter();
        initSearchBar();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Uri data = this.getIntent().getData();

        if (data != null && data.isHierarchical() && getContext() != null) {
            if (data.getQueryParameter("product_id") != null) {
                // launch the activity with the product
                mProductDisplayPresenter.setSearchParam(data.getQueryParameter("product_id"));
                mProductDisplayPresenter.onCreate();
            }
        }
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_main);
        TextView mToolbarTitle = (TextView) findViewById(R.id.toolbar_activity_main_text_view);

        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(getResources().getString(R.string.app_name));
    }

    private void initRetainFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        mRetainFragment = (ProductDisplayActivityRetainFragment) fragmentManager.findFragmentByTag(RETAIN_FRAGMENT_TAG);
        if (mRetainFragment == null) {
            mRetainFragment = new ProductDisplayActivityRetainFragment();
            fragmentManager.beginTransaction().add(mRetainFragment, RETAIN_FRAGMENT_TAG).commit();
        }
    }

    private void initPresenter() {
        mProductDisplayPresenter = mRetainFragment.getProductDisplayActivityPresenter();
        mRetainFragment.retainPresenter(null);
        if (mProductDisplayPresenter == null) {
            mProductDisplayPresenter = new ProductDisplayPresenter();
        }
        mProductDisplayPresenter.attachView(this);

        // checking if the search term is not null
        if (mRetainFragment.getSearchTerm() != null) {
            fetchSearchedProduct(mRetainFragment.getSearchTerm());
        }
    }

    private void initSearchBar() {
        mSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (!mSearchBar.getText().toString().isEmpty()) {
                        // hiding keyboard after the text is entered
                        Utils.hideKeyBoard(ProductDisplayActivity.this);

                        // making network request to fetch all products
                        fetchSearchedProduct(mSearchBar.getText().toString());

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

    private void fetchSearchedProduct(String term) {

        // caching the search term
        mRetainFragment.retainSearchTerm(term);
        mProductDisplayPresenter.setSearchParam(term);

        // checking for network connectivity
        if (Utils.isNetworkAvailable(ProductDisplayActivity.this)) {
            // calling onCreate method of presenter which creates the network connection
            mProductDisplayPresenter.onCreate();
        } else {
            // showing internet connection failure screen
            // presenter should call this
            mProductDisplayPresenter.displayNoInternetScreen();
        }
    }

    private void initUi() {
        initToolbar();
        initRecyclerView();

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductDisplayPresenter.onCreate();
            }
        });
    }

    private void initRecyclerView() {
        mProductDisplayAdapter = new ProductDisplayAdapter(getContext(), new ArrayList<Result>(), this);

        mProductDisplayList.setHasFixedSize(true);
        mProductDisplayList.setLayoutManager(new LinearLayoutManager(getContext()));
        mProductDisplayList.setItemAnimator(new DefaultItemAnimator());
        mProductDisplayList.setAdapter(mProductDisplayAdapter);
    }

    @Override
    public void showSearchResults(List<Result> productList) {
        Log.d("ninja", productList.size() + "");
        mProductDisplayAdapter.addProducts(new ArrayList<>(productList));

    }

    @Override
    public void showEmptySearchToast(String message) {
        Utils.showToast(getContext(), message);
    }

    @Override
    public void hideStateChangeViews() {
        if (mNoInternetContainer.getVisibility() == View.VISIBLE || mNoSearchContainer.getVisibility() == View.VISIBLE
                || mProductDisplayList.getVisibility() == View.VISIBLE) {
            mNoInternetContainer.setVisibility(View.GONE);
            mNoSearchContainer.setVisibility(View.GONE);
            mProductDisplayList.setVisibility(View.GONE);
        }
    }

    @Override
    public void displayNoSearchResultContainer() {
        if (!(mNoSearchContainer.getVisibility() == View.VISIBLE)) {
            mNoSearchContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayProductView() {
        if (mProductDisplayList.getVisibility() == View.GONE) {
            mProductDisplayList.setVisibility(View.VISIBLE);
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


    @Override
    public void shareProduct(String productId) {
        ShareCompat.IntentBuilder
                .from(this)
                .setText(SHARE_URI + productId)
                .setType("text/plain")
                .setChooserTitle("Share Via")
                .startChooser();
    }

    @Override
    public void getInfoAboutProduct(String productUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(productUrl));
        startActivity(intent);
    }

    @Override
    public void addToCart() {
        Log.d("ninja", "Added to cart!!!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isFinishing()) {
            mRetainFragment.retainPresenter(mProductDisplayPresenter);
        }
        mProductDisplayPresenter.detachView();
        mProductDisplayPresenter = null;
    }
}
