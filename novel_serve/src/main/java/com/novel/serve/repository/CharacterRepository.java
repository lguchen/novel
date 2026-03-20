package com.novel.serve.repository;

import com.novel.serve.entity.NovelCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<NovelCharacter, Long> {
    List<NovelCharacter> findByNovelId(Long novelId);
    List<NovelCharacter> findByNovelIdAndRole(Long novelId, String role);
}