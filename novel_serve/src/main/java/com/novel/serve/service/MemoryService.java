package com.novel.serve.service;

import com.novel.serve.entity.*;
import com.novel.serve.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemoryService {
    
    @Autowired
    private NovelRepository novelRepository;
    
    @Autowired
    private CharacterRepository characterRepository;
    
    @Autowired
    private WorldSettingRepository worldSettingRepository;
    
    @Autowired
    private ConversationHistoryRepository conversationHistoryRepository;
    
    public String buildContextMemory(Long novelId) {
        StringBuilder contextBuilder = new StringBuilder();
        
        Novel novel = novelRepository.findById(novelId).orElse(null);
        if (novel == null) {
            return "";
        }
        
        contextBuilder.append("小说标题: ").append(novel.getTitle()).append("\n");
        contextBuilder.append("类型: ").append(novel.getGenre()).append("\n");
        contextBuilder.append("描述: ").append(novel.getDescription()).append("\n\n");
        
        if (novel.getStyleGuide() != null && !novel.getStyleGuide().isEmpty()) {
            contextBuilder.append("写作风格指南:\n").append(novel.getStyleGuide()).append("\n\n");
        }
        
        List<NovelCharacter> characters = characterRepository.findByNovelId(novelId);
        if (!characters.isEmpty()) {
            contextBuilder.append("角色设定:\n");
            for (NovelCharacter character : characters) {
                contextBuilder.append("- ").append(character.getName())
                        .append(" (").append(character.getRole()).append(")\n");
                if (character.getPersonality() != null) {
                    contextBuilder.append("  性格: ").append(character.getPersonality()).append("\n");
                }
                if (character.getBackground() != null) {
                    contextBuilder.append("  背景: ").append(character.getBackground()).append("\n");
                }
                if (character.getAppearance() != null) {
                    contextBuilder.append("  外貌: ").append(character.getAppearance()).append("\n");
                }
                if (character.getRelationships() != null) {
                    contextBuilder.append("  关系: ").append(character.getRelationships()).append("\n");
                }
                contextBuilder.append("\n");
            }
        }
        
        List<WorldSetting> worldSettings = worldSettingRepository.findByNovelId(novelId);
        if (!worldSettings.isEmpty()) {
            contextBuilder.append("世界观设定:\n");
            for (WorldSetting setting : worldSettings) {
                contextBuilder.append("- ").append(setting.getName())
                        .append(" (").append(setting.getCategory()).append(")\n");
                if (setting.getDescription() != null) {
                    contextBuilder.append("  描述: ").append(setting.getDescription()).append("\n");
                }
                if (setting.getRules() != null) {
                    contextBuilder.append("  规则: ").append(setting.getRules()).append("\n");
                }
                if (setting.getGeography() != null) {
                    contextBuilder.append("  地理: ").append(setting.getGeography()).append("\n");
                }
                if (setting.getHistory() != null) {
                    contextBuilder.append("  历史: ").append(setting.getHistory()).append("\n");
                }
                contextBuilder.append("\n");
            }
        }
        
        List<ConversationHistory> recentConversations = conversationHistoryRepository
                .findByNovelIdOrderBySequenceAsc(novelId)
                .stream()
                .skip(Math.max(0, conversationHistoryRepository.countByNovelId(novelId) - 10))
                .collect(Collectors.toList());
        
        if (!recentConversations.isEmpty()) {
            contextBuilder.append("最近对话记录:\n");
            for (ConversationHistory history : recentConversations) {
                contextBuilder.append(history.getRole()).append(": ").append(history.getContent()).append("\n");
            }
            contextBuilder.append("\n");
        }
        
        if (novel.getContent() != null && !novel.getContent().isEmpty()) {
            contextBuilder.append("当前小说内容:\n");
            contextBuilder.append(novel.getContent()).append("\n\n");
        }
        
        String contextMemory = contextBuilder.toString();
        novel.setContextMemory(contextMemory);
        novelRepository.save(novel);
        
        return contextMemory;
    }
    
    public void saveConversation(Long novelId, Long agentId, String role, String content) {
        Integer sequence = conversationHistoryRepository.countByNovelId(novelId) + 1;
        
        ConversationHistory history = new ConversationHistory();
        history.setNovelId(novelId);
        history.setAgentId(agentId);
        history.setRole(role);
        history.setContent(content);
        history.setSequence(sequence);
        
        conversationHistoryRepository.save(history);
    }
    
    public List<ConversationHistory> getConversationHistory(Long novelId) {
        return conversationHistoryRepository.findByNovelIdOrderBySequenceAsc(novelId);
    }
    
    public void clearConversationHistory(Long novelId) {
        conversationHistoryRepository.deleteByNovelId(novelId);
    }
}