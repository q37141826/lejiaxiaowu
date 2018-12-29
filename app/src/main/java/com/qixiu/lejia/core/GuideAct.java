package com.qixiu.lejia.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseActivity;
import com.qixiu.lejia.prefs.Prefs;
import com.qixiu.lejia.prefs.PrefsKeys;
import com.qixiu.widget.indicator.CircleIndicator;

/**
 * Created by Long on 2018/5/10
 * <pre>
 *     引导页
 * </pre>
 */
public class GuideAct extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, GuideAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Prefs.put(PrefsKeys.KEY_FIRST_RUN, false);
        setContentView(R.layout.act_guide);
        View startHome = findViewById(R.id.start_home);
        startHome.setOnClickListener(v -> {
            //去首页
            MainActivity.start(this);
        });

        CircleIndicator indicator = findViewById(R.id.indicator);
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                startHome.setVisibility(position == 2 ? View.VISIBLE : View.INVISIBLE);
                indicator.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
            }
        });
        viewPager.setAdapter(new InnerPageAdapter());
        indicator.setViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return true;
    }

    @Override
    public void onBackPressed() {
    }

    private static class InnerPageAdapter extends PagerAdapter {

        private static final int[] IMAGES = {
                R.drawable.guide1,
                R.drawable.guide2,
                R.drawable.guide3,
        };

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View view = inflater.inflate(R.layout.guide_item, container, false);
            ImageView imageView = view.findViewById(R.id.image);
            imageView.setImageResource(IMAGES[position]);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
