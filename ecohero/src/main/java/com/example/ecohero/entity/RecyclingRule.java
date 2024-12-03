package com.example.ecohero.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recycling_rule")
public class RecyclingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String region;       // 지역명
    private String material;     // 물질 종류 (예: '플라스틱', '종이')
    private String regulation;   // 분리수거 규정

    // 기본 생성자
    public RecyclingRule() {}

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }
}
