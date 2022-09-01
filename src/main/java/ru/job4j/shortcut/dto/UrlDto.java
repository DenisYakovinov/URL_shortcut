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
@Schema(description = "created url response")
public class UrlDto {

    @Schema(description = "random unique code for url created automatically")
    private String code;
}
