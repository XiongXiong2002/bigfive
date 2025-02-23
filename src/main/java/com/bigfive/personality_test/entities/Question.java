package com.bigfive.personality_test.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 注解表示这是一个对应数据库表的实体类
@Entity
// 对应的数据库表名
@Table(name = "questions")
public class Question {
    // 主键字段，自动生成
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 问题内容
    private String content;

    // 主要分类（如 "openness"）
    private String category;

    // 子分类（如 "imagination"）
    private String subcategory;

    // Getter 和 Setter 方法

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
}