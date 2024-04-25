package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.dto.mapper.ClassDTOMapper;
import com.jawbr.dnd5e.exptracker.dto.response.ClassDTO;
import com.jawbr.dnd5e.exptracker.entity.Class;
import com.jawbr.dnd5e.exptracker.exception.ClassNotFoundException;
import com.jawbr.dnd5e.exptracker.exception.IllegalParameterException;
import com.jawbr.dnd5e.exptracker.repository.ClassRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final ClassDTOMapper classDTOMapper;

    public ClassService(ClassRepository classRepository,
                        ClassDTOMapper classDTOMapper)
    {
        this.classRepository = classRepository;
        this.classDTOMapper = classDTOMapper;
    }

    public Page<ClassDTO> findAllClasses(Integer page, Integer pageSize, String sortBy) {
        page = Optional.ofNullable(page).orElse(0);
        pageSize = Math.min(Optional.ofNullable(pageSize).orElse(6), 15);
        String sortByField = Optional.ofNullable(sortBy)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    if(s.equals("name"))
                        return "name";
                    else
                        throw new IllegalParameterException(String.format("Parameter '%s' is illegal.", sortBy));
                })
                .orElse("id");

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortByField));

        Page<Class> classes = Optional.of(classRepository.findAll(pageable))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new ClassNotFoundException("No classes found."));

        return classes.map(classDTOMapper);
    }

    public ClassDTO findByUuid(UUID classUuid) {
        return Optional.ofNullable(classRepository.findByUuid(classUuid))
                .map(classDTOMapper)
                .orElseThrow(() -> new ClassNotFoundException("No classes found."));
    }
}
