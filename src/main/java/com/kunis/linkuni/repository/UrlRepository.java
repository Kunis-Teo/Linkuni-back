package com.kunis.linkuni.repository;

import com.kunis.linkuni.entity.Category;
import com.kunis.linkuni.entity.Url;
import com.kunis.linkuni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UrlRepository extends JpaRepository<Url, String> {
    Boolean existsByUrlAndUser(String url, User user);

    List<Url> findByCategory(Category category);

    @Query(value = "select distinct u from Url u join fetch u.urlTagList ut join fetch ut.tag where u.category=:category")
    List<Url> findByCategoryJoinFetch(Category category);

    List<Url> findByUser(User user);
}
