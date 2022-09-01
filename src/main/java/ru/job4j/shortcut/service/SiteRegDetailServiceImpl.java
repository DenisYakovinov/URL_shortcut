package ru.job4j.shortcut.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.service.api.SiteService;

import java.util.Collections;

@Service
public class SiteRegDetailServiceImpl implements UserDetailsService {

    private final SiteService siteService;

    public SiteRegDetailServiceImpl(SiteService siteService) {
        this.siteService = siteService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Site site = siteService.getByLogin(login);
        return new User(site.getLogin(), site.getPassword(), Collections.emptyList());
    }
}
