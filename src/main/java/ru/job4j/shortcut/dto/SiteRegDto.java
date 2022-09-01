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
@Schema(description = "registered site response")
public class SiteRegDto {

    @Schema(description = "random unique login created automatically")
    private String login;

    @Schema(description = "random unique password created automatically")
    private String password;

    @Schema(description = "shows that the site is registered")
    private boolean registration;
}
