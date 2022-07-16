package ru.kenyo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kenyo.repository.FileRepository;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository repository;
}