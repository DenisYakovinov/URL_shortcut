package ru.job4j.shortcut.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.shortcut.dto.SiteCreationDto;
import ru.job4j.shortcut.dto.SiteRegDto;
import ru.job4j.shortcut.model.Site;

@Mapper(componentModel = "spring")
public interface SiteDtoMapper {

    SiteRegDto toDto(Site site);

    @Mapping(target = "name", source = "site")
    Site toModel(SiteCreationDto siteCreationDto);
}
