package com.example.repository;

import com.example.model.InstrumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentEntityRepository extends JpaRepository<InstrumentEntity, Long> {
}
