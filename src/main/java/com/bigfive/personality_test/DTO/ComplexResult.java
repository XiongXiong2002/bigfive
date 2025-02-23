package com.bigfive.personality_test.DTO;

public class ComplexResult {
    
    private String subPersonality; // 子人格类型 (e.g., "Imagination")
    private String personality; // 大人格类型 (e.g., "Openness")
    private String comment; // 评价内容

    // 无参构造方法（Spring 需要）
    public ComplexResult() {}

    // 带参数构造方法
    public ComplexResult(String personality, String subPersonality, String comment) {
        this.personality = personality;
        this.subPersonality = subPersonality;
        this.comment = comment;
    }

    // Getter & Setter
    public String getSubPersonality() {
        return subPersonality;
    }

    public void setSubPersonality(String subPersonality) {
        this.subPersonality = subPersonality;
    }

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
