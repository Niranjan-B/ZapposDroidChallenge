package com.ninja.ilovezappos.ui.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ninja.data.entities.Result;
import com.ninja.ilovezappos.BR;
import com.ninja.ilovezappos.R;
import com.ninja.ilovezappos.utils.ProductCardClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by niranjanb on 06/02/17.
 */

public class ProductDisplayAdapter extends RecyclerView.Adapter<ProductDisplayAdapter.ViewHolder> {

    ArrayList<Result> mProducts;
    Context mContext;
    ProductCardClickListener mProductClickListener;

    public ProductDisplayAdapter(Context context, ArrayList<Result> products,
                                 ProductCardClickListener productCardClickListener) {
        mContext = context;
        mProducts = products;
        mProductClickListener = productCardClickListener;
    }

    public void addProducts(ArrayList<Result> moreProducts) {
        if (!mProducts.isEmpty()) {
            mProducts.clear();
        }
        mProducts.add(moreProducts.get(0));
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.card_product
                , parent, false);
        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Result product = mProducts.get(position);
        ViewDataBinding viewDataBinding = holder.getDataBinding();
        viewDataBinding.setVariable(BR.result, product);
        setClickListeners(holder, product);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding mBinding;
        ImageButton mShare, mInfo;
        Button mAddToCart;

        public ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            mBinding = viewDataBinding;

            mShare = (ImageButton) viewDataBinding.getRoot().findViewById(R.id.button_share_product_card);
            mInfo = (ImageButton) viewDataBinding.getRoot().findViewById(R.id.button_info_product_card);
            mAddToCart = (Button) viewDataBinding.getRoot().findViewById(R.id.button_add_product_card);
        }

        public ViewDataBinding getDataBinding() {
            return mBinding;
        }
    }

    private void setClickListeners(ViewHolder holder, final Result result) {
        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductClickListener.shareProduct(result.getProductId());
            }
        });
        holder.mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductClickListener.getInfoAboutProduct(result.getProductUrl());
            }
        });
        holder.mAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductClickListener.addToCart();
            }
        });
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext()).load(url).fit().placeholder(R.mipmap.no_search)
                .into(imageView);
    }
}
