package com.bigfive.personality_test.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bigfive.personality_test.entities.PersonalityComments;
import com.bigfive.personality_test.entities.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {


    // 查询所有问题
    @Query(value = "SELECT * FROM questions", nativeQuery = true)
    List<Question> getAllQuestions();

    // 插入新问题（不需要额外定义，JpaRepository 自带 save() 方法）
    
    // 删除问题
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM questions WHERE id = :id", nativeQuery = true) // ✅ 直接从表里删除
    void deleteQuestionById(@Param("id") int id);
    

    // 当 nativeQuery = true 时，Spring Data JPA 允许你在 @Query 注解中直接编写 原生 SQL 语句，而不是使用 JPQL（Java Persistence Query Language）
    // 查询指定类别的随机问题
    @Query(value = "SELECT * FROM questions WHERE category = :category ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Question> findQuestionsByCategory(@Param("category") String category, @Param("limit") int limit);

    // 查询简单测试结果
    @Query(value = "SELECT * FROM PersonalityComments WHERE category = :category AND level = :level LIMIT 1", nativeQuery = true)
    PersonalityComments findSimpleResult(@Param("category") String category, @Param("level") String level);

    // 查询子类别的问题
    @Query(value = "SELECT * FROM questions WHERE category = :category AND subCategory = :subCategory ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<Question> findQuestionsBySubCategory(@Param("category") String category, @Param("subCategory") String subCategory);

    @Query(value = "SELECT COUNT(*) FROM questions WHERE subCategory = :subCategory", nativeQuery = true)
    int findNumberOfQuestions(@Param("subCategory") String subCategory );
}
