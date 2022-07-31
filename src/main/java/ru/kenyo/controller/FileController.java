package ru.kenyo.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kenyo.service.FileService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService service;

    @PostMapping("file")
    @SneakyThrows
    public ResponseEntity<?> postFile(@RequestParam String filename, @RequestPart MultipartFile file) {
        service.createFile(filename, file.getBytes());
        return ResponseEntity.ok().build();
    }

    @GetMapping("file")
    public ResponseEntity<?> getFile(@RequestParam String filename) {
        return ResponseEntity.ok().body(service.getFile(filename));
    }

    @DeleteMapping("file")
    public ResponseEntity<?> deleteFile(@RequestParam String filename) {
        service.deleteFile(filename);
        return ResponseEntity.ok().build();
    }

    @PutMapping("file")
    public ResponseEntity<?> updateFile(@RequestParam String filename, @RequestBody String name) {
        service.updateFile(filename, name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("list")
    public ResponseEntity<List<?>> getFiles(@RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok().body(service.getFiles(limit));
    }
}