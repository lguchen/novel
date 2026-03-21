package com.novel.serve.controller;

import com.novel.serve.entity.Chapter;
import com.novel.serve.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@CrossOrigin(origins = "http://localhost:5173")
public class ChapterController {
    
    @Autowired
    private ChapterService chapterService;
    
    @PostMapping
    public ResponseEntity<Chapter> createChapter(@RequestBody Chapter chapter) {
        Chapter createdChapter = chapterService.createChapter(chapter);
        return ResponseEntity.ok(createdChapter);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Chapter> getChapter(@PathVariable Long id) {
        return chapterService.getChapterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/novel/{novelId}")
    public ResponseEntity<List<Chapter>> getChaptersByNovelId(@PathVariable Long novelId) {
        return ResponseEntity.ok(chapterService.getChaptersByNovelId(novelId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Chapter> updateChapter(@PathVariable Long id, @RequestBody Chapter chapter) {
        Chapter updatedChapter = chapterService.updateChapter(id, chapter);
        if (updatedChapter != null) {
            return ResponseEntity.ok(updatedChapter);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/content")
    public ResponseEntity<Chapter> appendContent(@PathVariable Long id, @RequestBody String content) {
        Chapter updatedChapter = chapterService.appendContent(id, content);
        if (updatedChapter != null) {
            return ResponseEntity.ok(updatedChapter);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/novel/{novelId}/next-chapter-number")
    public ResponseEntity<Integer> getNextChapterNumber(@PathVariable Long novelId) {
        return ResponseEntity.ok(chapterService.getNextChapterNumber(novelId));
    }
}