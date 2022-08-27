package com.kunis.linkuni.repository;

import com.kunis.linkuni.entity.Tag;
import com.kunis.linkuni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    List<Tag> findByUser(User usre);
}
