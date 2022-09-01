package ru.job4j.shortcut.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.shortcut.aspect.Loggable;
import ru.job4j.shortcut.exception.EntityNotFoundException;
import ru.job4j.shortcut.exception.SiteNameReservedException;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.service.api.SiteService;
import ru.job4j.shortcut.service.codegenerator.CodeGenerator;

@Service
@Loggable
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;
    private final CodeGenerator codeGenerator;
    private final PasswordEncoder encoder;

    public SiteServiceImpl(SiteRepository siteRepository, CodeGenerator codeGenerator, PasswordEncoder encoder) {
        this.siteRepository = siteRepository;
        this.codeGenerator = codeGenerator;
        this.encoder = encoder;
    }

    @Override
    public Site getById(Long id) {
        return siteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("site with id = %d wasn't found.", id)));
    }

    @Override
    @Transactional
    public Site save(Site site) {
        if (siteRepository.findByLogin(site.getName()).isPresent()) {
            throw new SiteNameReservedException(String.format("The site name '%s' already reserved please try again",
                    site.getName()));
        }
        site.setLogin(codeGenerator.generateCode());
        String notEncodedPass = codeGenerator.generateCode();
        site.setPassword(encoder.encode(notEncodedPass));
        site.setRegistration(true);
        siteRepository.save(site);
        return Site.of(site.getLogin(), notEncodedPass, true);
    }

    @Override
    public Site getByLogin(String login) {
        return siteRepository.findByLogin(login).orElseThrow(() -> new EntityNotFoundException(
                String.format("site with login %s wasn't found.", login)));
    }

    @Override
    public Site getByLoginWithUrls(String name) {
        return siteRepository.findByLoginWithUrls(name).orElseThrow(() -> new EntityNotFoundException(
                String.format("site %s wasn't found.", name)));
    }
}
