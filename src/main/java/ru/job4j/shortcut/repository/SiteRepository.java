package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.shortcut.model.Site;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiteRepository extends CrudRepository<Site, Long> {

    @Override
    List<Site> findAll();

    @Query("from Site s left join fetch s.urls where s.login = :sLogin")
    Optional<Site> findByLoginWithUrls(@Param("sLogin") String login);

    Optional<Site> findByLogin(String login);

    Optional<Site> findByName(String name);
}
