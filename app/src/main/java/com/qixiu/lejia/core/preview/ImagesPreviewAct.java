package com.qixiu.lejia.core.preview;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Long on 2018/5/8
 * <pre>
 *     图片预览
 * </pre>
 */
public class ImagesPreviewAct extends BaseActivity {

    public static final String EXTRA_STARTING_POSITION = "extra_starting_position";
    public static final String EXTRA_CURRENT_POSITION  = "extra_current_position";

    private static final String KEY_IMAGES    = "IMAGES";
    private static final String KEY_START_POS = "START_POS";

    private ImagePreviewFrag mCurrentFrag;
    private int              mStartingPosition;
    private int              mCurrentPosition;
    private boolean          mIsReturning;

    private SharedElementCallback mSharedElementCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                ImageView sharedElement = mCurrentFrag.getAlbumImage();
                if (sharedElement == null) {
                    // If shared element is null, then it has been scrolled off screen and
                    // no longer visible. In this case we cancel the shared element transition by
                    // removing the shared element from the shared elements map.
                    names.clear();
                    sharedElements.clear();
                } else if (mCurrentPosition != mStartingPosition) {
                    // If the user has swiped to a different ViewPager page, then we need to
                    // remove the old shared element and replace it with the new shared element
                    // that should be transitioned instead.
                    names.clear();
                    names.add(sharedElement.getTransitionName());
                    sharedElements.clear();
                    sharedElements.put(sharedElement.getTransitionName(), sharedElement);
                }
            }
        }
    };


    public static void start(Activity context, List<String> images, int startPos,
                             @Nullable Bundle options) {
        Intent starter = new Intent(context, ImagesPreviewAct.class);
        starter.putStringArrayListExtra(KEY_IMAGES, (ArrayList<String>) images);
        starter.putExtra(KEY_START_POS, startPos);
        if (options == null) {
            context.startActivity(starter);
        } else {
            context.startActivity(starter, options);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置标记可以让内容在状态栏上显示
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.act_image_preview);

        //add shared transition callback
        postponeEnterTransition();
        setEnterSharedElementCallback(mSharedElementCallback);
        getWindow().getSharedElementEnterTransition().setDuration(200)
                .setInterpolator(new LinearOutSlowInInterpolator());
        getWindow().getSharedElementReturnTransition().setDuration(200)
                .setInterpolator(new DecelerateInterpolator());
        getWindow().getSharedElementEnterTransition().addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionStart(Transition transition) {
                if (!mIsReturning) {
                    hideToolbar();
                }
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                if (!mIsReturning) {
                    showToolbar();
                }
            }
        });

        // get extras
        List<String> images = getIntent().getStringArrayListExtra(KEY_IMAGES);
        mStartingPosition = getIntent().getIntExtra(KEY_START_POS, 0);
        mCurrentPosition = mStartingPosition;

        //setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        TextView titleView = findViewById(R.id.title);
        //设置标题
        titleView.setText(String.format(Locale.getDefault(), "1/%d", images.size()));

        ViewPager pager = findViewById(R.id.pager);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                titleView.setText(String.format(Locale.getDefault(), "%d/%d", position + 1, images.size()));
            }
        });
        ImagesPageAdapter adapter = new ImagesPageAdapter(getSupportFragmentManager(), images);
        pager.setAdapter(adapter);
        pager.setCurrentItem(mCurrentPosition, false);
    }

    @Override
    public void finishAfterTransition() {
        //隐藏toolbar
        hideToolbar();

        mIsReturning = true;
        Intent data = new Intent();
        data.putExtra(EXTRA_STARTING_POSITION, mStartingPosition);
        data.putExtra(EXTRA_CURRENT_POSITION, mCurrentPosition);
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    private void showToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
    }

    private void hideToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
    }

    private class ImagesPageAdapter extends FragmentStatePagerAdapter {

        private List<String> images;

        ImagesPageAdapter(FragmentManager fm, List<String> images) {
            super(fm);
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ImagePreviewFrag.newInstance(images.get(position), mStartingPosition, position);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mCurrentFrag = (ImagePreviewFrag) object;
        }
    }

    public static class ImagePreviewFrag extends Fragment {

        private static final String TAG = "ImagePreviewFrag";

        private static final String KEY_SRC = "SRC";

        private String    mSrc;
        private ImageView mImageView;

        private int mStartingPosition;
        private int mPosition;

        public static ImagePreviewFrag newInstance(String src, int startPos, int currentPos) {
            Bundle args = new Bundle();
            args.putString(KEY_SRC, src);
            args.putInt(EXTRA_STARTING_POSITION, startPos);
            args.putInt(EXTRA_CURRENT_POSITION, currentPos);
            ImagePreviewFrag fragment = new ImagePreviewFrag();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle args = getArguments();
            if (args != null) {
                mSrc = args.getString(KEY_SRC);
                mStartingPosition = args.getInt(EXTRA_STARTING_POSITION);
                mPosition = args.getInt(EXTRA_CURRENT_POSITION);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.frag_image_preview, container, false);
            mImageView = (ImageView) root;
            mImageView.setTransitionName(String.valueOf(mPosition));

            Glide.with(this)
                    .load(mSrc)
                    .asBitmap()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mImageView.setImageBitmap(resource);
                            if (mPosition == mStartingPosition) {
                                mImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                    @Override
                                    public boolean onPreDraw() {
                                        mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                                        getActivity().startPostponedEnterTransition();
                                        return true;
                                    }
                                });
                            }
                        }
                    });

            return root;
        }


        /**
         * Returns the shared element that should be transitioned back to the previous Activity,
         * or null if the view is not visible on the screen.
         */
        @Nullable
        ImageView getAlbumImage() {
            if (isViewInBounds(getActivity().getWindow().getDecorView(), mImageView)) {
                return mImageView;
            }
            return null;
        }

        /**
         * Returns true if {@param view} is contained within {@param container}'s bounds.
         */
        private static boolean isViewInBounds(@NonNull View container, @NonNull View view) {
            Rect containerBounds = new Rect();
            container.getHitRect(containerBounds);
            return view.getLocalVisibleRect(containerBounds);
        }
    }


}
