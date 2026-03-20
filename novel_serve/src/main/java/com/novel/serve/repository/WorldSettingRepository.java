package com.novel.serve.repository;

import com.novel.serve.entity.WorldSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorldSettingRepository extends JpaRepository<WorldSetting, Long> {
    List<WorldSetting> findByNovelId(Long novelId);
    List<WorldSetting> findByNovelIdAndCategory(Long novelId, String category);
}