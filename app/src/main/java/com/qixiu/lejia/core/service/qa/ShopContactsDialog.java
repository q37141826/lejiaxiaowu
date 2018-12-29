package com.qixiu.lejia.core.service.qa;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qixiu.adapter.BindableItem;
import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.resp.QuestionResp;
import com.qixiu.lejia.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2018/6/7
 */
public class ShopContactsDialog extends BottomSheetDialogFragment {

    private static final String KEY_SHOP_CONTACTS = "SHOP_CONTACTS";

    private RecyclerView mRecyclerView;

    private TextView dismiss;

    public static ShopContactsDialog newInstance(List<QuestionResp.ShopContact> shopContacts) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_SHOP_CONTACTS, (ArrayList<? extends Parcelable>) shopContacts);
        ShopContactsDialog fragment = new ShopContactsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_shop_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        dismiss = view.findViewById(R.id.dismiss);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<QuestionResp.ShopContact> shopContacts = getArguments().getParcelableArrayList(KEY_SHOP_CONTACTS);
        List<BindableItem> items = new ArrayList<>();
        //noinspection ConstantConditions
        for (QuestionResp.ShopContact contact : shopContacts) {
            items.add(new ShopContactItem(contact));
        }
        BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(items);
        adapter.setItemActionHandler(o -> {
            ShopContactItem item = (ShopContactItem) o;
            IntentUtils.startDial(getContext(), item.getContact().getContact());
            dismiss();
        });
        mRecyclerView.setAdapter(adapter);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
