package com.jawbr.dnd5e.exptracker.entity;

import com.jawbr.dnd5e.exptracker.util.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String username;

    @Column(unique = true)
    private String email;

    @Column(length = 80)
    private String password;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    /*
     * Usuario podera criar "campanha" e dentro dessa campanha terá seus Jogadores/Personagens
     * Lá ele poderá especificar quanto de XP cada Jogadores/Personagens ganhou em tal sessão
     * Pode tanto especificar XP para todos e adicionar e também especificar uma quantia de XP individualmente
     */

}
