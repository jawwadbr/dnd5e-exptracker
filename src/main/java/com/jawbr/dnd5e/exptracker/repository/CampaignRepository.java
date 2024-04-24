package com.jawbr.dnd5e.exptracker.repository;

import com.jawbr.dnd5e.exptracker.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
