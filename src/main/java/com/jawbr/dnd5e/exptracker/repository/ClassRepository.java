package com.jawbr.dnd5e.exptracker.repository;

import com.jawbr.dnd5e.exptracker.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassRepository extends JpaRepository<Class, Long> {

    Class findByUuid(UUID classUuid);
}
