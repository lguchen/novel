package com.novel.serve.repository;

import com.novel.serve.entity.ConversationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationHistoryRepository extends JpaRepository<ConversationHistory, Long> {
    List<ConversationHistory> findByNovelIdOrderBySequenceAsc(Long novelId);
    List<ConversationHistory> findByNovelIdAndAgentIdOrderBySequenceAsc(Long novelId, Long agentId);
    Integer countByNovelId(Long novelId);
    void deleteByNovelId(Long novelId);
}