package ru.kenyo;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.util.Properties;


@SpringBootApplication
public class Application {
    private static final String PROPERTIES_FILE = String.format("%s/.db-env", System.getProperty("user.dir"));

    @SneakyThrows
    private static void setUpEnvironment() {
        FileInputStream propFile =
                new FileInputStream(PROPERTIES_FILE);
        Properties properties =
                new Properties(System.getProperties());
        properties.load(propFile);
        System.setProperties(properties);
    }

    public static void main(String[] args) {
        setUpEnvironment();
        SpringApplication.run(Application.class);
    }
}