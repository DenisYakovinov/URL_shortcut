package ru.job4j.shortcut.service.api;

import ru.job4j.shortcut.model.Site;

public interface SiteService extends GenericService<Site, Long> {

    Site getByLogin(String login);

    Site getByLoginWithUrls(String name);
}
