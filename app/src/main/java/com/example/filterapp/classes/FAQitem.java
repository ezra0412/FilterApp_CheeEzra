package com.example.filterapp.classes;

public class FAQitem {
    private String question;
    private String answer;
    private boolean isExpandable;

    public FAQitem(String question, String answer, boolean isExpandable) {
        this.question = question;
        this.answer = answer;
        this.isExpandable = isExpandable;
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

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }
}
