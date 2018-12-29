package com.qixiu.lejia.core.shop;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.ShopDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2018/6/1
 */
public class SelectRoomDialogFrag extends BottomSheetDialogFragment {

    private static final String KEY_ROOMS = "ROOMS";

    private RecyclerView mRecyclerView;

    public static SelectRoomDialogFrag newInstance(List<ShopDetail.RecommendRoom> rooms) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_ROOMS, (ArrayList<? extends Parcelable>) rooms);
        SelectRoomDialogFrag fragment = new SelectRoomDialogFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_select_room, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false));
        mRecyclerView.addItemDecoration(new ShopDetailAct.RoomDivider());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            List<ShopDetail.RecommendRoom> rooms = args.getParcelableArrayList(KEY_ROOMS);
            if (rooms == null) {
                return;
            }
            List<RecommendRoomItem> items = new ArrayList<>();
            for (ShopDetail.RecommendRoom room : rooms) {
                items.add(new RecommendRoomItem(room));
            }
//            BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(items);
//            adapter.setItemActionHandler(o -> {
////                AuthenticationAct.start(getContext(), UserType.PERSONAL);
//                RecommendRoomItem item = (RecommendRoomItem) o;
//                RoomDetailAct.start(getContext(), item.getRoom().getId());
//                dismiss();
//            });
            TimeAdapter adapter =new TimeAdapter(getContext(),items);
            mRecyclerView.setAdapter(adapter);
        }
    }
}
