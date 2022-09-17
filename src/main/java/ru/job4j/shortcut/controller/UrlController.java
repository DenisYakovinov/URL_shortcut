package ru.job4j.shortcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.job4j.shortcut.aspect.Loggable;
import ru.job4j.shortcut.dto.UrlCreationDto;
import ru.job4j.shortcut.dto.UrlDto;
import ru.job4j.shortcut.dto.UrlDtoStatistic;
import ru.job4j.shortcut.mapper.UrlDtoMapper;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.Url;
import ru.job4j.shortcut.service.api.SiteService;
import ru.job4j.shortcut.service.api.UrlService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Loggable
@Tag(name = "urls", description = "methods for urls")
public class UrlController {

    private final UrlService urlService;
    private final SiteService siteService;
    private final UrlDtoMapper urlDtoMapper;

    public UrlController(UrlService urlService, SiteService siteService, UrlDtoMapper urlDtoMapper) {
        this.urlService = urlService;
        this.siteService = siteService;
        this.urlDtoMapper = urlDtoMapper;
    }

    @PostMapping("/convert")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "convert url to unique code (only for authorized site)")
    @Transactional
    public ResponseEntity<UrlDto> urlRegistration(@RequestBody
                                                  @Parameter(description = "site creation DTO")
                                                  UrlCreationDto urlCreationDto,
                                                  @CurrentSecurityContext(expression = "authentication?.name")
                                                  @Parameter(hidden = true)
                                                  String currentSiteLogin) {
        Url url = urlDtoMapper.toModel(urlCreationDto);
        url.setSite(siteService.getByLogin(currentSiteLogin));
        return ResponseEntity.status(HttpStatus.CREATED).body(urlDtoMapper.toDto(urlService.save(url)));
    }

    @GetMapping("/redirect/{code}")
    @Operation(summary = "redirect to url by code")
    public ResponseEntity<Void> redirectToUrlByCode(@PathVariable("code")
                                                    @Parameter(description = "url code", required = true)
                                                    String code) {
        Url url = urlService.getByCode(code);
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", url.getUrlValue()).build();
    }

    @GetMapping("/statistic")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "get statistic for a site (only for authorized site)")
    public ResponseEntity<List<UrlDtoStatistic>> getStatisticBySite(
                                                   @CurrentSecurityContext(expression = "authentication?.name")
                                                   @Parameter(hidden = true)
                                                   String currentSiteLogin) {
        Site site = siteService.getByLoginWithUrls(currentSiteLogin);
        return ResponseEntity.status(HttpStatus.OK).body(site.getUrls().stream()
                                                                       .map(urlDtoMapper::toDtoStatistic)
                                                                       .collect(Collectors.toList()));
    }
}
