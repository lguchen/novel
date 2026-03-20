package com.novel.serve.service;

import com.novel.serve.entity.Novel;
import com.novel.serve.repository.NovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NovelService {
    
    @Autowired
    private NovelRepository novelRepository;
    
    public Novel createNovel(Novel novel) {
        return novelRepository.save(novel);
    }
    
    public Novel updateNovel(Long id, Novel novel) {
        Optional<Novel> existingNovel = novelRepository.findById(id);
        if (existingNovel.isPresent()) {
            Novel updatedNovel = existingNovel.get();
            updatedNovel.setTitle(novel.getTitle());
            updatedNovel.setDescription(novel.getDescription());
            updatedNovel.setGenre(novel.getGenre());
            updatedNovel.setContent(novel.getContent());
            updatedNovel.setContextMemory(novel.getContextMemory());
            updatedNovel.setStyleGuide(novel.getStyleGuide());
            updatedNovel.setWordCount(novel.getWordCount());
            return novelRepository.save(updatedNovel);
        }
        return null;
    }
    
    public void deleteNovel(Long id) {
        novelRepository.deleteById(id);
    }
    
    public Optional<Novel> getNovelById(Long id) {
        return novelRepository.findById(id);
    }
    
    public List<Novel> getAllNovels() {
        return novelRepository.findAll();
    }
    
    public Novel updateContextMemory(Long id, String contextMemory) {
        Optional<Novel> novelOpt = novelRepository.findById(id);
        if (novelOpt.isPresent()) {
            Novel novel = novelOpt.get();
            novel.setContextMemory(contextMemory);
            return novelRepository.save(novel);
        }
        return null;
    }
    
    public Novel appendContent(Long id, String newContent) {
        Optional<Novel> novelOpt = novelRepository.findById(id);
        if (novelOpt.isPresent()) {
            Novel novel = novelOpt.get();
            String currentContent = novel.getContent() != null ? novel.getContent() : "";
            novel.setContent(currentContent + newContent);
            novel.setWordCount(novel.getContent().length());
            return novelRepository.save(novel);
        }
        return null;
    }
}