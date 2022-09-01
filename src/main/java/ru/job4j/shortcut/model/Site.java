package ru.job4j.shortcut.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "site")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @Column(name = "name")
    @NotBlank(message = "site name can't be empty or null")
    @Size(min = 4, max = 255, message = "name length should be between 4 and 255")
    private String name;

    @Column(name = "login")
    @EqualsAndHashCode.Include
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "registration")
    private boolean registration;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id")
    private Set<Url> urls = new HashSet<>();

    public static Site of(String login, String password, boolean registration) {
        Site site = new Site();
        site.setLogin(login);
        site.setPassword(password);
        site.setRegistration(registration);
        return site;
    }

    public static Site of(String name) {
        Site site = new Site();
        site.setName(name);
        return site;
    }
}
