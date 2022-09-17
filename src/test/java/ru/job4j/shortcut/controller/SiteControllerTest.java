package ru.job4j.shortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.shortcut.UrlShortcutStarter;
import ru.job4j.shortcut.dto.SiteCreationDto;
import ru.job4j.shortcut.dto.SiteRegDto;
import ru.job4j.shortcut.dto.UrlCreationDto;
import ru.job4j.shortcut.exception.SiteNameReservedException;
import ru.job4j.shortcut.exception.UrlValueReservedException;
import ru.job4j.shortcut.mapper.SiteDtoMapper;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.Url;
import ru.job4j.shortcut.service.api.SiteService;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UrlShortcutStarter.class)
@AutoConfigureMockMvc
class SiteControllerTest {

    @MockBean
    SiteService siteService;

    @MockBean
    SiteDtoMapper siteDtoMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void registration_shouldCallServiceThenReturnJsonSiteRegDto_whenPostRegistrationMethod() throws Exception {
        Site site = Site.of("site");
        Site savedSite = Site.of("login", "pass", true);
        SiteCreationDto siteCreationDto = new SiteCreationDto();
        siteCreationDto.setSite("site");
        SiteRegDto siteRegDto = SiteRegDto.of("login", "pass", true);
        Mockito.when(siteDtoMapper.toModel(siteCreationDto)).thenReturn(site);
        Mockito.when(siteDtoMapper.toDto(savedSite)).thenReturn(siteRegDto);
        Mockito.when(siteService.save(site)).thenReturn(savedSite);
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(siteCreationDto)))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.login").value(savedSite.getLogin()))
                .andExpect(jsonPath("$.password").value(savedSite.getPassword()));
    }

    @Test
    void registration_shouldReturnConflictStatusWithJsonMessage_whenServiceThrowSiteNameReservedException()
            throws Exception {
        SiteCreationDto siteCreationDto = new SiteCreationDto();
        siteCreationDto.setSite("site");
        Site site = Site.of("site");
        when(siteDtoMapper.toModel(siteCreationDto)).thenReturn(site);
        when(siteService.save(site)).thenThrow(new SiteNameReservedException("site reserved"));
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(siteCreationDto)))
                .andDo(print())
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.message").value("site reserved"));
    }
}
