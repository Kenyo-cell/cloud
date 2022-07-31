package ru.kenyo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kenyo.dto.FilePreviewDTO;
import ru.kenyo.entity.FileEntity;
import ru.kenyo.exception.BadRequestException;
import ru.kenyo.repository.FileRepository;

import java.util.List;
import java.util.stream.Collectors;

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
        FileEntity updatedEntity = new FileEntity(newName, file.getContent());
        repository.deleteById(filename);
        repository.save(updatedEntity);
    }

    public List<FilePreviewDTO> getFiles(Integer limit) {
        List<FileEntity> entities;
        if (limit == null) entities = repository.findAll();
        else entities = repository.findAllWithLimit(limit);
        return entities.stream()
                .map(entity -> new FilePreviewDTO(entity.getFilename(), entity.getContent().length))
                .collect(Collectors.toList());
    }
}