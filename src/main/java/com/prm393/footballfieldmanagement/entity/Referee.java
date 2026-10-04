package com.prm393.footballfieldmanagement.entity;

import com.prm393.footballfieldmanagement.enums.RefereeQualityLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "referees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Referee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "referee_id")
    private Long refereeId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "quality_level")
    private RefereeQualityLevel qualityLevel;

    @Column(name = "hourly_rate", precision = 19, scale = 2)
    private BigDecimal hourlyRate;

    @Column(name = "is_active")
    private boolean isActive;
}
