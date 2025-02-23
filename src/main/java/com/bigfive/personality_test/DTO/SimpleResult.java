package com.bigfive.personality_test.DTO;

public class SimpleResult {
    private String personality; // 人格类型 (e.g., "Openness")
    private String comment; // 评价内容

    // 构造方法
    public SimpleResult(String personality, String comment) {
        this.personality = personality;
        this.comment = comment;
    }

    // Getter & Setter
    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
