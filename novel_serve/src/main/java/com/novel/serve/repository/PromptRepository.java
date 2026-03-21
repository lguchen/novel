package com.novel.serve.repository;

import com.novel.serve.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
    List<Prompt> findByCategory(String category);
    List<Prompt> findByTagsContaining(String tag);
    List<Prompt> findByContentContaining(String keyword);
    List<Prompt> findByOrderByUsageCountDesc();
}