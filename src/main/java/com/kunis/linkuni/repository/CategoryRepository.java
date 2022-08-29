package com.kunis.linkuni.repository;

import com.kunis.linkuni.entity.Category;
import com.kunis.linkuni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query(value = "select c from Category c join fetch c.user where c.id = :id")
    Category findByIdFetch(@Param("id") String id);

    @Query(value = "select distinct c from Category c join fetch c.urlList ul where c.user=:user")
    List<Category> findByUserJoinFetch(User user);

    List<Category> findByUser(User user);

    boolean existsByUserAndName(User user, String name);
}
