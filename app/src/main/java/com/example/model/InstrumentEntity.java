package com.example.model;

import com.example.abstractions.symbology.InstrumentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "instruments")
public class InstrumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String code;

    private String exchange;

    private String description;

    private LocalDate expiration;

    private InstrumentType type;
}
