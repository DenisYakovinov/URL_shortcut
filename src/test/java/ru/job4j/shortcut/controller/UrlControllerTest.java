package ru.job4j.shortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.shortcut.UrlShortcutStarter;
import ru.job4j.shortcut.dto.UrlCreationDto;
import ru.job4j.shortcut.dto.UrlDto;
import ru.job4j.shortcut.dto.UrlDtoStatistic;
import ru.job4j.shortcut.exception.EntityNotFoundException;
import ru.job4j.shortcut.exception.UrlValueReservedException;
import ru.job4j.shortcut.mapper.UrlDtoMapper;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.Url;
import ru.job4j.shortcut.service.api.SiteService;
import ru.job4j.shortcut.service.api.UrlService;

import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UrlShortcutStarter.class)
@AutoConfigureMockMvc
class UrlControllerTest {

    private static final String MOCKED_SECURITY_LOGIN = "user";

    @MockBean
    SiteService siteService;

    @MockBean
    UrlService urlService;

    @MockBean
    UrlDtoMapper urlDtoMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void urlRegistration_shouldCallServiceThenReturnJsonUrlDto_whenHttpPostRegistrationMethod() throws Exception {
        Url url = Url.of("url");
        UrlCreationDto urlCreationDto = new UrlCreationDto();
        urlCreationDto.setUrl("url");
        UrlDto urlDto = new UrlDto();
        urlDto.setCode("code");
        when(urlService.save(url)).thenReturn(url);
        when(urlDtoMapper.toModel(urlCreationDto)).thenReturn(url);
        when(urlDtoMapper.toDto(url)).thenReturn(urlDto);
        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(urlCreationDto)))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.code").value(urlDto.getCode()));
        verify(siteService).getByLogin(MOCKED_SECURITY_LOGIN);
    }

    @Test
    @WithMockUser
    void urlRegistration_shouldReturnConflictStatusWithJsonMessage_whenServiceThrowUrlValueReservedException()
            throws Exception {
        UrlCreationDto urlCreationDto = new UrlCreationDto();
        urlCreationDto.setUrl("url");
        Url url = new Url();
        when(urlDtoMapper.toModel(urlCreationDto)).thenReturn(url);
        when(urlService.save(url)).thenThrow(new UrlValueReservedException("url reserved"));
        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(urlCreationDto)))
                .andDo(print())
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.message").value("url reserved"));
    }

    @Test
    void redirectToUrlByCode_shouldRedirectViaLocationHeader_whenHttpGetMethodByCode() throws Exception {
        Url url = Url.of("url");
        when(urlService.getByCode("code")).thenReturn(url);
        mockMvc.perform(get("/redirect/code"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(header().string("Location", url.getUrlValue()));
    }

    @Test
    void redirectToUrlByCode_shouldReturnNotFoundStatusWithJsonMessage_whenServiceThrowEntityNotFoundException()
            throws Exception {
        when(urlService.getByCode("code")).thenThrow(new EntityNotFoundException("not found"));
        mockMvc.perform(get("/redirect/code"))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.details").value("not found"));
    }

    @Test
    @WithMockUser
    void getStatisticBySite_shouldReturnStatisticJsonList_whenHttpGetMethodBySite() throws Exception {
        Url firstUrl = Url.of("firstUrl");
        Url secondUrl = Url.of("secondUrl");
        UrlDtoStatistic firstDto = UrlDtoStatistic.of("first", 1);
        UrlDtoStatistic secondDto = UrlDtoStatistic.of("second", 2);
        Site site = new Site();
        site.setUrls(Set.of(firstUrl, secondUrl));
        when(siteService.getByLoginWithUrls(MOCKED_SECURITY_LOGIN)).thenReturn(site);
        when(urlDtoMapper.toDtoStatistic(firstUrl)).thenReturn(firstDto);
        when(urlDtoMapper.toDtoStatistic(secondUrl)).thenReturn(secondDto);
        mockMvc.perform(get("/statistic"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[*].urlValue").value(containsInAnyOrder("first", "second")));
    }

    @Test
    @WithMockUser
    void getStatisticBySite_shouldReturnNotFoundStatusWithJsonMessage_whenServiceThrowEntityNotFoundException()
            throws Exception {
        when(siteService.getByLoginWithUrls(MOCKED_SECURITY_LOGIN)).thenThrow(new EntityNotFoundException("not found"));
        mockMvc.perform(get("/statistic"))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.details").value("not found"));
    }
}
