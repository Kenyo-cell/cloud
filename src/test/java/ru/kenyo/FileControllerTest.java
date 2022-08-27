package ru.kenyo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.kenyo.controller.AuthController;
import ru.kenyo.controller.FileController;
import ru.kenyo.dto.LoginDTO;
import ru.kenyo.entity.FileEntity;
import ru.kenyo.exception.BadRequestException;
import ru.kenyo.service.AuthService;
import ru.kenyo.service.FileService;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(FileController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileControllerTest {
    private static final String FILE_PROPERTY_NAME = "file";
    private static final String FILENAME_PROPERTY_NAME = "filename";
    private static final List<FileEntity> files = Arrays.asList(
            new FileEntity("test", "content".getBytes()),
            new FileEntity("neto", "logy".getBytes()),
            new FileEntity("java", "spring".getBytes()),
            new FileEntity("kotlin", "ktor".getBytes())
    );

    private String token;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    public void successFileUploadTest() throws Exception {
        FileEntity file = files.get(files.size() - 1);
        Mockito.doNothing().when(fileService).createFile(file.getFilename(), file.getContent());
        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/file")
                        .file(FILE_PROPERTY_NAME, file.getContent())
                        .queryParam(FILENAME_PROPERTY_NAME, file.getFilename())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void badRequestFileUploadTest() throws Exception {
        FileEntity file = files.get(files.size() - 1);
        Mockito.doThrow(BadRequestException.class).when(fileService).createFile(file.getFilename(), file.getContent());
        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/file")
                        .file(FILE_PROPERTY_NAME, file.getContent())
                        .queryParam(FILENAME_PROPERTY_NAME, file.getFilename())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getFileSuccessTest() throws Exception {
        FileEntity file = files.get(files.size() - 1);
        Mockito.when(fileService.getFile(file.getFilename())).thenReturn(file);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/file")
                        .queryParam(FILENAME_PROPERTY_NAME, file.getFilename())
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(file)));
    }
}
