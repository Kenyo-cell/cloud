package ru.kenyo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kenyo.entity.FileEntity;
import ru.kenyo.exception.BadRequestException;
import ru.kenyo.repository.FileRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository repository;

    public FileEntity getFile(String filename) {
        return repository.findById(filename)
                .orElseThrow(() -> new BadRequestException("There's no such file with name %s".formatted(filename)));
    }

    public void createFile(String filename, byte[] content) {
        repository.save(new FileEntity(filename, content));
    }

    public void deleteFile(String filename) {
        repository.delete(
                repository.findById(filename)
                        .orElseThrow(
                                () -> new BadRequestException("There's no such file with name %s".formatted(filename))
                        )
        );
    }

    public void updateFile(String filename, String newName) throws BadRequestException {
        FileEntity file = repository.findById(filename)
                .orElseThrow(() -> new BadRequestException("There's no such file with name %s".formatted(filename)));
        file.setFilename(newName);
        repository.save(file);
    }

    public List<FileEntity> getFiles(Integer limit) {
        if (limit == null) return repository.findAll();
        return repository.findAllWithLimit(limit);
    }
}