package com.fanclub.zinzin.domain.card.repository;

import com.fanclub.zinzin.domain.card.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByContent(String content);
}
