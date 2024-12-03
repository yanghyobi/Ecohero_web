package com.example.ecohero.service;

import com.example.ecohero.entity.RecyclingRule;
import com.example.ecohero.repository.RecyclingRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatbotService {

    private final RecyclingRuleRepository recyclingRuleRepository;

    public ChatbotService(RecyclingRuleRepository recyclingRuleRepository) {
        this.recyclingRuleRepository = recyclingRuleRepository;
    }

    // 지역과 물질에 맞는 규정을 반환하는 로직
    public String getRecyclingRule(String region, String material) {
        if (material == null || material.isBlank()) {
            return "물질 정보를 입력해주세요.";
        }

        // 지역과 물질 정보가 모두 있는 경우
        if (region != null && !region.isBlank()) {
            Optional<RecyclingRule> rule = recyclingRuleRepository.findByRegionAndMaterial(region, material);
            if (rule.isPresent()) {
                return rule.get().getRegulation();
            }
        }

        // 물질만 있는 경우
        List<RecyclingRule> materialRules = recyclingRuleRepository.findByMaterial(material);
        if (!materialRules.isEmpty()) {
            return materialRules.get(0).getRegulation(); // 첫 번째 규칙 반환
        }

        // 규정이 없는 경우
        return "해당 물질에 대한 분리수거 정보가 없습니다.";
    }

}
