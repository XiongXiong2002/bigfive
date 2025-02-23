package com.bigfive.personality_test.DTO;

import java.util.Map;

public class PersonalityScore {
    private String personality;
    private Map<String, Integer> subcategories;

    // Getter 和 Setter
    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public Map<String, Integer> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Map<String, Integer> subcategories) {
        this.subcategories = subcategories;
    }


    public int calculateTotalScore() {
        int totalScore = 0;
        for (Integer score : subcategories.values()) {
            totalScore += score;
        }
        return totalScore;
    }
}
