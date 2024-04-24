package com.jawbr.dnd5e.exptracker.repository;

import com.jawbr.dnd5e.exptracker.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceRepository extends JpaRepository<Race, Long> {
}
