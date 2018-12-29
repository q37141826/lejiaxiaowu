package com.qixiu.lejia.core.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.home.story.StoryBean;
import com.qixiu.widget.RoundedImageView;

class HomeStoryAdapter extends RecyclerBaseAdapter {
    @Override
    public int getLayoutId() {
        return R.layout.itme_story;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new HomeStoryHolder(itemView, context, this);
    }

    public class HomeStoryHolder extends RecyclerBaseHolder {
        RoundedImageView story_image;
        TextView title_story_text;
        TextView context_story_text;

        public HomeStoryHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            story_image = itemView.findViewById(R.id.story_image);
            title_story_text = itemView.findViewById(R.id.title_story_text);
            context_story_text = itemView.findViewById(R.id.context_story_text);
        }

        @Override
        public void bindHolder(int position) {
            if (mData instanceof StoryBean.ListBean) {
                StoryBean.ListBean bean = (StoryBean.ListBean) mData;
                Glide.with(mContext).load(bean.getUrl()).into(story_image);
                title_story_text.setText(bean.getTitle());
                context_story_text.setText(bean.getRead());
            }
        }
    }
}
