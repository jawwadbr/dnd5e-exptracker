package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.response.ClassDTO;
import com.jawbr.dnd5e.exptracker.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Classes Controller", description = "Classes endpoints")
@RestController
@RequestMapping("/api/v1/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @Operation(summary = "Find all classes",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping
    public Page<ClassDTO> findAllClasses(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy
    )
    {
        return classService.findAllClasses(page, pageSize, sortBy);
    }

    @Operation(summary = "Find class using UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/{classUuid}")
    public ClassDTO findByUuid(@PathVariable UUID classUuid) {
        return classService.findByUuid(classUuid);
    }
}
