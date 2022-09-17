package ru.job4j.shortcut.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.job4j.shortcut.config.LiquibaseConfig;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.Url;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(LiquibaseConfig.class)
class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void incrementUrlVisitCount_shouldIncrementVisitToGivenUrl() {
        Site site = Site.of("login", "password", true);
        site.setName("site");
        entityManager.persist(site);
        Url url = Url.of("https://www.test.com/index.html");
        url.setCode("code");
        url.setSite(site);
        entityManager.persist(url);
        urlRepository.incrementUrlVisitCount("code");
        entityManager.flush();
        entityManager.clear();
        assertThat(urlRepository.findByUrlValue("https://www.test.com/index.html")
                .orElseThrow(RuntimeException::new).getVisitCount()).isEqualTo(1);
    }
}