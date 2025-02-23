package com.bigfive.personality_test.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PersonalityComments")
public class PersonalityComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")  // 映射数据库字段 category
    private String category;  

    @Column(name = "level")  // 映射数据库字段 level
    private String level;

    @Column(name = "comment")  // 映射数据库字段 comment
    private String comment;

    // 默认构造函数（JPA 需要）
    public PersonalityComments() {}

    // Getters and Setters
    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getCategory() { 
        return category; 
    }

    public void setCategory(String category) { 
        this.category = category; 
    }

    public String getLevel() { 
        return level; 
    }

    public void setLevel(String level) { 
        this.level = level; 
    }

    public String getComment() { 
        return comment; 
    }

    public void setComment(String comment) { 
        this.comment = comment; 
    }

    
}
