package com.novel.serve.controller;

import com.novel.serve.entity.AIModel;
import com.novel.serve.repository.AIModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-models")
@CrossOrigin(origins = "http://localhost:5173")
public class AIModelController {
    
    @Autowired
    private AIModelRepository aiModelRepository;
    
    @PostMapping
    public ResponseEntity<AIModel> createAIModel(@RequestBody AIModel aiModel) {
        AIModel createdModel = aiModelRepository.save(aiModel);
        return ResponseEntity.ok(createdModel);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AIModel> getAIModel(@PathVariable Long id) {
        return aiModelRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<AIModel>> getAllAIModels() {
        return ResponseEntity.ok(aiModelRepository.findAll());
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<AIModel>> getActiveAIModels() {
        return ResponseEntity.ok(aiModelRepository.findByIsActive(true));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AIModel> updateAIModel(@PathVariable Long id, @RequestBody AIModel aiModel) {
        return aiModelRepository.findById(id)
                .map(existingModel -> {
                    existingModel.setName(aiModel.getName());
                    existingModel.setProvider(aiModel.getProvider());
                    existingModel.setModelType(aiModel.getModelType());
                    existingModel.setApiKey(aiModel.getApiKey());
                    existingModel.setApiUrl(aiModel.getApiUrl());
                    existingModel.setModelId(aiModel.getModelId());
                    existingModel.setMaxTokens(aiModel.getMaxTokens());
                    existingModel.setTemperature(aiModel.getTemperature());
                    existingModel.setIsActive(aiModel.getIsActive());
                    AIModel updatedModel = aiModelRepository.save(existingModel);
                    return ResponseEntity.ok(updatedModel);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAIModel(@PathVariable Long id) {
        aiModelRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/toggle")
    public ResponseEntity<AIModel> toggleAIModel(@PathVariable Long id) {
        return aiModelRepository.findById(id)
                .map(model -> {
                    model.setIsActive(!model.getIsActive());
                    AIModel updatedModel = aiModelRepository.save(model);
                    return ResponseEntity.ok(updatedModel);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}