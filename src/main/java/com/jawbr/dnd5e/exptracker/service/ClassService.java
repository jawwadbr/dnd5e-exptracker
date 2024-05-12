package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.dto.mapper.ClassDTOMapper;
import com.jawbr.dnd5e.exptracker.dto.request.ClassRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.ClassDTO;
import com.jawbr.dnd5e.exptracker.entity.Class;
import com.jawbr.dnd5e.exptracker.entity.PlayerCharacter;
import com.jawbr.dnd5e.exptracker.exception.ClassNotFoundException;
import com.jawbr.dnd5e.exptracker.exception.IllegalParameterException;
import com.jawbr.dnd5e.exptracker.repository.ClassRepository;
import com.jawbr.dnd5e.exptracker.repository.PlayerCharacterRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final PlayerCharacterRepository playerCharacterRepository;
    private final ClassDTOMapper classDTOMapper;

    public ClassService(ClassRepository classRepository,
                        PlayerCharacterRepository playerCharacterRepository, ClassDTOMapper classDTOMapper)
    {
        this.classRepository = classRepository;
        this.playerCharacterRepository = playerCharacterRepository;
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

    /*
     * ADMIN ENDPOINTS
     */

    public ClassDTO saveClass(ClassRequestDTO classRequestDTO) {
        if(Optional.ofNullable(classRepository.findByName(classRequestDTO.name())).isPresent()) {
            throw new DataIntegrityViolationException(
                    String.format("Class '%s' already exists.", classRequestDTO.name()));
        }

        Class newClass = new Class();
        newClass.setName(classRequestDTO.name());
        newClass = classRepository.save(newClass);

        return ClassDTO.builder()
                .name(newClass.getName())
                .class_uuid(newClass.getUuid().toString())
                .build();
    }

    public void deleteClassByName(String className) {
        Optional.of(classRepository.findByName(className)).ifPresentOrElse(
                theClass -> {
                    List<PlayerCharacter> characters = playerCharacterRepository.findByPlayerCharClass(theClass);
                    characters.forEach(c -> c.setPlayerCharClass(null));
                    classRepository.delete(theClass);
                },
                () -> {
                    throw new ClassNotFoundException(String.format("Class '%s' not found.", className));
                }
        );
    }

    public ClassDTO updateClass(ClassRequestDTO classRequestDTO, String className) {
        return Optional.of(classRepository.findByName(className)).map((c) -> {
                    Class updatedClass = Class.builder()
                            .id(c.getId())
                            .uuid(c.getUuid())
                            .name(classRequestDTO.name())
                            .build();

                    classRepository.save(updatedClass);
                    return ClassDTO.builder()
                            .class_uuid(updatedClass.getUuid().toString())
                            .name(updatedClass.getName())
                            .build();
                })
                .orElseThrow(() ->
                        new ClassNotFoundException(String.format("Class '%s' not found.", className)));
    }
}
