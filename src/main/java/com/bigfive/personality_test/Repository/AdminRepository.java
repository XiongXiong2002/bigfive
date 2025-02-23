package com.bigfive.personality_test.Repository;

import com.bigfive.personality_test.entities.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<AdminUser, Long> {

    // 🔹 查询所有匹配 `username` 的管理员（可能有多个）
    @Query(value = "SELECT * FROM admin WHERE username = :username AND role ='ADMIN'", nativeQuery = true)
    List<AdminUser> findByUsername(@Param("username") String username);

    
}
