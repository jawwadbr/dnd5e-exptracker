package com.jawbr.dnd5e.exptracker.dto.mapper;

import com.jawbr.dnd5e.exptracker.dto.response.CampaignDTO;
import com.jawbr.dnd5e.exptracker.dto.response.InviteCodeDTO;
import com.jawbr.dnd5e.exptracker.entity.InviteCode;
import com.jawbr.dnd5e.exptracker.util.Mapper;

import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Mapper
public class InviteCodeDTOMapper implements Function<InviteCode, InviteCodeDTO> {

    @Override
    public InviteCodeDTO apply(InviteCode code) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss a");

        return InviteCodeDTO.builder()
                .code(code.getCode())
                .expiry_date(code.getExpiryDate().format(formatter) + " UTC")
                .campaign(CampaignDTO.builder()
                        .name(code.getCampaign().getName())
                        .campaign_creator(CampaignDTO.CampaignCreatorDTO.builder()
                                .username(code.getCampaign().getCreator().getUsername())
                                .public_uuid(code.getCampaign().getCreator().getUuid().toString())
                                .build())
                        .public_campaign_uuid(code.getCampaign().getUuid().toString())
                        .build())
                .build();
    }
}
