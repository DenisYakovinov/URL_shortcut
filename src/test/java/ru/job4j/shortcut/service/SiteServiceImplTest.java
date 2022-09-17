package ru.job4j.shortcut.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.job4j.shortcut.exception.EntityNotFoundException;
import ru.job4j.shortcut.exception.SiteNameReservedException;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.Url;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.service.codegenerator.CodeGenerator;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SiteServiceImplTest {

    private static final long ID = 1L;
    private static final String LOGIN = "login";

    @Mock
    SiteRepository siteRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    CodeGenerator codeGenerator;

    @InjectMocks
    SiteServiceImpl siteService;

    @Test
    void getById_shouldReturnSite_whenSiteId() {
        Site expected = new Site();
        Mockito.when(siteRepository.findById(ID)).thenReturn(Optional.of(expected));
        assertEquals(expected, siteService.getById(ID));
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenSiteWithIdDoesNotExist() {
        Mockito.when(siteRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> siteService.getById(ID));
    }

    @Test
    void getByLogin_shouldReturnSite_whenSiteLogin() {
        Site expected = new Site();
        Mockito.when(siteRepository.findByLogin(LOGIN)).thenReturn(Optional.of(expected));
        assertEquals(expected, siteService.getByLogin(LOGIN));
    }

    @Test
    void getByLogin_shouldThrowEntityNotFoundException_whenSiteWithLoginDoesNotExist() {
        Mockito.when(siteRepository.findByLogin("notExisting")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> siteService.getByLogin("notExisting"));
    }

    @Test
    void getByLoginWithUrls_shouldReturnWithUrls_whenSiteLogin() {
        Site site = new Site();
        site.setLogin(LOGIN);
        site.setUrls(Set.of(Url.of("first"), Url.of("second")));
        Mockito.when(siteRepository.findByLoginWithUrls(LOGIN)).thenReturn(Optional.of(site));
        assertEquals(site, siteService.getByLoginWithUrls(LOGIN));
    }

    @Test
    void getByLoginWithUrls_shouldThrowEntityNotFoundException_whenSiteWithLoginDoesNotExist() {
        Mockito.when(siteRepository.findByLogin("notExisting")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> siteService.getByLogin("notExisting"));
    }

    @Test
    void save_shouldReturnSavedSite_whenSite() {
        Site site = new Site();
        Mockito.when(codeGenerator.generateCode()).thenReturn("mockRandomCode");
        Mockito.when(siteRepository.save(site)).thenReturn(site);
        Site actual = siteService.save(site);
        assertTrue(actual.isRegistration());
        assertEquals(site, actual);
        verify(codeGenerator, times(2)).generateCode();
        verify(passwordEncoder, times(1)).encode("mockRandomCode");
    }

    @Test
    void save_shouldThrowSiteNameReservedException_whenSiteNameExists() {
        Site site = Site.of("existingSite");
        Mockito.when(siteRepository.findByName("existingSite")).thenReturn(Optional.of(site));
        assertThrows(SiteNameReservedException.class, () -> siteService.save(site));
    }
}
