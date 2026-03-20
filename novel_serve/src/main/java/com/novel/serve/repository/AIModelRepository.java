package com.novel.serve.repository;

import com.novel.serve.entity.AIModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AIModelRepository extends JpaRepository<AIModel, Long> {
    List<AIModel> findByIsActive(Boolean isActive);
    Optional<AIModel> findByIdAndIsActive(Long id, Boolean isActive);
    List<AIModel> findByProvider(String provider);
}