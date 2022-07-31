package ru.kenyo.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "file")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileEntity {
    @Id
    String filename;

    @Lob
    byte[] content;
}
