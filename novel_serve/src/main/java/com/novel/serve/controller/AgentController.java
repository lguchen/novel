package com.novel.serve.controller;

import com.novel.serve.entity.Agent;
import com.novel.serve.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@CrossOrigin(origins = "http://localhost:5173")
public class AgentController {
    
    @Autowired
    private AgentRepository agentRepository;
    
    @PostMapping
    public ResponseEntity<Agent> createAgent(@RequestBody Agent agent) {
        Agent createdAgent = agentRepository.save(agent);
        return ResponseEntity.ok(createdAgent);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgent(@PathVariable Long id) {
        return agentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Agent>> getAllAgents() {
        return ResponseEntity.ok(agentRepository.findAll());
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Agent>> getActiveAgents() {
        return ResponseEntity.ok(agentRepository.findByIsActive(true));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Agent> updateAgent(@PathVariable Long id, @RequestBody Agent agent) {
        return agentRepository.findById(id)
                .map(existingAgent -> {
                    existingAgent.setName(agent.getName());
                    existingAgent.setDescription(agent.getDescription());
                    existingAgent.setSystemPrompt(agent.getSystemPrompt());
                    existingAgent.setInstructions(agent.getInstructions());
                    existingAgent.setRole(agent.getRole());
                    existingAgent.setModelId(agent.getModelId());
                    existingAgent.setIsActive(agent.getIsActive());
                    Agent updatedAgent = agentRepository.save(existingAgent);
                    return ResponseEntity.ok(updatedAgent);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        agentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/toggle")
    public ResponseEntity<Agent> toggleAgent(@PathVariable Long id) {
        return agentRepository.findById(id)
                .map(agent -> {
                    agent.setIsActive(!agent.getIsActive());
                    Agent updatedAgent = agentRepository.save(agent);
                    return ResponseEntity.ok(updatedAgent);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}