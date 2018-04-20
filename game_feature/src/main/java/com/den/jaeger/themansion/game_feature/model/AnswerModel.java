package com.den.jaeger.themansion.game_feature.model;

public class AnswerModel {
    int nextQid;
    String answer;
    String anotherDialog;
    int effect;

    public AnswerModel(int nextQid, String answer, String anotherDialog, int effect) {
        this.nextQid = nextQid;
        this.answer = answer;
        this.anotherDialog = anotherDialog;
        this.effect = effect;
    }

    public int getNextQid() {
        return nextQid;
    }

    public void setNextQid(int nextQid) {
        this.nextQid = nextQid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnotherDialog() {
        return anotherDialog;
    }

    public void setAnotherDialog(String anotherDialog) {
        this.anotherDialog = anotherDialog;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }
}
