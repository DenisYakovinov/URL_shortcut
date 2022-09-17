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
@Schema(description = "url visit statistic response (for authorized site)")
public class UrlDtoStatistic {

    @Schema(description = "url value")
    private String urlValue;

    @Schema(description = "visit count")
    private long visitCount;

    public static UrlDtoStatistic of(String urlValue, long visitCount) {
        UrlDtoStatistic urlDtoStatistic = new UrlDtoStatistic();
        urlDtoStatistic.setUrlValue(urlValue);
        urlDtoStatistic.setVisitCount(visitCount);
        return urlDtoStatistic;
    }
}
