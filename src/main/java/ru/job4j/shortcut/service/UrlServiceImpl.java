package ru.job4j.shortcut.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.shortcut.aspect.Loggable;
import ru.job4j.shortcut.exception.EntityNotFoundException;
import ru.job4j.shortcut.exception.UrlValueReservedException;
import ru.job4j.shortcut.model.Url;
import ru.job4j.shortcut.repository.UrlRepository;
import ru.job4j.shortcut.service.api.UrlService;
import ru.job4j.shortcut.service.codegenerator.CodeGenerator;

@Service
@Loggable
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final CodeGenerator codeGenerator;

    public UrlServiceImpl(UrlRepository urlRepository, CodeGenerator codeGenerator) {
        this.urlRepository = urlRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public Url getById(Long id) {
        return urlRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("url with id = %d wasn't found.", id)));
    }

    @Override
    @Transactional
    public Url save(Url url) {
        if (urlRepository.findByUrlValue(url.getUrlValue()).isPresent()) {
            throw new UrlValueReservedException(String.format("The url '%s' already reserved please try again",
                    url.getUrlValue()));
        }
        url.setCode(codeGenerator.generateCode());
        return urlRepository.save(url);
    }

    @Override
    @Transactional
    public Url getByCode(String code) {
        Url url = urlRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException(
                String.format("url with code = %s wasn't found.", code)));
        incrementUrlVisitCount(code);
        return url;
    }

    @Override
    public void incrementUrlVisitCount(String code) {
        urlRepository.incrementUrlVisitCount(code);
    }
}
