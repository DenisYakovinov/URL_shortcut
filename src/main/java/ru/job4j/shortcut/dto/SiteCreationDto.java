package ru.job4j.shortcut.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Schema(description = "site creation  request")
public class SiteCreationDto {

    @Schema(description = "site value")
    private String site;
}
