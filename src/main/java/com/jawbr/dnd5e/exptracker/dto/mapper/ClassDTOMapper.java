package com.jawbr.dnd5e.exptracker.dto.mapper;

import com.jawbr.dnd5e.exptracker.dto.response.ClassDTO;
import com.jawbr.dnd5e.exptracker.entity.Class;
import com.jawbr.dnd5e.exptracker.util.Mapper;

import java.util.function.Function;

@Mapper
public class ClassDTOMapper implements Function<Class, ClassDTO> {

    @Override
    public ClassDTO apply(Class aClass) {
        return ClassDTO.builder()
                .name(aClass.getName())
                .class_uuid(aClass.getUuid().toString())
                .build();
    }
}
