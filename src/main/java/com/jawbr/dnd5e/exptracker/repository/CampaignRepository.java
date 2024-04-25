package com.jawbr.dnd5e.exptracker.repository;

import com.jawbr.dnd5e.exptracker.entity.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

//    @Query("SELECT c FROM Campaign c WHERE c.uuid = :campaignUuid")
//    Campaign findJoinedCampaignByUuid(UUID campaignUuid);

    @Query("SELECT c FROM Campaign c JOIN c.players p WHERE c.uuid = :campaignUuid AND p.id = :userId")
    Campaign findJoinedCampaignByUuidAndUserId(UUID campaignUuid, Long userId);

    @Query("SELECT c FROM Campaign c JOIN c.creator p WHERE c.uuid = :campaignUuid AND p.id = :userId")
    Campaign findCreatedCampaignByUuidAndUserId(UUID campaignUuid, Long userId);

    @Query("SELECT c FROM Campaign c JOIN c.players p WHERE p.id = :userId")
    Page<Campaign> findJoinedCampaignsByUserId(Long userId, Pageable pageable);

    @Query("SELECT c FROM Campaign c JOIN c.creator cr WHERE cr.id = :userId")
    Page<Campaign> findCreatedCampaignsByUserId(Long userId, Pageable pageable);
}
