package ru.job4j.shortcut.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.shortcut.exception.EntityNotFoundException;
import ru.job4j.shortcut.exception.UrlValueReservedException;
import ru.job4j.shortcut.model.Url;
import ru.job4j.shortcut.repository.UrlRepository;
import ru.job4j.shortcut.service.codegenerator.CodeGenerator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    private static final long ID = 1L;
    private static final String CODE = "code";

    @Mock
    UrlRepository urlRepository;

    @Mock
    CodeGenerator codeGenerator;

    @InjectMocks
    UrlServiceImpl urlService;

    @Test
    void getById_shouldReturnUrl_whenUrlId() {
        Url expected = new Url();
        Mockito.when(urlRepository.findById(ID)).thenReturn(Optional.of(expected));
        assertEquals(expected, urlService.getById(ID));
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenUrlWithIdDoesNotExist() {
        Mockito.when(urlRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> urlService.getById(ID));
    }

    @Test
    void getByCode_shouldReturnUrlWithCode_whenUrlCode() {
        Url expected = new Url();
        Mockito.when(urlRepository.findByCode(CODE)).thenReturn(Optional.of(expected));
        assertEquals(expected, urlService.getByCode(CODE));
    }

    @Test
    void getByCode_shouldThrowEntityNotFoundException_whenUrlWithCodeDoesNotExist() {
        Mockito.when(urlRepository.findByCode(CODE)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> urlService.getByCode(CODE));
    }

    @Test
    void save_shouldReturnSavedUrl_whenUrl() {
        Url expected = new Url();
        Mockito.when(urlRepository.save(expected)).thenReturn(expected);
        assertEquals(expected, urlService.save(expected));
        verify(codeGenerator, times(1)).generateCode();
    }

    @Test
    void save_shouldThrowUrlValueReservedException_whenUrlExists() {
        Url url = Url.of("existingUrl");
        Mockito.when(urlRepository.findByUrlValue("existingUrl")).thenReturn(Optional.of(url));
        assertThrows(UrlValueReservedException.class, () -> urlService.save(url));
    }

    @Test
    void incrementUrlVisitCount_shouldCallIncrementInInjectedUrlRepository() {
        urlService.incrementUrlVisitCount(CODE);
        verify(urlRepository, times(1)).incrementUrlVisitCount(CODE);
    }
}