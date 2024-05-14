package com.jawbr.dnd5e.exptracker.entity;

import com.jawbr.dnd5e.exptracker.util.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 80)
    private String password;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @ManyToMany(mappedBy = "players", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    private List<Campaign> joinedCampaigns;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Campaign> createdCampaigns;

    @OneToMany(mappedBy = "player", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PlayerCharacter> createdCharacters;

    @Column
    private ZonedDateTime deactivationExpirationDate;
}
