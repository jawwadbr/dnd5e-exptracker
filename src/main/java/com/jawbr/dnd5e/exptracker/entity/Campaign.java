package com.jawbr.dnd5e.exptracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    private String name;

    private String description;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "campaign_joined_users",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> players;

    @OneToMany(mappedBy = "campaign", orphanRemoval = true)
    private List<PlayerCharacter> playerCharacters;

    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    private User creator;

}
