package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/17 0017
 */
public class Question {

    @SerializedName("qu_title")
    private String question;
    @SerializedName("qu_content")
    private String answer;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question1 = (Question) o;

        if (question != null ? !question.equals(question1.question) : question1.question != null)
            return false;
        return answer != null ? answer.equals(question1.answer) : question1.answer == null;
    }

    @Override
    public int hashCode() {
        int result = question != null ? question.hashCode() : 0;
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        return result;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
