package com.novel.serve.controller;

import com.novel.serve.entity.Novel;
import com.novel.serve.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/novels")
@CrossOrigin(origins = "http://localhost:5173")
public class NovelController {
    
    @Autowired
    private NovelService novelService;
    
    @PostMapping
    public ResponseEntity<Novel> createNovel(@RequestBody Novel novel) {
        Novel createdNovel = novelService.createNovel(novel);
        return ResponseEntity.ok(createdNovel);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Novel> getNovel(@PathVariable Long id) {
        return novelService.getNovelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Novel>> getAllNovels() {
        return ResponseEntity.ok(novelService.getAllNovels());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Novel> updateNovel(@PathVariable Long id, @RequestBody Novel novel) {
        Novel updatedNovel = novelService.updateNovel(id, novel);
        if (updatedNovel != null) {
            return ResponseEntity.ok(updatedNovel);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNovel(@PathVariable Long id) {
        novelService.deleteNovel(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/content")
    public ResponseEntity<Novel> appendContent(@PathVariable Long id, @RequestBody String content) {
        Novel updatedNovel = novelService.appendContent(id, content);
        if (updatedNovel != null) {
            return ResponseEntity.ok(updatedNovel);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/memory")
    public ResponseEntity<Novel> updateMemory(@PathVariable Long id, @RequestBody String contextMemory) {
        Novel updatedNovel = novelService.updateContextMemory(id, contextMemory);
        if (updatedNovel != null) {
            return ResponseEntity.ok(updatedNovel);
        }
        return ResponseEntity.notFound().build();
    }
}