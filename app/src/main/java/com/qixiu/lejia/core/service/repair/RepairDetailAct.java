package com.qixiu.lejia.core.service.repair;

import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.base.BaseMultiStateAct;
import com.qixiu.lejia.beans.ImageInfo;
import com.qixiu.lejia.beans.RepairDetail;
import com.qixiu.lejia.core.preview.ImagesPreviewAct;
import com.qixiu.lejia.databinding.ActRepairDetailBinding;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.widget.SpaceItemDecoration;

import java.util.List;
import java.util.Map;

/**
 * Created by Long on 2018/5/16
 * <pre>
 *     报修详情
 * </pre>
 */
public class RepairDetailAct extends BaseMultiStateAct {

    private static final String TAG = "RepairDetailAct";

    private static final String KEY_ID = "ID";

    private ActRepairDetailBinding mBinding;

    private Bundle mTmpReenterState;

    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mTmpReenterState != null) {
                int startPos = mTmpReenterState.getInt(ImagesPreviewAct.EXTRA_STARTING_POSITION);
                int currentPos = mTmpReenterState.getInt(ImagesPreviewAct.EXTRA_CURRENT_POSITION);
                if (startPos != currentPos) {
                    View transitionView = getTransitionView(mBinding.recyclerView, currentPos);
                    if (transitionView != null) {
                        names.clear();
                        sharedElements.clear();
                        names.add(transitionView.getTransitionName());
                        sharedElements.put(transitionView.getTransitionName(), transitionView);
                    }
                }
                mTmpReenterState = null;
            }
        }
    };

    public static void start(Context context, String id) {
        Intent starter = new Intent(context, RepairDetailAct.class);
        starter.putExtra(KEY_ID, id);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setExitSharedElementCallback(mCallback);

        load();
//        loadSignedRoomInfo();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        mTmpReenterState = new Bundle(data.getExtras());
        postponeEnterTransition();
        startPostponedEnterTransition();
    }

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        mBinding = ActRepairDetailBinding.inflate(inflater);
        //noinspection ConstantConditions
        return mBinding.getRoot();
    }

    @Override
    protected void onContentViewCreated(View view) {

        //Setup RecyclerView
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mBinding.recyclerView.addItemDecoration(new SpaceItemDecoration(this, 8));

        mBinding.done.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("是否确认该报修已完成？")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        doneRepair();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });
    }

    @Override
    protected void onReload() {
        load();
//        loadSignedRoomInfo();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        String id = getIntent().getStringExtra(KEY_ID);
        call = AppApi.get().repairDetail(id);
        call.enqueue(new RequestCallback<RepairDetail>() {
            @Override
            protected void onSuccess(RepairDetail detail) {
                switchToContentState();
                handleResp(detail);
            }

            @Override
            protected void onFailure(ResponseError error) {
                switchToErrorState();
            }
        });
    }

    private void handleResp(RepairDetail detail) {
        mBinding.setItem(detail);

        int status = detail.getStatus();

        if (status == 1) {
            mBinding.submitTimeline.setChecked(true);
            mBinding.allotTimeline.setChecked(false);
            mBinding.doneTimeline.setChecked(false);
        } else if (status == 2) {
            mBinding.submitTimeline.setChecked(true);
            mBinding.allotTimeline.setChecked(true);
            mBinding.doneTimeline.setChecked(false);
        } else if (status == 3) {
            mBinding.submitTimeline.setChecked(true);
            mBinding.allotTimeline.setChecked(true);
            mBinding.doneTimeline.setChecked(true);
        } else if (status == 4) {
            mBinding.submitTimeline.setChecked(true);
            mBinding.allotTimeline.setChecked(true);
            mBinding.doneTimeline.setChecked(false);
            mBinding.allotText.setText("已拒绝");
        }

        mBinding.done.setVisibility(status == 2 ? View.VISIBLE : View.GONE);
        mBinding.doneContainer.setVisibility(status == 2 ? View.GONE : View.VISIBLE);

        //images
        List<ImageInfo> images = detail.getImages();
        if (images == null || images.isEmpty()) {
            return;
        }
        ImagesAdapter imagesAdapter = new ImagesAdapter(images, false);
        imagesAdapter.setOnItemClickListener(new ImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int viewType, int position) {
                List<String> images = imagesAdapter.getImages();

                View transitionView = getTransitionView(mBinding.recyclerView, position);
                Logger.d(TAG, "onItemClick: " + transitionView.getTransitionName());
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                        RepairDetailAct.this,
                        transitionView,    //做动画的View
                        transitionView.getTransitionName())
                        .toBundle();

                ImagesPreviewAct.start(RepairDetailAct.this, images, position, bundle);

            }

            @Override
            public void onItemDel(int position) {
                //ignore
            }
        });

        mBinding.recyclerView.setAdapter(imagesAdapter);

    }


    private View getTransitionView(RecyclerView parent, int pos) {
        ImagesAdapter.ItemViewHolder holder = (ImagesAdapter.ItemViewHolder)
                parent.findViewHolderForAdapterPosition(pos);
        return holder.view;
    }


    //完成报修
    @SuppressWarnings("unchecked")
    private void doneRepair() {
        String id = getIntent().getStringExtra(KEY_ID);
        call = AppApi.get().doneRepair(id);
        call.enqueue(new RequestCallback() {
            @Override
            protected void onSuccess(Object o) {
                //
                finish();
            }
        });
    }

//    //获取已签约房间信息
//    @SuppressWarnings("unchecked")
//    private void loadSignedRoomInfo() {
//        call = AppApi.get().signedRoomInfo(LoginStatus.getToken());
//        call.enqueue(new RequestCallback<SignedRoom>() {
//            @Override
//            protected void onSuccess(SignedRoom o) {
//                mBinding.roomInfo.setText(o.getRoomNum());
//            }
//        });
//    }

}
