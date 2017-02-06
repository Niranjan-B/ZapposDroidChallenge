package com.ninja.ilovezappos.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ninja.data.entities.Result;
import com.ninja.ilovezappos.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by niranjanb on 06/02/17.
 */

public class ProductDisplayAdapter extends RecyclerView.Adapter<ProductDisplayAdapter.ViewHolder> {

    ArrayList<Result> mProducts;
    Context mContext;

    public ProductDisplayAdapter(Context context, ArrayList<Result> products) {
        mContext = context;
        mProducts = products;
    }

    public void addProducts(ArrayList<Result> moreProducts) {
        if (!mProducts.isEmpty()) {
            mProducts.clear();
        }
        mProducts.addAll(moreProducts);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Result product = mProducts.get(position);
        holder.mProductName.setText(product.getProductName());
        holder.mBrandName.setText(product.getBrandName());
        holder.mPrice.setText(product.getPrice());
        holder.mOriginalPrice.setText(product.getOriginalPrice());
        holder.mDiscount.setText(product.getPercentOff());
        Picasso.with(mContext).load(product.getThumbnailImageUrl()).fit().placeholder(R.mipmap.no_search)
                .into(holder.mProductImage);
        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "share being clicked on " + position, Toast.LENGTH_LONG).show();
            }
        });
        holder.mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "info being clicked on " + position, Toast.LENGTH_LONG).show();
            }
        });
        holder.mAddToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "add being clicked on " + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mProductName, mBrandName, mOriginalPrice, mPrice, mDiscount;
        ImageView mProductImage;
        ImageButton mShare;
        Button mAddToCard, mInfo;

        public ViewHolder(View itemView) {
            super(itemView);

            mProductName = (TextView) itemView.findViewById(R.id.textview_prodname_productcard);
            mBrandName = (TextView) itemView.findViewById(R.id.textview_manname_productcard);
            mPrice = (TextView) itemView.findViewById(R.id.textview_prodprice_productcard);
            mOriginalPrice = (TextView) itemView.findViewById(R.id.textview_discountprice_productcard);
            mDiscount = (TextView) itemView.findViewById(R.id.textView_discount_productcard);
            mProductImage = (ImageView) itemView.findViewById(R.id.imgview_product_productcard);
            mShare = (ImageButton) itemView.findViewById(R.id.button_share_product_card);
            mInfo = (Button) itemView.findViewById(R.id.button_info_product_card);
            mAddToCard = (Button) itemView.findViewById(R.id.button_add_product_card);

        }
    }
}
