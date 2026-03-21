package com.novel.serve.controller;

import com.novel.serve.entity.Prompt;
import com.novel.serve.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prompts")
@CrossOrigin(origins = "http://localhost:5173")
public class PromptController {
    
    @Autowired
    private PromptService promptService;
    
    @PostMapping
    public ResponseEntity<Prompt> createPrompt(@RequestBody Prompt prompt) {
        Prompt createdPrompt = promptService.createPrompt(prompt);
        return ResponseEntity.ok(createdPrompt);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Prompt> getPrompt(@PathVariable Long id) {
        return promptService.getPromptById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Prompt>> getAllPrompts() {
        return ResponseEntity.ok(promptService.getAllPrompts());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Prompt> updatePrompt(@PathVariable Long id, @RequestBody Prompt prompt) {
        Prompt updatedPrompt = promptService.updatePrompt(id, prompt);
        if (updatedPrompt != null) {
            return ResponseEntity.ok(updatedPrompt);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrompt(@PathVariable Long id) {
        promptService.deletePrompt(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Prompt>> getPromptsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(promptService.getPromptsByCategory(category));
    }
    
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Prompt>> getPromptsByTag(@PathVariable String tag) {
        return ResponseEntity.ok(promptService.getPromptsByTag(tag));
    }
    
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Prompt>> searchPrompts(@PathVariable String keyword) {
        return ResponseEntity.ok(promptService.searchPrompts(keyword));
    }
    
    @GetMapping("/popular")
    public ResponseEntity<List<Prompt>> getPopularPrompts() {
        return ResponseEntity.ok(promptService.getPopularPrompts());
    }
    
    @PostMapping("/{id}/usage")
    public ResponseEntity<Prompt> incrementUsageCount(@PathVariable Long id) {
        Prompt updatedPrompt = promptService.incrementUsageCount(id);
        if (updatedPrompt != null) {
            return ResponseEntity.ok(updatedPrompt);
        }
        return ResponseEntity.notFound().build();
    }
}