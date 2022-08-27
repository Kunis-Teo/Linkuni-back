package com.kunis.linkuni.repository;

import com.kunis.linkuni.entity.Url;
import com.kunis.linkuni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, String> {
    Boolean existsByUrlAndUser(String url, User user);
}
