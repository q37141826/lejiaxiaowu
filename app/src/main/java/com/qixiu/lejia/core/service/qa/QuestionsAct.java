package com.qixiu.lejia.core.service.qa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.adapter.BindableViewHolder;
import com.qixiu.adapter.ItemActionHandler;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.Question;
import com.qixiu.lejia.beans.resp.QuestionResp;
import com.qixiu.lejia.databinding.ItemQuestionBinding;

import java.util.List;

/**
 * Created by Long on 2018/5/17
 * <pre>
 *     客户提问
 * </pre>
 */
public class QuestionsAct extends BaseWhiteStateBarActivity {

    private RecyclerView mRecyclerView;

    private List<QuestionResp.ShopContact> shopContacts;

    public static void start(Context context) {
        Intent starter = new Intent(context, QuestionsAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        load();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.act_questions, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_offset_start_16));
        mRecyclerView.addItemDecoration(divider);

        findViewById(R.id.call).setOnClickListener(v -> {
            ShopContactsDialog.newInstance(shopContacts)
                    .show(getSupportFragmentManager(), null);
        });
    }

    @Override
    protected void onReload() {
        load();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        call = AppApi.get().questions();
        call.enqueue(new RequestCallback<QuestionResp>() {
            @Override
            protected void onSuccess(QuestionResp resp) {
                shopContacts = resp.getShopContacts();
                List<Question> questions = resp.getQuestions();
                if (questions.isEmpty()) {
                    switchToEmptyState();
                    return;
                }
                switchToContentState();
                handleResp(questions);
            }

            @Override
            protected void onFailure(ResponseError error) {
                switchToErrorState();
            }
        });
    }

    private void handleResp(List<Question> questions) {
        List<QuestionItem> items = QuestionItem.from(questions);
        BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(items);
        adapter.setItemActionHandler(new ItemActionHandler<QuestionItem>() {
            @Override
            public void onItemClick(QuestionItem item) {
                int pos = adapter.indexOfItem(item);
                BindableViewHolder holder = (BindableViewHolder) mRecyclerView.findViewHolderForLayoutPosition(pos);
                ItemQuestionBinding binding = (ItemQuestionBinding) holder.getBinding();
                int state = binding.expandableLayout.getState();
                int degree = state == 0 ? 90 : 0;
                binding.arrow.clearAnimation();
                binding.arrow.animate()
                        .setDuration(200)
                        .rotation(degree)
                        .start();
                binding.expandableLayout.toggle();
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

}
