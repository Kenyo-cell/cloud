package ru.kenyo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kenyo.service.FileService;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService service;

    @GetMapping("/")
    public Object getObject() {
        return new Object();
    }
}