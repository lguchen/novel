package com.novel.serve.service;

import com.novel.serve.entity.Prompt;
import com.novel.serve.repository.PromptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PromptService {
    
    @Autowired
    private PromptRepository promptRepository;
    
    public Prompt createPrompt(Prompt prompt) {
        return promptRepository.save(prompt);
    }
    
    public Prompt updatePrompt(Long id, Prompt prompt) {
        Optional<Prompt> existingPrompt = promptRepository.findById(id);
        if (existingPrompt.isPresent()) {
            Prompt updatedPrompt = existingPrompt.get();
            updatedPrompt.setContent(prompt.getContent());
            updatedPrompt.setCategory(prompt.getCategory());
            updatedPrompt.setTags(prompt.getTags());
            return promptRepository.save(updatedPrompt);
        }
        return null;
    }
    
    public void deletePrompt(Long id) {
        promptRepository.deleteById(id);
    }
    
    public Optional<Prompt> getPromptById(Long id) {
        return promptRepository.findById(id);
    }
    
    public List<Prompt> getAllPrompts() {
        return promptRepository.findAll();
    }
    
    public List<Prompt> getPromptsByCategory(String category) {
        return promptRepository.findByCategory(category);
    }
    
    public List<Prompt> getPromptsByTag(String tag) {
        return promptRepository.findByTagsContaining(tag);
    }
    
    public List<Prompt> searchPrompts(String keyword) {
        return promptRepository.findByContentContaining(keyword);
    }
    
    public List<Prompt> getPopularPrompts() {
        return promptRepository.findByOrderByUsageCountDesc();
    }
    
    public Prompt incrementUsageCount(Long id) {
        Optional<Prompt> promptOpt = promptRepository.findById(id);
        if (promptOpt.isPresent()) {
            Prompt prompt = promptOpt.get();
            prompt.setUsageCount(prompt.getUsageCount() + 1);
            return promptRepository.save(prompt);
        }
        return null;
    }
}