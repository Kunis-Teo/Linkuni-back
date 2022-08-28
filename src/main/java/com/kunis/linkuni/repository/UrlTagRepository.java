package com.kunis.linkuni.repository;

import com.kunis.linkuni.entity.Category;
import com.kunis.linkuni.entity.Url;
import com.kunis.linkuni.entity.UrlTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UrlTagRepository extends JpaRepository<UrlTag, String> {
    @Query(value = "select ut from UrlTag ut join fetch ut.tag where ut.url.id = :id")
    List<UrlTag> findByUrl(@Param("id") String id);


}
