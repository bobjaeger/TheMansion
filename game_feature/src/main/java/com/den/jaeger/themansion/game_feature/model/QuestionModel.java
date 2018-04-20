package com.den.jaeger.themansion.game_feature.model;

import java.util.List;

public class QuestionModel {
    int qid;
    String question;
    List<AnswerModel> answerLists;

    public QuestionModel(int qid, String question, List<AnswerModel> answerLists) {
        this.qid = qid;
        this.question = question;
        this.answerLists = answerLists;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<AnswerModel> getAnswerLists() {
        return answerLists;
    }

    public void setAnswerLists(List<AnswerModel> answerLists) {
        this.answerLists = answerLists;
    }
}
