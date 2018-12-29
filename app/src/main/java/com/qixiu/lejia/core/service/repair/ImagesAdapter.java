package com.qixiu.lejia.core.service.repair;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.ImageInfo;
import com.qixiu.lejia.utils.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2018/5/5
 */
class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ImagesAdapter";

    static final         int ADD   = R.layout.item_add_image;
    private static final int IMAGE = R.layout.item_square_image;

    private List<ImageInfo> mImages = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    //是否可编辑
    private boolean editable;

    ImagesAdapter(List<ImageInfo> images, boolean editable) {
        this.editable = editable;
        if (images != null) {
            mImages.addAll(images);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!editable) {
            return IMAGE;
        }

        int size = mImages.size();
        if (size < 9) {
            if (position == getItemCount() - 1) {
                return ADD;
            }
        }
        return IMAGE;
    }

    @Override
    public int getItemCount() {
        if (!editable) {
            return mImages.size();
        }
        if (mImages.size() < 9) {
            return mImages.size() + 1;
        }
        return mImages.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ADD) {
            return new AddViewHolder(inflater.inflate(ADD, parent, false));
        }
        return new ItemViewHolder(inflater.inflate(IMAGE, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final int viewType = holder.getItemViewType();
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(viewType, holder.getAdapterPosition());
            }
        });

        if (viewType == IMAGE) {
            ImageInfo imageInfo = mImages.get(holder.getAdapterPosition());
            ItemViewHolder imageHolder = (ItemViewHolder) holder;
            if (editable) {
                //在编辑模式下设置删除按钮点击事件
                imageHolder.del.setOnClickListener(view -> {
                    del(holder.getAdapterPosition());
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemDel(holder.getAdapterPosition());
                    }
                });
            }
            //删除按钮只有在编辑模式下显示
            imageHolder.del.setVisibility(editable ? View.VISIBLE : View.GONE);
            //load image
            Glide.with(imageHolder.view.getContext())
                    .load(imageInfo.getSrc())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageHolder.view);

            //set transition name
            imageHolder.view.setTransitionName(String.valueOf(position));

        }
    }

    void onImageSelected(List<Uri> uris) {
        ArrayList<ImageInfo> list = Lists.newArrayList();
        for (Uri uri : uris) {
            list.add(new ImageInfo(uri.toString()));
        }
        int originSize = mImages.size();
        mImages.addAll(list);
        if (mImages.size() == 9) {
            notifyItemRemoved(originSize);
        }
        notifyItemRangeInserted(originSize, list.size());
    }

    public void del(int position) {
        mImages.remove(position);
        notifyItemRemoved(position);
    }

    public void add(List<ImageInfo> list) {
        int originSize = mImages.size();
        mImages.addAll(list);
        notifyItemRangeInserted(originSize, list.size());
    }

    public int getImageSize() {
        return mImages.size();
    }

    public ImageInfo getItem(int position) {
        return mImages.get(position);
    }

    public List<String> getUris() {
        List<String> uris = Lists.newArrayList();
        for (ImageInfo image : mImages) {
            uris.add(image.getSrc());
        }
        return uris;
    }

    public List<String> getImages() {
        List<String> list = new ArrayList<>();
        for (ImageInfo image : mImages) {
            list.add(image.getSrc());
        }
        return list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    interface OnItemClickListener {
        void onItemClick(int viewType, int position);

        void onItemDel(int position);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView   view;
        ImageButton del;

        ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.image);
            del = itemView.findViewById(R.id.del);
        }
    }

    private static class AddViewHolder extends RecyclerView.ViewHolder {

        AddViewHolder(View itemView) {
            super(itemView);
        }
    }

}
