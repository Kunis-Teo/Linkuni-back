package com.kunis.linkuni.repository;

import com.kunis.linkuni.entity.Category;
import com.kunis.linkuni.entity.Url;
import com.kunis.linkuni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlRepository extends JpaRepository<Url, String> {
    Boolean existsByUrlAndUser(String url, User user);

    List<Url> findByCategory(Category category);

    List<Url> findByUser(User user);
}
