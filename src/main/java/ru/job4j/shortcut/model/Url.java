package ru.job4j.shortcut.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "urls")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @Column(name = "url")
    @URL(message = "This is an invalid URL")
    private String urlValue;

    @Column(name = "code")
    private String code;

    @Column(name = "visit_count")
    private long visitCount;

    @ManyToOne
    @JoinColumn(name = "site_id")
    @NotNull(message = "The site of this URL can't be null")
    private Site site;
}
