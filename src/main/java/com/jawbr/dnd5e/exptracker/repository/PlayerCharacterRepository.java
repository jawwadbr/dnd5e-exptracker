package com.jawbr.dnd5e.exptracker.repository;

import com.jawbr.dnd5e.exptracker.entity.Class;
import com.jawbr.dnd5e.exptracker.entity.PlayerCharacter;
import com.jawbr.dnd5e.exptracker.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {

    @Query("SELECT pc FROM PlayerCharacter pc JOIN pc.campaign c JOIN pc.player p WHERE p.id = :userId AND c.uuid = :campaignUuid")
    List<PlayerCharacter> findAllUserPlayerCharacterOnSingleCampaign(UUID campaignUuid, Long userId);

    @Query("SELECT pc from PlayerCharacter pc JOIN pc.player p WHERE p.id = :userId AND pc.uuid = :characterUuid")
    PlayerCharacter findUserPlayerCharacterByUuid(UUID characterUuid, Long userId);

    List<PlayerCharacter> findByPlayerRace(Race race);

    List<PlayerCharacter> findByPlayerCharClass(Class theClass);

    PlayerCharacter findByUuid(UUID characterUuid);
}
