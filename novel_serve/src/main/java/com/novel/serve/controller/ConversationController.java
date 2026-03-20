package com.novel.serve.controller;

import com.novel.serve.entity.Agent;
import com.novel.serve.entity.ConversationHistory;
import com.novel.serve.entity.Novel;
import com.novel.serve.repository.AgentRepository;
import com.novel.serve.repository.NovelRepository;
import com.novel.serve.service.AIService;
import com.novel.serve.service.MemoryService;
import com.novel.serve.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conversation")
@CrossOrigin(origins = "http://localhost:5173")
public class ConversationController {
    
    @Autowired
    private AIService aiService;
    
    @Autowired
    private MemoryService memoryService;
    
    @Autowired
    private NovelService novelService;
    
    @Autowired
    private NovelRepository novelRepository;
    
    @Autowired
    private AgentRepository agentRepository;
    
    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, Object> request) {
        try {
            Long novelId = Long.valueOf(request.get("novelId").toString());
            Long agentId = request.get("agentId") != null ? Long.valueOf(request.get("agentId").toString()) : null;
            String userMessage = request.get("message").toString();
            String action = request.get("action") != null ? request.get("action").toString() : "chat";
            
            Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new RuntimeException("Novel not found"));
            
            String contextMemory = memoryService.buildContextMemory(novelId);
            
            String systemPrompt = buildSystemPrompt(novel, agentId, action, contextMemory);
            
            String aiResponse = aiService.callAIModel(
                    agentId != null ? agentRepository.findById(agentId).get().getModelId() : 1L,
                    systemPrompt,
                    userMessage
            );
            
            memoryService.saveConversation(novelId, agentId, "user", userMessage);
            memoryService.saveConversation(novelId, agentId, "assistant", aiResponse);
            
            if ("continue".equals(action)) {
                novelService.appendContent(novelId, aiResponse);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("response", aiResponse);
            response.put("contextMemory", contextMemory);
            response.put("novel", novel);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/history/{novelId}")
    public ResponseEntity<List<ConversationHistory>> getConversationHistory(@PathVariable Long novelId) {
        return ResponseEntity.ok(memoryService.getConversationHistory(novelId));
    }
    
    @DeleteMapping("/history/{novelId}")
    public ResponseEntity<Void> clearConversationHistory(@PathVariable Long novelId) {
        memoryService.clearConversationHistory(novelId);
        return ResponseEntity.ok().build();
    }
    
    private String buildSystemPrompt(Novel novel, Long agentId, String action, String contextMemory) {
        StringBuilder promptBuilder = new StringBuilder();
        
        promptBuilder.append("你是一个专业的小说创作助手。");
        
        if (agentId != null) {
            Agent agent = agentRepository.findById(agentId).orElse(null);
            if (agent != null && agent.getSystemPrompt() != null) {
                promptBuilder.append("\n").append(agent.getSystemPrompt());
            }
            if (agent != null && agent.getInstructions() != null) {
                promptBuilder.append("\n").append(agent.getInstructions());
            }
        }
        
        promptBuilder.append("\n\n当前小说背景信息:\n").append(contextMemory);
        
        switch (action) {
            case "continue":
                promptBuilder.append("\n\n任务: 基于上述信息，续写小说内容。保持风格一致，剧情连贯。");
                break;
            case "character":
                promptBuilder.append("\n\n任务: 帮助用户完善角色设定，提供角色发展建议。");
                break;
            case "plot":
                promptBuilder.append("\n\n任务: 帮助用户规划剧情发展，提供情节建议。");
                break;
            case "dialogue":
                promptBuilder.append("\n\n任务: 生成符合角色性格的对话内容。");
                break;
            default:
                promptBuilder.append("\n\n任务: 回答用户关于小说创作的问题，提供建议和帮助。");
        }
        
        return promptBuilder.toString();
    }
}