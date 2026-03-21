package com.novel.serve.repository;

import com.novel.serve.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByNovelIdOrderByChapterNumberAsc(Long novelId);
    Chapter findByNovelIdAndChapterNumber(Long novelId, Integer chapterNumber);
}