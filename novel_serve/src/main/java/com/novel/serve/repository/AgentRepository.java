package com.novel.serve.repository;

import com.novel.serve.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    List<Agent> findByIsActive(Boolean isActive);
    List<Agent> findByModelId(Long modelId);
    List<Agent> findByRole(String role);
}