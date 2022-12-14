package ru.job4j.shortcut.service.api;

import ru.job4j.shortcut.model.Url;

public interface UrlService extends GenericService<Url, Long> {

    Url getByCode(String code);

    void incrementUrlVisitCount(String code);
}
