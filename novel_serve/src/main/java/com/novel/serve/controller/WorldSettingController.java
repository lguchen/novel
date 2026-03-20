package com.novel.serve.controller;

import com.novel.serve.entity.WorldSetting;
import com.novel.serve.repository.WorldSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/world-settings")
@CrossOrigin(origins = "http://localhost:5173")
public class WorldSettingController {
    
    @Autowired
    private WorldSettingRepository worldSettingRepository;
    
    @PostMapping
    public ResponseEntity<WorldSetting> createWorldSetting(@RequestBody WorldSetting worldSetting) {
        WorldSetting createdSetting = worldSettingRepository.save(worldSetting);
        return ResponseEntity.ok(createdSetting);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WorldSetting> getWorldSetting(@PathVariable Long id) {
        return worldSettingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<WorldSetting>> getAllWorldSettings() {
        return ResponseEntity.ok(worldSettingRepository.findAll());
    }
    
    @GetMapping("/novel/{novelId}")
    public ResponseEntity<List<WorldSetting>> getWorldSettingsByNovel(@PathVariable Long novelId) {
        return ResponseEntity.ok(worldSettingRepository.findByNovelId(novelId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<WorldSetting> updateWorldSetting(@PathVariable Long id, @RequestBody WorldSetting worldSetting) {
        return worldSettingRepository.findById(id)
                .map(existingSetting -> {
                    existingSetting.setName(worldSetting.getName());
                    existingSetting.setCategory(worldSetting.getCategory());
                    existingSetting.setDescription(worldSetting.getDescription());
                    existingSetting.setRules(worldSetting.getRules());
                    existingSetting.setGeography(worldSetting.getGeography());
                    existingSetting.setHistory(worldSetting.getHistory());
                    existingSetting.setNovelId(worldSetting.getNovelId());
                    WorldSetting updatedSetting = worldSettingRepository.save(existingSetting);
                    return ResponseEntity.ok(updatedSetting);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorldSetting(@PathVariable Long id) {
        worldSettingRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}