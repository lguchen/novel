package com.novel.serve.controller;

import com.novel.serve.entity.NovelCharacter;
import com.novel.serve.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
@CrossOrigin(origins = "http://localhost:5173")
public class CharacterController {
    
    @Autowired
    private CharacterRepository characterRepository;
    
    @PostMapping
    public ResponseEntity<NovelCharacter> createCharacter(@RequestBody NovelCharacter character) {
        NovelCharacter createdCharacter = characterRepository.save(character);
        return ResponseEntity.ok(createdCharacter);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NovelCharacter> getCharacter(@PathVariable Long id) {
        return characterRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<NovelCharacter>> getAllCharacters() {
        return ResponseEntity.ok(characterRepository.findAll());
    }
    
    @GetMapping("/novel/{novelId}")
    public ResponseEntity<List<NovelCharacter>> getCharactersByNovel(@PathVariable Long novelId) {
        return ResponseEntity.ok(characterRepository.findByNovelId(novelId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<NovelCharacter> updateCharacter(@PathVariable Long id, @RequestBody NovelCharacter character) {
        return characterRepository.findById(id)
                .map(existingCharacter -> {
                    existingCharacter.setName(character.getName());
                    existingCharacter.setRole(character.getRole());
                    existingCharacter.setPersonality(character.getPersonality());
                    existingCharacter.setBackground(character.getBackground());
                    existingCharacter.setAppearance(character.getAppearance());
                    existingCharacter.setRelationships(character.getRelationships());
                    existingCharacter.setNovelId(character.getNovelId());
                    NovelCharacter updatedCharacter = characterRepository.save(existingCharacter);
                    return ResponseEntity.ok(updatedCharacter);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        characterRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}