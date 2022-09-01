package ru.job4j.shortcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.shortcut.aspect.Loggable;
import ru.job4j.shortcut.dto.SiteCreationDto;
import ru.job4j.shortcut.dto.SiteRegDto;
import ru.job4j.shortcut.mapper.SiteDtoMapper;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.service.api.SiteService;

@RestController
@Loggable
@Tag(name = "sites", description = "methods for sites")
public class SiteController {

    private final SiteService siteService;
    private final SiteDtoMapper dtoMapper;

    public SiteController(SiteService siteService, SiteDtoMapper dtoMapper) {
        this.siteService = siteService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping("/registration")
    @Operation(summary = "register a site")
    public ResponseEntity<SiteRegDto> registration(@RequestBody
                                                   @Parameter(description = "site creation DTO")
                                                   SiteCreationDto siteCreationDto) {
        Site site = dtoMapper.toModel(siteCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toDto(siteService.save(site)));
    }
}
