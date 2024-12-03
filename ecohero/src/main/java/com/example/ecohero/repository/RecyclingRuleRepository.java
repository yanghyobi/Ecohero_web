package com.example.ecohero.repository;

import com.example.ecohero.entity.RecyclingRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecyclingRuleRepository extends JpaRepository<RecyclingRule, Long> {
    // 지역과 물질에 따른 분리수거 규정을 찾는 메서드
    Optional<RecyclingRule> findByRegionAndMaterial(String region, String material);

    // 물질에 따른 분리수거 규칙 목록을 반환하는 메서드
    List<RecyclingRule> findByMaterial(String material);
}
