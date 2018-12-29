package com.qixiu.lejia.core.service.qa;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.Question;
import com.qixiu.lejia.utils.Lists;

import java.util.List;

/**
 * Created by Long on 2018/5/17
 */
public class QuestionItem extends BindableItem {

    private Question question;

    private QuestionItem(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    @Override
    public int getViewType() {
        return R.layout.item_question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionItem that = (QuestionItem) o;

        return question != null ? question.equals(that.question) : that.question == null;
    }

    @Override
    public int hashCode() {
        return question != null ? question.hashCode() : 0;
    }

    public static List<QuestionItem> from(List<Question> questions) {
        List<QuestionItem> items = Lists.newArrayList();
        for (Question question : questions) {
            items.add(new QuestionItem(question));
        }
        return items;
    }

}

