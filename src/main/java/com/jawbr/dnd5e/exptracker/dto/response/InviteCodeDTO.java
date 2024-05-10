package com.jawbr.dnd5e.exptracker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record InviteCodeDTO(
        String code,
        String expiry_date,
        CampaignDTO campaign
) {
}
