package com.qixiu.lejia.core.shop;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qixiu.adapter.RecyclerPagerAdapter;
import com.qixiu.lejia.R;

import java.util.List;

/**
 * Created by Long on 2018/5/31
 */
class ShopImageAdapter extends RecyclerPagerAdapter<ShopImageAdapter.ImageViewHolder> {

    private List<String> mImages;

    ShopImageAdapter(List<String> images) {
        this.mImages = images;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        return new ImageViewHolder(inflater.inflate(R.layout.page_shop_image, container, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String image = mImages.get(position);
        /*ImageBindingAdapters.bindImage(holder.imageView, image, null,
                null, null);*/
        Glide.with(holder.imageView.getContext())
                .load(image)
                .crossFade()
                .into(holder.imageView);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    static class ImageViewHolder extends RecyclerPagerAdapter.ViewHolder {

        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
