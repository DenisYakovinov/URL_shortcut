package ru.job4j.shortcut.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.shortcut.dto.UrlCreationDto;
import ru.job4j.shortcut.dto.UrlDto;
import ru.job4j.shortcut.dto.UrlDtoStatistic;
import ru.job4j.shortcut.model.Url;

@Mapper(componentModel = "spring")
public interface UrlDtoMapper {

    UrlDto toDto(Url url);

    @Mapping(target = "urlValue", source = "url")
    Url toModel(UrlCreationDto urlCreationDto);

    UrlDtoStatistic toDtoStatistic(Url url);
}
