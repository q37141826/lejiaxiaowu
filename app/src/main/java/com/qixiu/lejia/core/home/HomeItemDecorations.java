package com.qixiu.lejia.core.home;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qixiu.adapter.ItemDecorations;

/**
 * Created by Long on 2018/4/23
 */
public class HomeItemDecorations {

    /**
     * 首页门店展示列表条目分割线
     *
     * @return ItemDecorationFactory
     */
    public static ItemDecorations.ItemDecorationFactory shopsDivider(int space) {
        return view -> new ShopsDivider(view.getContext(), space);
    }

    private static class ShopsDivider extends RecyclerView.ItemDecoration {

        private int space;

        ShopsDivider(Context context, int space) {
            this.space = (int) (context.getResources().getDisplayMetrics().density * space);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
        }
    }

}
