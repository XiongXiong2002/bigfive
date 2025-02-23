package com.bigfive.personality_test.DTO;

public class DeleteInfo {
    private int id;
    private String subCategory;

    // Constructor
    public DeleteInfo() {
    }

    public DeleteInfo(int id, String subCategory) {
        this.id = id;
        this.subCategory = subCategory;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for subCategory
    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return "DeleteInfo{id=" + id + ", subCategory='" + subCategory + "'}";
    }
}
