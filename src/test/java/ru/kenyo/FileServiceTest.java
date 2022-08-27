package ru.kenyo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.kenyo.entity.FileEntity;
import ru.kenyo.exception.BadRequestException;
import ru.kenyo.repository.FileRepository;
import ru.kenyo.service.FileService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class FileServiceTest {
    private static final List<FileEntity> files = Arrays.asList(
            new FileEntity("test", "content".getBytes()),
            new FileEntity("neto", "logy".getBytes()),
            new FileEntity("java", "spring".getBytes()),
            new FileEntity("kotlin", "ktor".getBytes())
    );

    @MockBean
    private FileRepository fileRepository;

    @Autowired
    private FileService service;

    @Test
    public void failedUpdateFileTest() {
        Mockito.when(fileRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(
                BadRequestException.class,
                () -> service.updateFile(files.get(2).getFilename(), "scala")
        );
    }

    @Test
    public void successUpdateFileTest() {
        Mockito.when(fileRepository.findById("java")).thenReturn(Optional.of(files.get(2)));

        Assertions.assertDoesNotThrow(() -> service.updateFile(files.get(2).getFilename(), "scala"));
    }

    @Test
    public void successGetFiles() {
        Mockito.when(fileRepository.findAll()).thenReturn(files);

        Assertions.assertEquals(service.getFiles(null).size(), files.size());
    }
}
