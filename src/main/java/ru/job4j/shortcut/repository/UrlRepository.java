package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.shortcut.model.Url;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends CrudRepository<Url, Long> {

    @Override
    List<Url> findAll();

    Optional<Url> findByUrlValue(String name);

    Optional<Url> findByCode(String code);

    @Modifying
    @Query("update Url u set u.visitCount = u.visitCount + 1 where u.code=:code")
    void incrementUrlVisitCount(@Param("code") String code);
}
