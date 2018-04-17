package com.den.jaeger.themansion.game_feature.model;

import java.io.Serializable;

public class HighScoreModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private int timeFinished;

    public HighScoreModel(String name, int timeFinished) {
        this.name = name;
        this.timeFinished = timeFinished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeFinished() {
        return timeFinished;
    }

    public void setTimeFinished(int timeFinished) {
        this.timeFinished = timeFinished;
    }

}
