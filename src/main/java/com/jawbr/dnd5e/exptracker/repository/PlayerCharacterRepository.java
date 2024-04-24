package com.jawbr.dnd5e.exptracker.repository;

import com.jawbr.dnd5e.exptracker.entity.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {
}
