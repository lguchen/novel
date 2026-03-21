package com.novel.serve.service;

import com.novel.serve.entity.Chapter;
import com.novel.serve.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChapterService {
    
    @Autowired
    private ChapterRepository chapterRepository;
    
    public Chapter createChapter(Chapter chapter) {
        // 计算章节字数
        if (chapter.getContent() != null) {
            chapter.setWordCount(chapter.getContent().length());
        }
        return chapterRepository.save(chapter);
    }
    
    public Chapter updateChapter(Long id, Chapter chapter) {
        Optional<Chapter> existingChapter = chapterRepository.findById(id);
        if (existingChapter.isPresent()) {
            Chapter updatedChapter = existingChapter.get();
            updatedChapter.setTitle(chapter.getTitle());
            updatedChapter.setContent(chapter.getContent());
            updatedChapter.setWordCount(chapter.getContent() != null ? chapter.getContent().length() : 0);
            return chapterRepository.save(updatedChapter);
        }
        return null;
    }
    
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }
    
    public Optional<Chapter> getChapterById(Long id) {
        return chapterRepository.findById(id);
    }
    
    public List<Chapter> getChaptersByNovelId(Long novelId) {
        return chapterRepository.findByNovelIdOrderByChapterNumberAsc(novelId);
    }
    
    public Chapter appendContent(Long id, String newContent) {
        Optional<Chapter> chapterOpt = chapterRepository.findById(id);
        if (chapterOpt.isPresent()) {
            Chapter chapter = chapterOpt.get();
            String currentContent = chapter.getContent() != null ? chapter.getContent() : "";
            chapter.setContent(currentContent + newContent);
            chapter.setWordCount(chapter.getContent().length());
            return chapterRepository.save(chapter);
        }
        return null;
    }
    
    public Integer getNextChapterNumber(Long novelId) {
        List<Chapter> chapters = chapterRepository.findByNovelIdOrderByChapterNumberAsc(novelId);
        if (chapters.isEmpty()) {
            return 1;
        }
        return chapters.get(chapters.size() - 1).getChapterNumber() + 1;
    }
}