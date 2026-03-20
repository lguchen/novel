package com.novel.serve.repository;

import com.novel.serve.entity.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovelRepository extends JpaRepository<Novel, Long> {
    List<Novel> findByGenre(String genre);
    List<Novel> findByTitleContainingIgnoreCase(String title);
}