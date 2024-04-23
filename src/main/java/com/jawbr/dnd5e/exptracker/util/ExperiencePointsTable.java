package com.jawbr.dnd5e.exptracker.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExperiencePointsTable {
    LEVEL_1(0),
    LEVEL_2(300),
    LEVEL_3(900),
    LEVEL_4(2700),
    LEVEL_5(6500),
    LEVEL_6(14000),
    LEVEL_7(23000),
    LEVEL_8(34000),
    LEVEL_9(48000),
    LEVEL_10(64000),
    LEVEL_11(85000),
    LEVEL_12(100000),
    LEVEL_13(120000),
    LEVEL_14(140000),
    LEVEL_15(165000),
    LEVEL_16(195000),
    LEVEL_17(225000),
    LEVEL_18(265000),
    LEVEL_19(305000),
    LEVEL_20(355000);

    private final int experiencePoints;
}
