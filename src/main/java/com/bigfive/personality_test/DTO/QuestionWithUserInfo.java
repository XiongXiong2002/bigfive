package com.bigfive.personality_test.DTO;

import java.util.List;

import com.bigfive.personality_test.entities.Question;

public class QuestionWithUserInfo {
    private String userName;
    private String passWord;
    private List<Question> question;

    public QuestionWithUserInfo(String userName, String passWord, List<Question> question) {
        this.userName = userName;
        this.passWord = passWord;
        this.question = question;
    }

    // Getters
    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public List<Question> getQuestion() {
        return question;
    }

    // Setters
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setQuestion(List<Question> question) {
        this.question = question;
    }
}
