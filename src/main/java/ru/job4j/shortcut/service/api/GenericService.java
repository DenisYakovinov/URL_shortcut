package ru.job4j.shortcut.service.api;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface GenericService<T, ID> {

    T getById(ID id);

    T save(@Valid T model);
}
