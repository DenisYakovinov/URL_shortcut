package ru.job4j.shortcut.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import ru.job4j.shortcut.exception.EntityNotFoundException;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.service.api.SiteService;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SiteRegDetailServiceImplTest {

    @Mock
    SiteService siteService;

    @InjectMocks
    SiteRegDetailServiceImpl siteRegDetailService;

    @Test
    void loadUserByUsername_shouldReturnSpringSecurityUserObject_whenSiteLogin() {
        Site site = Site.of("login", "pass", true);
        Mockito.when(siteService.getByLogin("login")).thenReturn(site);
        User expected =  new User(site.getLogin(), site.getPassword(), Collections.emptyList());
        assertEquals(expected, siteRegDetailService.loadUserByUsername("login"));
    }

    @Test
    void loadUserByUsername_shouldThrowEntityNotFoundException_whenSiteWithLoginDoesNotExist() {
        Mockito.when(siteService.getByLogin("notExisting")).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> siteRegDetailService.loadUserByUsername("notExisting"));
    }
}
