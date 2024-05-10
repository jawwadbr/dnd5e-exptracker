package com.jawbr.dnd5e.exptracker.repository;

import com.jawbr.dnd5e.exptracker.entity.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InviteCodeRepository extends JpaRepository<InviteCode, Long> {

    List<InviteCode> findByExpiryDateBefore(LocalDateTime time);

    boolean existsByCode(String code);
}
