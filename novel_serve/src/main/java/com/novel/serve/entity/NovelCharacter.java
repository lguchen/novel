package com.novel.serve.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "characters")
public class NovelCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String role;
    private String personality;
    
    @Column(columnDefinition = "TEXT")
    private String background;
    
    @Column(columnDefinition = "TEXT")
    private String appearance;
    
    @Column(columnDefinition = "TEXT")
    private String relationships;
    
    private Long novelId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}